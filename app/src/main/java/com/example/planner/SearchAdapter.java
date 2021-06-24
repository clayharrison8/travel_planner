package com.example.planner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planner.adapter.ImageBindingAdapter;
import com.example.planner.model.POI;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> implements Filterable {
    private ArrayList<POI> countries;
    private ArrayList<POI> countriesFull;
    private View view;
    private MainActivity mainActivity;
    private Reusable reusable = new Reusable();

    SearchAdapter(ArrayList<POI> countries1, MainActivity mainActivity) {
        countries = countries1;
        countriesFull = new ArrayList<>(countries);
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public SearchAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search, viewGroup, false);

        return new SearchAdapter.MyViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull final SearchAdapter.MyViewHolder myViewHolder, final int i) {
        myViewHolder.name.setText(countries.get(i).getName());
        ImageBindingAdapter.bindSearchThumbnail(myViewHolder.image, countries.get(i).getImages().get(0).getSizes().getMedium().getUrl());

        myViewHolder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Saving country and going to country page
                MainActivity.savePOI(countries.get(i));
                mainActivity.resetSearch();
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
        TextView name;
        ImageView image;
        LinearLayout card;
        RecyclerView recyclerView;

        MyViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.cityName);
            image = itemView.findViewById(R.id.ivPic);
            card = itemView.findViewById(R.id.card);
            recyclerView = itemView.findViewById(R.id.favourites_recycler);

        }

    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    // Making new list featuring filtered countries
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<POI> filteredList = new ArrayList<>();

            // Check if input field is empty
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(countriesFull);
            } else {

                // Getting user input
                String filterPattern = constraint.toString().toLowerCase().trim();

                // Looping through country list and add country if it contains user input
                for (POI POI : countriesFull) {
                    if (POI.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(POI);

                    }
                }

            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }

        // Displaying new list featuring list of filtered countries
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            countries.clear();
            countries.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };

}
