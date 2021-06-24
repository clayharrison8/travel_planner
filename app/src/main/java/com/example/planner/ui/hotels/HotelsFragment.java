package com.example.planner.ui.hotels;

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
import com.example.planner.api.RetrofitClient;
import com.example.planner.api.TriposoAPI;
import com.example.planner.databinding.FragmentHotelsBinding;
import com.example.planner.model.POI;
import com.example.planner.model.InitialResponse;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class HotelsFragment extends Fragment {


    private POIAdapter poiAdapter = new POIAdapter();
    private String hotels;
    private POI poi;
    private SharedPreferences sharedPreferences;
    private JSONRequests jsonRequests = new JSONRequests();
    private Reusable reusable = new Reusable();

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FragmentHotelsBinding fragmentHotelsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_hotels, container, false);

        sharedPreferences = container.getContext().getSharedPreferences("myKey", MODE_PRIVATE);
        poi = MainActivity.getPOI();

        setData();

        getDatabase();

        reusable.setUpPOIRecyclerView(getContext(), poiAdapter, fragmentHotelsBinding.viewCities);

        jsonRequests.getHotels(poi, hotels, poiAdapter);

        // Inflate the layout for this fragment
        return fragmentHotelsBinding.getRoot();

    }

    // Saving data to SharedPreferences
    private void setData(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("button", "hotels");
        editor.apply();
    }

    private void getDatabase(){
        // Get current user and Settings node
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String userID = user.getUid();
        databaseReference = databaseReference.child("users").child(userID).child("Settings");

        // Getting hotel score from database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hotels = dataSnapshot.child("Hotel Score").child("value").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // Showing toolbar and setting title when fragment resumes
    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).show();
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle("Hotels in " + poi.getName());

    }

    // Hiding toolbar when fragment stops
    @Override
    public void onStop() {
        super.onStop();
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).hide();
    }
}
