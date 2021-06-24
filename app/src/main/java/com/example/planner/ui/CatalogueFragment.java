package com.example.planner.ui;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.planner.JSONRequests;
import com.example.planner.R;
import com.example.planner.RetrofitAdapter;
import com.example.planner.databinding.FragmentCatalogueBinding;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CatalogueFragment extends Fragment {
    private RetrofitAdapter retrofitAdapter = new RetrofitAdapter();
    private FragmentCatalogueBinding fragmentCatalogueBinding;
    private JSONRequests jsonRequests = new JSONRequests();

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentCatalogueBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_catalogue,container,false);
        Objects.requireNonNull(getActivity()).setTitle("Catalogue");

        setUpRecyclerView();

        jsonRequests.getPopularCountries(retrofitAdapter);

        // Inflate the layout for this fragment
        return fragmentCatalogueBinding.getRoot();
    }


    public void setUpRecyclerView(){
        // bind RecyclerView
        RecyclerView recyclerView = fragmentCatalogueBinding.catalogueRecycler;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        // Create an instance of the adapter and set on RecyclerView
        recyclerView.setAdapter(retrofitAdapter);
    }



}
