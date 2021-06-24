package com.example.planner.ui.favourites;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planner.CountryFragment;
import com.example.planner.MainActivity;
import com.example.planner.R;
import com.example.planner.Reusable;
import com.example.planner.adapter.ImageBindingAdapter;
import com.example.planner.model.POI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.MyViewHolder> {
    private ArrayList<POI> countries;
    private View view;
    private DatabaseReference databaseReference;
    private Random rand = new Random();
    private Reusable reusable = new Reusable();


    FavouritesAdapter(ArrayList<POI> countries1) {
        countries = countries1;
    }

    @NonNull
    @Override
    public FavouritesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.favourites, viewGroup, false);

        // Getting reference to database
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Getting favourites node in database
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String userID = user.getUid();
        databaseReference = databaseReference.child("users").child(userID).child("Favourites");

        return new FavouritesAdapter.MyViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull final FavouritesAdapter.MyViewHolder myViewHolder, int i) {
        myViewHolder.name.setText(countries.get(i).getName());
        myViewHolder.snippet.setText(countries.get(i).getSnippet());
        ImageBindingAdapter.bindThumbnail(myViewHolder.image, countries.get(i).getImages().get(rand.nextInt(countries.get(i).getImages().size())).getSizes().getMedium().getUrl());


        myViewHolder.popUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Inflating popup menu
                PopupMenu popup = new PopupMenu(view.getContext(), view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.popup_menu, popup.getMenu());

                // Removing irrelevant menu items
                popup.getMenu().findItem(R.id.book).setVisible(false);
                popup.getMenu().findItem(R.id.add_to_favs).setVisible(false);
                popup.getMenu().findItem(R.id.remove).setVisible(false);


                // Show popup menu
                popup.show();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.remove_from_favs:
                                // Delete country selected
                                deleteFavourites(myViewHolder.getAdapterPosition());
                                break;
                            case R.id.show_in_map:
                                // Display country in google maps
                                MainActivity.showInMap(countries.get(myViewHolder.getAdapterPosition()).getCoordinates().getLat(), countries.get(myViewHolder.getAdapterPosition()).getCoordinates().getLng(), countries.get(myViewHolder.getAdapterPosition()).getName(), view.getContext());
                                break;
                            default:
                                return false;
                        }
                        return true;
                    }
                });

            }
        });

        myViewHolder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.savePOI(countries.get(myViewHolder.getAdapterPosition()));
                FragmentTransaction transaction = reusable.transactions(view);
                transaction.add(R.id.frag_container, new CountryFragment()).addToBackStack(null).commit();
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return countries.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name, snippet;
        private ImageView image, popUp;
        private LinearLayout card;

        private MyViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.cityName);
            snippet = itemView.findViewById(R.id.citySnippet);
            image = itemView.findViewById(R.id.ivPic);
            popUp = itemView.findViewById(R.id.dotsPopUp);
            card = itemView.findViewById(R.id.card);

        }

    }

    private void deleteFavourites(final int i) {

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (countries.get(i).getId() != null) {
                    String id = countries.get(i).getId();
                    System.out.println("It exists");
                    // Checking if favourites node exists and if country clicked exists
                    if (dataSnapshot.exists() && dataSnapshot.child(id).exists()) {
                        System.out.println("It exists");
                        System.out.println(databaseReference.child(id));
                        // Removing country from database
                        dataSnapshot.child(countries.get(i).getId()).getRef().removeValue();
                        // Removing country from ArrayList
                        countries.remove(i);
                        notifyItemRemoved(i);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }


}
