package com.example.planner;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.planner.databinding.FragmentHomeBinding;
import com.example.planner.model.POI;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class HomeFragment extends Fragment {
    private RetrofitAdapter retrofitAdapter = new RetrofitAdapter();
    private FragmentHomeBinding fragmentHomeBinding;
    private JSONRequests jsonRequests = new JSONRequests();

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        container.setBackgroundColor(getResources().getColor(android.R.color.white));

        fragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        setUpRecyclerView();
        jsonRequests.getPopularCities(retrofitAdapter);

        // Setting up searchable list if it hasnt been created
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Searchable List");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    System.out.println("Exists");
                }
                else {
                    jsonRequests.getSearchBar();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        // Setting up planner list if it hasnt been created
        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child("Planner List");

        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    System.out.println("Exists");
                }
                else {
                    jsonRequests.getPlannerCityList();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        // Inflate the layout for this fragment
        return fragmentHomeBinding.getRoot();
    }

    private void setUpRecyclerView() {
        // bind RecyclerView
        RecyclerView recyclerView = fragmentHomeBinding.homeRecycler;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        // Create an instance of the adapter and set on RecyclerView
        recyclerView.setAdapter(retrofitAdapter);
    }

    // Showing toolbar and setting title when fragment starts
    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).show();
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle("Home");
    }

}



