package com.example.planner.ui.attractions;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
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
import com.example.planner.databinding.FragmentAttractionsBinding;
import com.example.planner.model.POI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class AttractionsFragment extends Fragment {

    private POIAdapter poiAdapter = new POIAdapter();
    private JSONRequests jsonRequests = new JSONRequests();
    private String sightseeing, bookable;
    private SharedPreferences sharedPreferences;
    private POI poi;
    private Reusable reusable = new Reusable();

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Setting up the view
        FragmentAttractionsBinding fragmentAttractionsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_attractions, container, false);
        // Getting SharedPreferences
        sharedPreferences = container.getContext().getSharedPreferences("myKey", MODE_PRIVATE);
        poi = MainActivity.getPOI();

        setData();

        getDatabase();

        reusable.setUpPOIRecyclerView(getContext(), poiAdapter, fragmentAttractionsBinding.viewCities);

        // Inflate the layout for this fragment
        return fragmentAttractionsBinding.getRoot();
    }

    // Putting data into SharedPreferences
    private void setData(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("button", "attractions");
        editor.apply();
    }

    // Retrieving sightseeing and bookable score from database
    private void getDatabase(){
        // Checking there is current user and getting 'Settings' node
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String userID = user.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(userID).child("Settings");

        // Retrieving data from database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    sightseeing = Objects.requireNonNull(dataSnapshot.child("Sightseeing Score").child("value").getValue()).toString();
                    bookable = Objects.requireNonNull(dataSnapshot.child("Bookable").getValue()).toString();
                    jsonRequests.getAttractions(poi, poiAdapter, bookable, sightseeing);
                }
                catch (NullPointerException e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    // Showing toolbar back and setting title when fragment started
    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).show();
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle("Attractions in " + poi.getName());


    }

    // Hiding toolbar back when fragment stopped
    @Override
    public void onStop() {
        super.onStop();
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).hide();
    }


}
