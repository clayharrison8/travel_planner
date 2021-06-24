package com.example.planner.ui.regions;

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
import com.example.planner.R;
import com.example.planner.RetrofitAdapter;
import com.example.planner.databinding.FragmentRegionsBinding;
import com.example.planner.model.POI;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class RegionsFragment extends Fragment {

    private RetrofitAdapter retrofitAdapter = new RetrofitAdapter();;
    private FragmentRegionsBinding fragmentRegionsBinding;
    private JSONRequests jsonRequests = new JSONRequests();
    private SharedPreferences sharedPreferences;
    private POI poi;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentRegionsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_regions,container,false);

        poi = MainActivity.getPOI();

        sharedPreferences = container.getContext().getSharedPreferences("myKey", MODE_PRIVATE);

        setData();

        setUpRecyclerView();

        jsonRequests.getRegions(poi, retrofitAdapter);

        // Inflate the layout for this fragment
        return fragmentRegionsBinding.getRoot();

    }


    public void setUpRecyclerView(){
        // bind RecyclerView
        RecyclerView recyclerView = fragmentRegionsBinding.viewCities;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        // Create an instance of the adapter and set on RecyclerView
        recyclerView.setAdapter(retrofitAdapter);
    }


    // Saving data to SharedPreferences
    private void setData(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("button", "region");
        editor.apply();
    }

    // Removing toolbar when fragment loaded
    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle("Regions in " + poi.getName());
    }

}
