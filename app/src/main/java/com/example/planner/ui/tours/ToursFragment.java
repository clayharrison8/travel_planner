package com.example.planner.ui.tours;

import android.content.SharedPreferences;
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
import com.example.planner.POIAdapter;
import com.example.planner.R;
import com.example.planner.Reusable;
import com.example.planner.databinding.FragmentToursBinding;
import com.example.planner.model.POI;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class ToursFragment extends Fragment {

    private POIAdapter poiAdapter = new POIAdapter();
    private SharedPreferences sharedPreferences;
    private POI POI;
    private JSONRequests jsonRequests = new JSONRequests();
    private Reusable reusable = new Reusable();

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FragmentToursBinding fragmentToursBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_tours, container, false);

        POI = MainActivity.getPOI();
        sharedPreferences = container.getContext().getSharedPreferences("myKey", MODE_PRIVATE);

        setData();

        reusable.setUpPOIRecyclerView(getContext(), poiAdapter, fragmentToursBinding.viewCities );

        jsonRequests.getTours(poiAdapter, POI);

        // Inflate the layout for this fragment
        return fragmentToursBinding.getRoot();

    }

    // Saving data to SharedPreferences
    private void setData(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("button", "tour");
        editor.apply();
    }

    // Showing toolbar and setting title when fragment resumes
    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).show();
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle("Tours in " + POI.getName());

    }

    // Hiding toolbar when fragment stops
    @Override
    public void onStop() {
        super.onStop();
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).hide();
    }

}
