package com.example.planner.ui.hotels;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planner.MainActivity;
import com.example.planner.R;
import com.example.planner.adapter.ImageBindingAdapter;

import java.util.Random;

public class HotelFragment extends Fragment {


    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private Random rand = new Random();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_hotel, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        TextView country_name = view.findViewById(R.id.name);
        TextView country_snippet = view.findViewById(R.id.country_snippet);
        ImageView imageView = view.findViewById(R.id.images);
        com.example.planner.model.POI POI = MainActivity.getPOI();

        MainActivity.setUpContentRecyclerView(recyclerView, getContext(), adapter,layoutManager, POI.getStructured_content().getSections());
        country_name.setText(POI.getName());
        country_snippet.setText(POI.getSnippet());
        ImageBindingAdapter.bindThumbnail(imageView, POI.getImages().get(rand.nextInt(POI.getImages().size())).getSizes().getMedium().getUrl());

        // Inflate the layout for this fragment
        return view;
    }

}
