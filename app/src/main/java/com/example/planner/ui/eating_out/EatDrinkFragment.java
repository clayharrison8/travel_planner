package com.example.planner.ui.eating_out;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planner.MainActivity;
import com.example.planner.R;
import com.example.planner.adapter.ImageBindingAdapter;
import com.example.planner.model.POI;

import java.util.Objects;
import java.util.Random;

public class EatDrinkFragment extends Fragment {


    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private Random rand = new Random();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Setting up view
        View view = inflater.inflate(R.layout.fragment_eatdrink, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        TextView country_name = view.findViewById(R.id.name);
        TextView country_snippet = view.findViewById(R.id.country_snippet);
        ImageView imageView = view.findViewById(R.id.images);
        POI poi = MainActivity.getPOI();

        MainActivity.setUpContentRecyclerView(recyclerView, getContext(), adapter,layoutManager, poi.getStructured_content().getSections());

        country_name.setText(poi.getName());
        country_snippet.setText(poi.getSnippet());
        // Setting picture to random image from arraylist
        ImageBindingAdapter.bindThumbnail(imageView, poi.getImages().get(rand.nextInt(poi.getImages().size())).getSizes().getMedium().getUrl());

        // Inflate the layout for this fragment
        return view;
    }

    // Hiding toolbar when fragment resumes
    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).hide();
    }

    // Showing toolbar when fragment stops
    @Override
    public void onStop() {
        super.onStop();
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).show();
    }

}
