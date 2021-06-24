package com.example.planner.ui.cities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planner.MainActivity;
import com.example.planner.Reusable;
import com.example.planner.ui.eating_out.EatingOutFragment;
import com.example.planner.ui.hotels.HotelsFragment;
import com.example.planner.ui.nightlife.NightlifesFragment;
import com.example.planner.R;
import com.example.planner.ui.tours.ToursFragment;
import com.example.planner.adapter.ImageBindingAdapter;
import com.example.planner.ui.attractions.AttractionsFragment;

import java.util.Objects;
import java.util.Random;

public class CityFragment extends Fragment {


    private Button tours, attractions, hotels, nightlife, eating_out;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private View view;
    private Reusable reusable = new Reusable();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_city, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        tours = view.findViewById(R.id.tours);
        hotels = view.findViewById(R.id.hotels);
        nightlife = view.findViewById(R.id.night_life);
        attractions = view.findViewById(R.id.top_attractions);
        TextView country_name = view.findViewById(R.id.name);
        TextView country_snippet = view.findViewById(R.id.country_snippet);
        eating_out = view.findViewById(R.id.eating_out);
        com.example.planner.model.POI POI = MainActivity.getPOI();
        ImageView imageView = view.findViewById(R.id.images);

        onClickListener();

        Random random = new Random();

        MainActivity.setUpContentRecyclerView(recyclerView, getContext(), adapter,layoutManager, POI.getStructured_content().getSections());
        country_name.setText(POI.getName());
        country_snippet.setText(POI.getSnippet());
        // Adding random image from array
        ImageBindingAdapter.bindThumbnail(imageView, POI.getImages().get(random.nextInt(POI.getImages().size())).getSizes().getMedium().getUrl());
        
        // Inflate the layout for this fragment
        return view;
    }


    private void onClickListener(){
        tours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = reusable.transactions(view);
                transaction.add(R.id.frag_container, new ToursFragment()).addToBackStack(null).commit();
            }
        });

        attractions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = reusable.transactions(view);
                transaction.add(R.id.frag_container, new AttractionsFragment()).addToBackStack(null).commit();
            }
        });

        hotels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = reusable.transactions(view);
                transaction.add(R.id.frag_container, new HotelsFragment()).addToBackStack(null).commit();
            }
        });

        nightlife.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = reusable.transactions(view);
                transaction.add(R.id.frag_container, new NightlifesFragment()).addToBackStack(null).commit();
            }
        });

        eating_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = reusable.transactions(view);
                transaction.add(R.id.frag_container, new EatingOutFragment()).addToBackStack(null).commit();
            }
        });

    }

    // Removing toolbar when fragment loaded
    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).hide();
    }

    // Adding toolbar back when fragment stopped
    @Override
    public void onStop() {
        super.onStop();
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).show();
    }

}
