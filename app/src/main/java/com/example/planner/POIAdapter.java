package com.example.planner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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

import com.example.planner.databinding.PoiItemBinding;
import com.example.planner.model.POI;
import com.example.planner.ui.attractions.AttractionFragment;
import com.example.planner.ui.eating_out.EatDrinkFragment;
import com.example.planner.ui.hotels.HotelFragment;
import com.example.planner.ui.nightlife.NightlifeFragment;
import com.example.planner.ui.regions.RegionFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

public class POIAdapter extends RecyclerView.Adapter<POIAdapter.MyViewHolder> implements CustomClickListener {
    private List<POI> pois;
    private CustomClickListener customClickListener;
    private View view;
    private String button;
    private Random rand = new Random();
    private Reusable reusable = new Reusable();


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        // Getting the root view
        PoiItemBinding poiItemBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.poi_item, viewGroup, false);

        view = viewGroup;

        SharedPreferences sharedPref = view.getContext().getSharedPreferences("myKey", MODE_PRIVATE);
        button = sharedPref.getString("button", "");


        // set the view's size, margins, paddings and layout parameters
        return new MyViewHolder(poiItemBinding);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.bindTo(pois.get(i));
        myViewHolder.poiItemBinding.setItemClickListener(this);

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

        private PoiItemBinding poiItemBinding;

        MyViewHolder(@NonNull PoiItemBinding poiItemBinding) {
            super(poiItemBinding.getRoot());

            this.poiItemBinding = poiItemBinding;
        }

        // Binding classes
        void bindTo(POI POI) {
            poiItemBinding.setVariable(BR.city, POI);
            poiItemBinding.setVariable(BR.itemClickListener, customClickListener);
            poiItemBinding.setVariable(BR.adapterPosition, getLayoutPosition());
            poiItemBinding.setVariable(BR.countryImageMedium, POI.getImages().get(rand.nextInt(POI.getImages().size())).getSizes().getMedium());

            poiItemBinding.executePendingBindings();

        }
    }

    public void cardClicked(POI POI) {

        MainActivity.savePOI(POI);

        FragmentTransaction transaction = reusable.transactions(view);

        // Checking what page user is on
        if (button != null && button.equals("region")) {
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
        PopupMenu popup = new PopupMenu(view.getContext(), view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.popup_menu, popup.getMenu());

        // Removing irrelevant menu items
        popup.getMenu().findItem(R.id.remove_from_favs).setVisible(false);
        popup.getMenu().findItem(R.id.remove).setVisible(false);


        if (button.equals("tour") || button.equals("attractions") || button.equals("hotels") || button.equals("nightlife") || button.equals("eating out")) {
            popup.getMenu().findItem(R.id.add_to_favs).setVisible(false);

        }

        if (poi.getType() != null && !poi.getType().equals("country")) {
            popup.getMenu().findItem(R.id.add_to_favs).setVisible(false);
        }
        if (poi.getBookingInfo() == null) {
            popup.getMenu().findItem(R.id.book).setVisible(false);
        }
        if (poi.getCoordinates() == null) {
            popup.getMenu().findItem(R.id.show_in_map).setVisible(false);
        }

        popup.show();

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.show_in_map:
                        MainActivity.showInMap(poi.getCoordinates().getLat(), poi.getCoordinates().getLng(), poi.getName(), view.getContext());
                        break;
                    case R.id.book:
                        // Redirecting user to booking page
                        Uri uri = Uri.parse(poi.getBookingInfo().getVendor_object_url());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        view.getContext().startActivity(intent);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });

    }


}
