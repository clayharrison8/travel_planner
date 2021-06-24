package com.example.planner.ui.tours;

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
import com.example.planner.model.POI;
import com.example.planner.R;
import com.example.planner.adapter.ImageBindingAdapter;

import java.util.Random;

public class TourFragment extends Fragment {


    private TextView country_snippet, country_name;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private View view;
    private ImageView imageView;
    private POI POI;
    Random rand = new Random();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_tour, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        country_name = view.findViewById(R.id.name);
        country_snippet = view.findViewById(R.id.country_snippet);
        imageView = view.findViewById(R.id.images);
        POI = MainActivity.getPOI();

        MainActivity.setUpContentRecyclerView(recyclerView, getContext(), adapter,layoutManager, POI.getStructured_content().getSections());

        country_name.setText(POI.getName());
        country_snippet.setText(POI.getSnippet());
        ImageBindingAdapter.bindThumbnail(imageView, POI.getImages().get(rand.nextInt(POI.getImages().size())).getSizes().getMedium().getUrl());

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

}
