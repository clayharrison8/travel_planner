package com.example.planner;

import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planner.databinding.RetroItemBinding;
import com.example.planner.model.POI;
import com.example.planner.ui.attractions.AttractionFragment;
import com.example.planner.ui.cities.CityFragment;
import com.example.planner.ui.eating_out.EatDrinkFragment;
import com.example.planner.ui.hotels.HotelFragment;
import com.example.planner.ui.nightlife.NightlifeFragment;
import com.example.planner.ui.regions.RegionFragment;
import com.example.planner.ui.tours.TourFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

public class RetrofitAdapter extends RecyclerView.Adapter<RetrofitAdapter.MyViewHolder> implements CustomClickListener {
    private List<POI> pois;
    private CustomClickListener customClickListener;
    private View view;
    private String button;
    private DatabaseReference databaseReference;
    private Random rand = new Random();
    private Reusable reusable = new Reusable();


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        // Getting the root view
        RetroItemBinding retroItemBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.retro_item, viewGroup, false);

        view = viewGroup;

        databaseReference = FirebaseDatabase.getInstance().getReference();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // Getting current user and retrieving favourites node
        assert user != null;
        String userID = user.getUid();
        databaseReference = databaseReference.child("users").child(userID).child("Favourites");

        SharedPreferences sharedPref = view.getContext().getSharedPreferences("myKey", MODE_PRIVATE);
        button = sharedPref.getString("button", "");


        // set the view's size, margins, paddings and layout parameters
        return new MyViewHolder(retroItemBinding);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.bindTo(pois.get(i));
        myViewHolder.retroItemBinding.setItemClickListener(this);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (pois != null) {
            return pois.size();
        } else {
            return 0;
        }
    }

    void setPOIList(ArrayList<POI> cities) {
        this.pois = cities;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private RetroItemBinding retroItemBinding;

        MyViewHolder(@NonNull RetroItemBinding retroItemBinding) {
            super(retroItemBinding.getRoot());

            this.retroItemBinding = retroItemBinding;
        }

        void bindTo(POI POI) {
            // Binding items
            retroItemBinding.setVariable(BR.city, POI);
            retroItemBinding.setVariable(BR.itemClickListener, customClickListener);
            retroItemBinding.setVariable(BR.adapterPosition, getLayoutPosition());
            retroItemBinding.setVariable(BR.countryImageMedium, POI.getImages().get(rand.nextInt(POI.getImages().size())).getSizes().getMedium());

            retroItemBinding.executePendingBindings();

        }
    }

    public void cardClicked(POI POI) {

        MainActivity.savePOI(POI);
        FragmentTransaction transaction = reusable.transactions(view);

        // Checking POI and button saved in SharedPreferences to determine which page to go to
        if (POI.getType() != null && POI.getType().equals("city")) {
            transaction.add(R.id.frag_container, new CityFragment()).addToBackStack(null).commit();
        } else if (POI.getType() != null && POI.getType().equals("country")) {
            transaction.add(R.id.frag_container, new CountryFragment()).addToBackStack(null).commit();
        } else if (button != null && button.equals("tour")) {
            transaction.add(R.id.frag_container, new TourFragment()).addToBackStack(null).commit();
        } else if (button != null && button.equals("region")) {
            transaction.add(R.id.frag_container, new RegionFragment()).addToBackStack(null).commit();
        } else if (button != null && button.equals("eating out")) {
            transaction.add(R.id.frag_container, new EatDrinkFragment()).addToBackStack(null).commit();
        } else if (button != null && button.equals("hotels")) {
            transaction.add(R.id.frag_container, new HotelFragment()).addToBackStack(null).commit();
        } else if (button != null && button.equals("nightlife")) {
            transaction.add(R.id.frag_container, new NightlifeFragment()).addToBackStack(null).commit();
        } else if (button != null && button.equals("attractions")) {
            transaction.add(R.id.frag_container, new AttractionFragment()).addToBackStack(null).commit();
        }

    }

    public void PopUpClicked(final POI poi, Integer integer) {

        // Inflating popup menu
        PopupMenu popup = new PopupMenu(view.getContext(), view, Gravity.CENTER);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.popup_menu, popup.getMenu());

        // Removing irrelevant menu items
        popup.getMenu().findItem(R.id.remove_from_favs).setVisible(false);
        popup.getMenu().findItem(R.id.remove).setVisible(false);


        if (button.equals("tour")) {
            popup.getMenu().findItem(R.id.add_to_favs).setVisible(false);

        }

        // Determining which menu items to remove (Depending on current page)
        if (poi.getType() != null && !poi.getType().equals("country")) {
            popup.getMenu().findItem(R.id.add_to_favs).setVisible(false);
        }
        if (poi.getBookingInfo() == null) {
            popup.getMenu().findItem(R.id.book).setVisible(false);
        }
        if (poi.getCoordinates() == null) {
            popup.getMenu().findItem(R.id.show_in_map).setVisible(false);
        }


        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.add_to_favs:
                        // Adding item to favourites node in database
                        databaseReference.child(poi.getId()).setValue(poi);
                        MainActivity.displayToast("Country added to Favourites", view.getContext());
                        break;
                    case R.id.show_in_map:
                        // Showing POI in google maps
                        MainActivity.showInMap(poi.getCoordinates().getLat(), poi.getCoordinates().getLng(), poi.getName(), view.getContext());
                        break;

                    default:
                        return false;
                }
                return true;
            }
        });
        popup.show();


    }


}
