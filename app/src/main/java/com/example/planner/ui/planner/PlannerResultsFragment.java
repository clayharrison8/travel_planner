package com.example.planner.ui.planner;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planner.MainActivity;
import com.example.planner.PlannerClickListener;
import com.example.planner.R;
import com.example.planner.Reusable;
import com.example.planner.algorithms.travellingsalesmen.GA;
import com.example.planner.algorithms.travellingsalesmen.Population;
import com.example.planner.algorithms.travellingsalesmen.TourManager;
import com.example.planner.api.RetrofitClient;
import com.example.planner.api.TriposoAPI;
import com.example.planner.databinding.FragmentPlannerResultsBinding;
import com.example.planner.model.POI;
import com.example.planner.model.InitialResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class PlannerResultsFragment extends Fragment implements PlannerClickListener {

    private PlannerAdapter plannerAdapter = new PlannerAdapter();
    private FragmentPlannerResultsBinding fragmentPlannerResultsBinding;
    private ArrayList<POI> city = new ArrayList<>();
    private View view;
    private String items_input, city_input, start_date, end_date;
    private List<String> datesSaved = new ArrayList<>();
    private List<List<POI>> partitions = new ArrayList<>();
    private DatabaseReference databaseReference;
    private ArrayList<POI> shortened = new ArrayList<>();
    private Reusable reusable = new Reusable();
    private RecyclerView daysRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Setting up view
        fragmentPlannerResultsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_planner_results, container, false);

        view = inflater.inflate(R.layout.fragment_planner_results, container, false);
        daysRecyclerView = fragmentPlannerResultsBinding.plannerDay;

        getData();

        // Getting current user
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        assert user != null;
        String userID = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(userID).child("My Trips");

        fragmentPlannerResultsBinding.setItemClickListener(this);

        sortList();
        setUpRecyclerView();

        // Inflate the layout for this fragment
        return fragmentPlannerResultsBinding.getRoot();
    }

    // Getting data about location selected for planner
    private void getPlannerLocation() {
        final TriposoAPI userDataService = RetrofitClient.getService();
        Call<InitialResponse> call = userDataService.getCity(city_input);
        call.enqueue(new Callback<InitialResponse>() {
            @Override
            public void onResponse(@NotNull Call<InitialResponse> call, @NotNull Response<InitialResponse> response) {
                InitialResponse cityResponse = response.body();
                if (cityResponse != null && cityResponse.getResults() != null) {
                    city = (ArrayList<POI>) cityResponse.getResults();
                }
            }
            @Override
            public void onFailure(@NotNull Call<InitialResponse> call, @NotNull Throwable t) {
            }
        });
    }


    // Getting data from SharedPreferences
    private void getData() {
        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("myKey", MODE_PRIVATE);
        items_input = sharedPreferences.getString("items input", "");
        city_input = sharedPreferences.getString("city input", "");
        start_date = sharedPreferences.getString("start date", "");
        end_date = sharedPreferences.getString("end date", "");

    }

    private void setUpRecyclerView() {
        // bind RecyclerView
        RecyclerView recyclerView = fragmentPlannerResultsBinding.viewCities;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Create an instance of the adapter and set on RecyclerView
        recyclerView.setAdapter(plannerAdapter);
    }

    private void hello2(ArrayList<POI> arrayList) {
        double totalDistance = 0;
        for (int i = 0; i < arrayList.size(); i++) {
//            System.out.println("Index " + i);
                if (i < arrayList.size() - 1) {
                    double distance = reusable.calculateDistance(arrayList.get(i).getCoordinates().getLat(), arrayList.get(i + 1).getCoordinates().getLat(), arrayList.get(i).getCoordinates().getLng(), arrayList.get(i + 1).getCoordinates().getLng());
                    System.out.println("Comparing " + arrayList.get(i).getName() + " to " + arrayList.get(i+1).getName());
                    reusable.calculateTime(distance);
                    totalDistance += distance;
                }

            }
        System.out.println("Total Miles: " + totalDistance);

        }

    private ArrayList<POI> touring(ArrayList<POI> arrayList){
        TourManager.clearDestinationsList();
        // Create and add the activityList
        TourManager.setDestinationsList(arrayList);

        // Initialize population
        Population pop = new Population(50, true);
        pop.getFittest().getDistance();

        // Evolve population for 100 generations
        pop = GA.evolvePopulation(pop);
        for (int i = 0; i < 100; i++) {
            pop = GA.evolvePopulation(pop);
        }

        // Print final results
        pop.getFittest().getDistance();
        shortened = pop.getList();

        return pop.getList();
    }

    // Saving plan to database
    public void savePlan(){
        MainActivity.displayToast("Saved to My Trips", getContext());
        System.out.println("Dates = " + MainActivity.getDates());
//        System.out.println("Start Date: " + start_date);
//        System.out.println("End Date: " + end_date);

        String uniqueID = Uri.encode(start_date + "-" + end_date);
        datesSaved = MainActivity.getDates();
        city.get(0).setUniqueID(uniqueID);
        databaseReference.child(city_input).child(uniqueID).child("dates").setValue(datesSaved);
        databaseReference.child(city_input).child(uniqueID).child("location").setValue(city);
        databaseReference.child(city_input).child(uniqueID).child("days").setValue(partitions);
    }

    private void sortList(){
        ArrayList<POI> activity = MainActivity.getActivityList();
        getPlannerLocation();
        partitions = reusable.createSubList(touring(activity), Integer.parseInt(items_input));
        reusable.setUpButton(getContext(), daysRecyclerView);
//        System.out.println("Cities");
//        hello2(activity);
//        System.out.println("Shortened");
//        hello2(shortened);
//        System.out.println("Partitions = " + partitions);

        plannerAdapter.setCityList(partitions);

    }

    // Showing toolbar and setting title when fragment displayed
    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).show();
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle("Itinerary for " + city_input + " trip");

    }


}