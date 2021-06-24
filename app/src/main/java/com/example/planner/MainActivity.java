package com.example.planner;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.planner.databinding.ActivityMainBinding;
import com.example.planner.model.POI;
import com.example.planner.model.StructuredContentSection;
import com.example.planner.ui.CatalogueFragment;
import com.example.planner.ui.CurrencyConverterFragment;
import com.example.planner.ui.UtilityFragment;
import com.example.planner.ui.favourites.FavouritesFragment;
import com.example.planner.ui.mytrips.MyTripsFragment;
import com.example.planner.ui.planner.PlannerFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    TextView username;
    protected ActivityMainBinding activityMainBinding;
    private static POI POI;
    private static ArrayList<POI> activityList;
    private static String interests;
    private static List<String> dates1 = new ArrayList<>();


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    SearchAdapter adapter;
    ArrayList<POI> countries;
    androidx.appcompat.widget.SearchView searchView;
    private DatabaseReference databaseReference;

    MenuItem searchItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Makes the toolbar visible
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Getting current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        username = findViewById(R.id.nav_header_text);
        drawerLayout = findViewById(R.id.drawer);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Calling the onNavigationItemSelected method
        navigationView.setNavigationItemSelectedListener(this);

        // Creating constructor for ActionBarToggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.side_navbar_open, R.string.side_navbar_close);

        // Makes the rotating navbar icon available
        toggle.syncState();

        // Sets text in navbar header to current user's email
        View headerView = navigationView.getHeaderView(0);
        assert user != null;
        String email = user.getEmail();
        TextView navUsername = headerView.findViewById(R.id.nav_header_text);
        navUsername.setText(email);

        getDatabase();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Searchable List");

        // Setting up recyclerview
        recyclerView = findViewById(R.id.favourites_recycler);
        countries = new ArrayList<>();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        addSearchList();

        // Makes search list disappear on load
        recyclerView.setVisibility(View.GONE);

        // Ensures that the fragment is not added more than once
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.frag_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.home);
        }

    }


    // Method for switching between fragments
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Hiding all current fragments in fragment list
        FragmentTransaction transaction = f;
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (!fragments.isEmpty()) {
            for (Fragment fragment : fragments) {
                if (!fragment.isHidden()) {
                    transaction.hide(fragment);
                }
            }
        }

        switch (menuItem.getItemId()) {

            case R.id.catalogue:
                transaction.add(R.id.frag_container, new CatalogueFragment()).addToBackStack(null).commit();
                break;
            case R.id.mytrips:
                transaction.add(R.id.frag_container, new MyTripsFragment()).addToBackStack(null).commit();
                break;
            case R.id.favourites:
                transaction.add(R.id.frag_container, new FavouritesFragment()).addToBackStack(null).commit();
                break;
            case R.id.settings:
                transaction.add(R.id.frag_container, new Setting()).addToBackStack(null).commit();
                break;
            case R.id.home:
                transaction.add(R.id.frag_container, new HomeFragment()).addToBackStack(null).commit();
                break;
            case R.id.planner:
                transaction.add(R.id.frag_container, new PlannerFragment()).addToBackStack(null).commit();
                break;
            case R.id.converter:
                transaction.add(R.id.frag_container, new CurrencyConverterFragment()).addToBackStack(null).commit();
                break;
            case R.id.sign_out:
                logout();
                break;

        }
        // Will close the navbar after an item is clicked
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    // Adding view
    @Override
    public void setContentView(View view) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        activityMainBinding.fragContainer.addView(view, lp);
    }

    // Logging out
    private void logout() {
        // This will display a pop up message confirming the user wants to log out
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Log Out")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                        // Calling method to sign out
                        firebaseAuth.signOut();
                        // Switching to the login page
                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        // Makes sure that the user cannot go back to previous page
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                })
                .setNegativeButton("No", null)
                .show();

    }


    // Retrieving all items in Country list in database
    public void addSearchList() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Adding all countries to arraylist
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    POI c = ds.getValue(POI.class);
                    countries.add(c);

                }
                // Setting up adapter
                adapter = new SearchAdapter(countries, MainActivity.this);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Expanding search bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        searchItem = menu.findItem(R.id.action_Search);
        searchView = (androidx.appcompat.widget.SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            // Not displaying list until text entered
            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 0){
                    recyclerView.setVisibility(View.VISIBLE);
                    adapter.getFilter().filter(newText);
                }
                else {
                    recyclerView.setVisibility(View.GONE);
                }
                return false;            }
        });

        return true;
    }


    public void resetSearch() {
        // Making RecyclerView disappear
        recyclerView.setVisibility(View.GONE);
        // Deleting search string
        searchView.setQuery("", false);
        // Removing keyboard
        searchView.clearFocus();
        // Shrinking searchview
//        System.out.println(searchItem.isic());

        searchItem.collapseActionView();
    }

    // Open location in google maps using latitude, longitude and name
    public static void showInMap(Double lat, Double lon, String name, Context context) {
        Uri gmmIntentUri = Uri.parse("geo:" + lat + "," + lon + "?q=" + name);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        context.startActivity(mapIntent);
    }

    // Setting up content adapter
    public static void setUpContentRecyclerView(RecyclerView recyclerView, Context context, RecyclerView.Adapter adapter, RecyclerView.LayoutManager layoutManager, ArrayList<StructuredContentSection> structuredContentSections) {
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);
        adapter = new ContentAdapter(structuredContentSections);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public static void savePOI(POI POI1) {
        POI = POI1;
    }

    public static POI getPOI() {
        return POI;
    }

    public static void saveActivityList(ArrayList<POI> activityList1) { activityList = activityList1; }

    public static ArrayList<POI> getActivityList() {
        return activityList;
    }

    public static void saveInterests(String interests1) {
        interests = interests1;
    }

    public static String getInterests() {
        return interests;
    }

    public static void saveDates(List<String> dates){
        dates1 = dates; }

    public static List<String> getDates() {
        return dates1;
    }

    private void getDatabase() {
        // Getting current user and Interests node
        String userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(userID).child("Settings").child("Interests");

        // Retrieving interests string
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                MainActivity.saveInterests(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // Pop fragment stack when click back button
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
            getSupportFragmentManager().popBackStack();
        else
            super.onBackPressed();
    }

    // Displaying message to the user
    public static void displayToast(String string, Context context){
        Toast.makeText(context,string,Toast.LENGTH_SHORT).show();
    }


}



