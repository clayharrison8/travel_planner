package com.example.planner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planner.adapter.ImageBindingAdapter;
import com.example.planner.model.POI;
import com.example.planner.ui.attractions.AttractionsFragment;
import com.example.planner.ui.cities.CitiesFragment;
import com.example.planner.ui.regions.RegionsFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.Random;

public class CountryFragment extends Fragment {


    private Button country_button, attractions, regions;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private ImageView add_or_remove;
    private POI POI;
    private DatabaseReference databaseReference;
    private boolean exists;
    private Random rand = new Random();
    private Reusable reusable = new Reusable();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_country, container, false);

        country_button = view.findViewById(R.id.country_button);
        attractions = view.findViewById(R.id.top_attractions);
        TextView country_name = view.findViewById(R.id.name);
        TextView country_snippet = view.findViewById(R.id.country_snippet);
        regions = view.findViewById(R.id.regions);
        ImageView imageView = view.findViewById(R.id.images);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        add_or_remove = view.findViewById(R.id.add_or_remove);
        POI = MainActivity.getPOI();


        // Get current user and get Favourites node
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String userID = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(userID).child("Favourites");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Check if country exists in Favourites node
                if(dataSnapshot.exists() && dataSnapshot.child(POI.getId()).exists()){
                    // Set image to remove button
                    add_or_remove.setBackgroundResource(R.drawable.remove_button);
                    exists = true;
                }
                else {
                    // Set image to add button
                    add_or_remove.setBackgroundResource(R.drawable.add_button);
                    exists = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        add_or_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If country exists, remove it.
                if(exists){
                    databaseReference.child(POI.getId()).getRef().removeValue();
                    add_or_remove.setBackgroundResource(R.drawable.add_button);
                    exists = false;
                    MainActivity.displayToast("Country removed from Favourites", view.getContext());

                }
                // If country dosen't exist, add it.
                else  {
                    databaseReference.child(POI.getId()).setValue(POI);
                    add_or_remove.setBackgroundResource(R.drawable.remove_button);
                    exists = true;
                    MainActivity.displayToast("Country added to Favourites", view.getContext());

                }

            }
        });


        onClickListener();

        MainActivity.setUpContentRecyclerView(recyclerView, getContext(), adapter,layoutManager, POI.getStructured_content().getSections());

        country_name.setText(POI.getName());
        country_snippet.setText(POI.getSnippet());
        ImageBindingAdapter.bindThumbnail(imageView, POI.getImages().get(rand.nextInt(POI.getImages().size())).getSizes().getMedium().getUrl());

        // Inflate the layout for this fragment
        return view;
    }


    private void onClickListener(){
        country_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = reusable.transactions(v);
                transaction.add(R.id.frag_container, new CitiesFragment()).addToBackStack(null).commit();
            }
        });

        attractions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = reusable.transactions(v);
                transaction.add(R.id.frag_container, new AttractionsFragment()).addToBackStack(null).commit();            }
        });

        regions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = reusable.transactions(v);
                transaction.add(R.id.frag_container, new RegionsFragment()).addToBackStack(null).commit();            }
        });
    }

    // Hide toolbar when fragment starts
    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).hide();
    }

    // Show toolbar when fragment stops
    @Override
    public void onStop() {
        super.onStop();
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).show();
    }

}
