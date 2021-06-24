package com.example.planner.ui.mytrips;

import android.content.SharedPreferences;
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
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

public class MyTripsAdapter extends RecyclerView.Adapter<MyTripsAdapter.MyViewHolder> {
    private ArrayList<POI> countries;
    private View view;
    private DatabaseReference databaseReference;
    private SharedPreferences sharedPreferences;
    private Random rand = new Random();
    private Reusable reusable = new Reusable();


    MyTripsAdapter(ArrayList<POI> countries1) {
        countries = countries1;
    }

    @NonNull
    @Override
    public MyTripsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mytrips_recycler, viewGroup, false);
        sharedPreferences = view.getContext().getSharedPreferences("myKey", MODE_PRIVATE);


        // Get current user and My Trips node
        databaseReference = FirebaseDatabase.getInstance().getReference();
        String userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        databaseReference = databaseReference.child("users").child(userID).child("My Trips");


        return new MyTripsAdapter.MyViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull final MyTripsAdapter.MyViewHolder myViewHolder, int i) {
        myViewHolder.name.setText(countries.get(i).getName());
        ImageBindingAdapter.bindThumbnail(myViewHolder.imageView, countries.get(i).getImages().get(rand.nextInt(countries.get(i).getImages().size())).getSizes().getMedium().getUrl());





        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction transaction = reusable.transactions(view);
                transaction.add(R.id.frag_container, new MyTripsResultsFragment()).addToBackStack(null).commit();
                setData(countries.get(myViewHolder.getAdapterPosition()).getId(), countries.get(myViewHolder.getAdapterPosition()).getUniqueID());
            }
        });

        myViewHolder.dotsPopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("clicked");
                // Inflating popup menu
                PopupMenu popup = new PopupMenu(view.getContext(), view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.popup_menu, popup.getMenu());

                // Removing irrelevant menu items
                popup.getMenu().findItem(R.id.book).setVisible(false);
                popup.getMenu().findItem(R.id.add_to_favs).setVisible(false);
                popup.getMenu().findItem(R.id.remove_from_favs).setVisible(false);
                popup.getMenu().findItem(R.id.show_in_map).setVisible(false);


                // Show popup menu
                popup.show();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        deleteTrip(myViewHolder.getAdapterPosition());
                        return true;
                    }
                });

            }
        });

//        myViewHolder.snippet.setText(countries.get(i).getSnippet());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return countries.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
         TextView name;
        LinearLayout cardView;
        ImageView imageView, dotsPopUp;


         MyViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.cityName);
            cardView = itemView.findViewById(R.id.card);
            imageView = itemView.findViewById(R.id.ivPic);
            dotsPopUp = itemView.findViewById(R.id.dotsPopUp);

        }

    }

    // Saving data to SharedPreferences
    private void setData(String myTrips, String myTripsID) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("MyTrips", myTrips);
        editor.putString("MyTripsID", myTripsID);
        editor.apply();
    }

    private void deleteTrip(final int i) {

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (countries.get(i) != null) {
                    String id = countries.get(i).getId();
                    System.out.println("It exists");
                    // Checking if favourites node exists and if country clicked exists
                    if (dataSnapshot.exists() && dataSnapshot.child(id).exists()) {
                        System.out.println("It exists");
                        System.out.println(databaseReference.child(id));
                        // Removing country from database
                        dataSnapshot.child(countries.get(i).getId()).child(countries.get(i).getUniqueID()).getRef().removeValue();
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