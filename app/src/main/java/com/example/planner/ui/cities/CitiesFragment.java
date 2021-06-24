package com.example.planner.ui.cities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planner.JSONRequests;
import com.example.planner.MainActivity;
import com.example.planner.R;
import com.example.planner.RetrofitAdapter;
import com.example.planner.Reusable;
import com.example.planner.api.RetrofitClient;
import com.example.planner.api.TriposoAPI;
import com.example.planner.databinding.FragmentCitiesBinding;
import com.example.planner.model.POI;
import com.example.planner.model.InitialResponse;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CitiesFragment extends Fragment {

    private RetrofitAdapter retrofitAdapter = new RetrofitAdapter();
    private FragmentCitiesBinding fragmentCitiesBinding;
    private POI poi;
    private JSONRequests jsonRequests = new JSONRequests();

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Setting up view
        fragmentCitiesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_cities,container,false);

        poi = MainActivity.getPOI();

        setUpRecyclerView();

        jsonRequests.getCity(poi, retrofitAdapter);

        // Inflate the layout for this fragment
        return fragmentCitiesBinding.getRoot();

    }

    public void setUpRecyclerView(){
        // bind RecyclerView
        RecyclerView recyclerView = fragmentCitiesBinding.viewCities;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        // Create an instance of the adapter and set on RecyclerView
        recyclerView.setAdapter(retrofitAdapter);
    }


    // Hiding toolbar and setting title when fragment resumed
    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).show();
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle("Cities of " + poi.getName());

    }



}
