package com.example.planner.ui.mytrips;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planner.R;
import com.example.planner.Reusable;
import com.example.planner.model.POI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class MyTripsResultsFragment extends Fragment {

    private ArrayList<POI> activities = new ArrayList<>();
    private View view;
    private String mytrips, myTripsID;
    private List<List<POI>> partitions = new ArrayList<>();
    private DatabaseReference databaseReference;
    private long divideBy;
    private Reusable reusable = new Reusable();
    private RecyclerView daysRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_mytrips_results, container, false);
        daysRecyclerView = view.findViewById(R.id.planner_day);

        getData();

        // Getting current user and My Trips node
        String userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(userID).child("My Trips");

        MyTrips();

        // Inflate the layout for this fragment
        return view;
    }


    // Retrieving data from SharedPreferences
    private void getData() {
        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("myKey", MODE_PRIVATE);
        mytrips = sharedPreferences.getString("MyTrips", "");
        myTripsID = sharedPreferences.getString("MyTripsID", "");

    }

    private void setUpRecyclerView() {
        // bind RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.viewCities);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Create an instance of the adapter and set on RecyclerView
        MyTripsResultsAdapter myTripsResultsAdapter = new MyTripsResultsAdapter(partitions);
        recyclerView.setAdapter(myTripsResultsAdapter);

    }

    private void MyTrips(){
        // Getting the number of days and list of activities from database
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.child(mytrips).child(myTripsID).child("days").getChildren()){
                    divideBy = ds.getChildrenCount();
                    for (DataSnapshot child: ds.getChildren()){
                        POI c = child.getValue(POI.class);
                        activities.add(c);
                    }
                }
                // Creating nested arraylist and setting up recyclerviews
                partitions = reusable.createSubList((activities), (int) divideBy);
                setUpRecyclerView();
                reusable.setUpButton(getContext(), daysRecyclerView);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // Setting title and showing toolbar when fragment resumed
    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).show();
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle("Itinerary for " + mytrips + " trip");

    }


}