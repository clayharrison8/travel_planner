package com.example.planner.ui.nightlife;

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
import com.example.planner.databinding.FragmentNightlifesBinding;
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

public class NightlifesFragment extends Fragment {

    private POIAdapter poiAdapter = new POIAdapter();
    private FragmentNightlifesBinding fragmentNightlifesBinding;
    private String nightlife;
    private SharedPreferences sharedPreferences;
    private POI poi;
    private JSONRequests jsonRequests = new JSONRequests();
    private Reusable reusable = new Reusable();

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentNightlifesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_nightlifes,container,false);

        poi = MainActivity.getPOI();

        sharedPreferences = container.getContext().getSharedPreferences("myKey", MODE_PRIVATE);

        setData();

        getDatabase();

        reusable.setUpPOIRecyclerView(getContext(), poiAdapter, fragmentNightlifesBinding.viewCities);

        jsonRequests.getNightlife(poi,poiAdapter, nightlife);

        // Inflate the layout for this fragment
        return fragmentNightlifesBinding.getRoot();

    }


    // Saving data to SharedPreferences
    private void setData(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("button", "nightlife");
        editor.apply();
    }

    private void getDatabase(){
        // Getting current user and Settings node
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String userID = user.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(userID).child("Settings");

        // Called anytime data changes or when activity starts
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nightlife = Objects.requireNonNull(dataSnapshot.child("Nightlife Score").child("value").getValue()).toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    // Showing toolbar and setting toolbar when fragment resumes
    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).show();
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle("Nightlife in " + poi.getName());

    }

    // Hiding toolbar when fragments stops
    @Override
    public void onStop() {
        super.onStop();
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).hide();
    }
}
