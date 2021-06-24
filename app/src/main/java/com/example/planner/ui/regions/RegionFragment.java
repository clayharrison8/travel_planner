package com.example.planner.ui.regions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planner.MainActivity;
import com.example.planner.Reusable;
import com.example.planner.ui.cities.CitiesFragment;
import com.example.planner.R;
import com.example.planner.adapter.ImageBindingAdapter;

import java.util.Random;

public class RegionFragment extends Fragment {


    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private Random rand = new Random();
    private Reusable reusable = new Reusable();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_region, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        Button country_button = view.findViewById(R.id.country_button);
        TextView country_name = view.findViewById(R.id.name);
        TextView country_snippet = view.findViewById(R.id.country_snippet);
        ImageView imageView = view.findViewById(R.id.images);
        com.example.planner.model.POI POI = MainActivity.getPOI();


        country_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = reusable.transactions(v);
                transaction.add(R.id.frag_container, new CitiesFragment()).commit();
            }
        });

        MainActivity.setUpContentRecyclerView(recyclerView, getContext(), adapter,layoutManager, POI.getStructured_content().getSections());

        country_name.setText(POI.getName());
        country_snippet.setText(POI.getSnippet());
        ImageBindingAdapter.bindThumbnail(imageView, POI.getImages().get(rand.nextInt(POI.getImages().size())).getSizes().getMedium().getUrl());
        
        // Inflate the layout for this fragment
        return view;
    }


}
