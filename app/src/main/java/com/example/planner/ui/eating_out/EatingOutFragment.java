package com.example.planner.ui.eating_out;

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
import com.example.planner.databinding.FragmentEatingOutBinding;
import com.example.planner.model.POI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class EatingOutFragment extends Fragment {

    private POIAdapter poiAdapter = new POIAdapter();
    private FragmentEatingOutBinding eatingOutBinding;
    private String eatingout;
    private SharedPreferences sharedPreferences;
    private POI poi;
    private JSONRequests jsonRequests = new JSONRequests();
    private Reusable reusable = new Reusable();

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Setting up view
        eatingOutBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_eating_out,container,false);

        poi = MainActivity.getPOI();

        // Calling SharedPreferences
        sharedPreferences = container.getContext().getSharedPreferences("myKey", MODE_PRIVATE);

        setData();

        getDatabase();

        reusable.setUpPOIRecyclerView(getContext(), poiAdapter, eatingOutBinding.viewCities);

        jsonRequests.getRestaurants(poi,poiAdapter,eatingout);

        // Inflate the layout for this fragment
        return eatingOutBinding.getRoot();

    }

    // Saving data to SharedPreferences
    private void setData(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("button", "eating out");
        editor.apply();
    }

    private void getDatabase(){
        // Getting current user and retrieving 'Settings' node
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String userID = user.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(userID).child("Settings");

        // Retrieving eat and drink score from database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eatingout = Objects.requireNonNull(dataSnapshot.child("Eat and Drink Score").child("value").getValue()).toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // Showing toolbar when fragment started
    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).show();
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle("Eating Out in " + poi.getName());

    }

    // Hiding toolbar when fragment stopped
    @Override
    public void onStop() {
        super.onStop();
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).hide();
    }

}
