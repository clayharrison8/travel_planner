package com.example.planner.ui.planner;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.planner.MainActivity;
import com.example.planner.R;
import com.example.planner.Reusable;
import com.example.planner.api.RetrofitClient;
import com.example.planner.api.TriposoAPI;
import com.example.planner.model.InitialResponse;
import com.example.planner.model.POI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class PlannerFragment extends Fragment {

    private EditText items, maximum_price;
    private Button getResults;
    private View view;
    private String items_input, city_input, maximum_price_input, clicked, bookable_input;
    private TextView start_date, end_date, clickedText;
    private Calendar calendar = Calendar.getInstance();
    private Date startDate, endDate, today;
    private long timeDifference, days;
    private Spinner spinner, spinner2;
    private DatabaseReference databaseReference, databaseReference2;
    private List<String> dates = new ArrayList<>();
    List<String> dates2 = new ArrayList<>();
    List<String> compareDates = new ArrayList<>();
    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
    private boolean contains;


    // For Planner Results
    private ArrayList<POI> activities = new ArrayList<>();
    private Reusable reusable = new Reusable();
    private double maximumPrice;
    private int count;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_planner, container, false);

        items = view.findViewById(R.id.itemsPerDay);
        getResults = view.findViewById(R.id.results);
        start_date = view.findViewById(R.id.start_date);
        end_date = view.findViewById(R.id.end_date);
        spinner = view.findViewById(R.id.persona_choice);
        spinner2 = view.findViewById(R.id.cityChose);
        maximum_price = view.findViewById(R.id.maximum_price);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Planner List");

        getUserSettings();


        start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedText = (TextView) v;
                // Checking what was clicked
                clicked = "Start Date";
                showDatePickerDialog();
            }
        });

        end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedText = (TextView) v;
                // Checking what was clicked
                clicked = "End Date";
                showDatePickerDialog();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void getPlannerResults() {
        getResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Converting fields into string
                items_input = items.getText().toString().trim();
                city_input = spinner2.getSelectedItem().toString();
                maximum_price_input = maximum_price.getText().toString().trim();

                // Checking if item input not empty
                if (items_input.length() == 0) {
                    // Displaying error message to user
                    items.setError("Cannot be null");
                    items.requestFocus();
                }

                // Checking start date is not null (Start Date is default value)
                if (start_date.getText().toString().equals("Start Date")) {
                    // Displaying error message to user
                    start_date.setError("Cannot be null");
                    start_date.requestFocus();
                }

                // Checking end date is not null (End Date is default value)
                if (end_date.getText().toString().equals("End Date")) {
                    // Displaying error message to user
                    end_date.setError("Cannot be null");
                    end_date.requestFocus();
                }

                if (items_input.startsWith("0")) {
                    Toast.makeText(getContext(), "Invalid number", Toast.LENGTH_SHORT).show();
                }

                else if (contains){
                    MainActivity.displayToast("Dates already booked, please select dates again", getContext());
                }
                // Checking values aren't empty
                else if (items_input.length() != 0 && !end_date.getText().toString().equals("End Date") && !start_date.getText().toString().equals("Start Date")) {
                    sendData();

                    System.out.println("Dates saved = " + dates);
                    MainActivity.saveDates(dates);
                    System.out.println("Returned dates = " + MainActivity.getDates());

                    // Going to results page
                    getPlannerActivities();
                }

            }
        });
    }

    // Saving data via SharedPreferences
    private void sendData() {
        SharedPreferences sharedPref = view.getContext().getSharedPreferences("myKey", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString("items input", items_input);
        editor.putString("city input", city_input);
        editor.putString("start date", start_date.getText().toString());
        editor.putString("end date", end_date.getText().toString());

        editor.apply();
    }

    // Showing calendar for user to pick date from
    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    // Retrieving day, month and year from date selected on calendar
    private final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            update();
        }
    };

    private void update() {
        // Optional date format
        String myFormat = "dd/MM/yyyy";
        // Formats date with string and language of given locale
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
        // Setting start/end date to date selected from calendar
        clickedText.setText(sdf.format(calendar.getTime()));

        // Setting start/end date Date field to date selected
        if (clicked.equals("Start Date")) {
            startDate = calendar.getTime();

        } else {
            endDate = calendar.getTime();
        }

        // Validation - Checking they're both not null and end date doesn't precede start date
        if (startDate != null && endDate != null) {
            if (endDate.before(startDate)) {
                // Displaying error message to user
                end_date.setError("End Date cannot be before Start Date");
                end_date.requestFocus();
            } else {
                // Calculating number of days between the two dates
                timeDifference = endDate.getTime() - startDate.getTime();
                days = TimeUnit.DAYS.convert(timeDifference, TimeUnit.MILLISECONDS) + 1;

                getDates(start_date.getText().toString(), end_date.getText().toString());
                checkDates();

//                System.out.println("C D = " + compareDates);
//                System.out.println("D2 = " + dates2);
//                for(int i = 0; i < compareDates.size(); i++){
//                    if (dates2.contains(compareDates.get(i))){
//                        System.out.println("true");
//                    }
//                }
            }
        }
    }

    private void setUpInterestDropdown() {
        // Creating a list of items for the dropdown.
        String[] items = new String[]{"Food Lover", "Culture Buff", "Nature", "Party", "Shopper", "Budget", "Mid Range", "Splurge", "Family", "Couples"};
        // Creating an adapter to describe how the items are displayed
        ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_dropdown_item, items);
        // Set the spinners adapter to the previously created one.
        spinner.setAdapter(adapter);
    }

    private void setUpCityDropdown() {
        // Creating a list of items for the dropdown.
        final ArrayList<String> items = new ArrayList<>();
        // Retrieving City List from database
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    // Looping through database items and adding to arraylist
                    POI c = ds.getValue(POI.class);
                    assert c != null;
                    items.add(c.getId());
                    // Creating an adapter to describe how the items are displayed
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_dropdown_item, items);
                    // Set the spinners adapter to the previously created one.
                    spinner2.setAdapter(adapter);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    private void getUserSettings() {
        // Getting current user and Settings node
        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        String userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        databaseReference = databaseReference.child("users").child(userID).child("Settings");

        // Getting data from database
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookable_input = Objects.requireNonNull(dataSnapshot.child("Bookable").getValue()).toString();
                // Checking bookable input from database to determine price field should be displayed
                if (bookable_input.equals("0")) {
                    maximum_price.setVisibility(View.GONE);
                }
                if (bookable_input.equals("1")) {
                    maximum_price.setVisibility(View.VISIBLE);
                }

                // Getting today's date
                today = calendar.getTime();

                setUpInterestDropdown();
                setUpCityDropdown();

                getPlannerResults();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void getPlannerActivities() {
        // Checking is price field is empty
        if (!maximum_price_input.equals("")) {
            maximumPrice = Double.parseDouble(maximum_price_input);
            count = Integer.parseInt(items_input) * Integer.parseInt(Long.toString(days)) * 2;
        } else {
            count = Integer.parseInt(items_input) * Integer.parseInt(Long.toString(days));
        }

        final TriposoAPI userDataService = RetrofitClient.getService();

        Call<InitialResponse> call = userDataService.getPlannerResults(city_input, bookable_input, "persona:" + spinner.getSelectedItem().toString().toLowerCase().replace(' ', '_'), MainActivity.getInterests(), count);
        call.enqueue(new Callback<InitialResponse>() {
            @Override
            public void onResponse(Call<InitialResponse> call, Response<InitialResponse> response) {
                InitialResponse cityResponse = response.body();
                if (cityResponse != null && cityResponse.getResults() != null) {
                    activities = (ArrayList<POI>) cityResponse.getResults();
//                    System.out.println("Activities = " + activities);

                    // Checking is price field is empty
                    if (!maximum_price_input.equals("")) {
                        activities = reusable.filterByPrice(activities, count, maximumPrice);
                    }

                    if (activities.size() == 0) {
                        Toast.makeText(getContext(), "Please increase price or items", Toast.LENGTH_SHORT).show();
                    }
                    // Checking if number of activities retrieved is correct
                    else if (!maximum_price_input.equals("") && (activities.size() != count / 2)) {
                        Toast.makeText(getContext(), "Please Increase Price", Toast.LENGTH_SHORT).show();
                    }
                    // Checking if number of activities retrieved is correct
                    else if (maximum_price_input.equals("") && (activities.size() != count)) {
                        Toast.makeText(getContext(), "Please Increase Price", Toast.LENGTH_SHORT).show();
                    }
                    // If successful, save activities and go to planner results page
                    else {
                        Toast.makeText(getContext(), "Price Success", Toast.LENGTH_SHORT).show();
                        MainActivity.saveActivityList(activities);

                        FragmentTransaction transaction = reusable.transactions(view);
                        transaction.add(R.id.frag_container, new PlannerResultsFragment()).addToBackStack(null).commit();

                    }

                } else {
                    Toast.makeText(getContext(), "Result Failed", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<InitialResponse> call, Throwable t) {
                System.out.println("Failure");
            }
        });

    }

    private void getDates(String start_date, String end_date) {
        compareDates.clear();
        dates2.clear();
        dates.clear();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date startDate = formatter.parse(start_date);
            Date endDate = formatter.parse(end_date);
            long interval = 24 * 1000 * 60 * 60; // 1 hour in millis
            long endTime = endDate.getTime(); // create your endtime here, possibly using Calendar or Date
            long startTime = startDate.getTime();
            while (startTime <= endTime) {
                dates.add(Uri.encode(new Date(startTime).toString()));
                compareDates.add(formatter.format(dateFormat.parse(new Date(startTime).toString())));
                startTime += interval;
            }

//            System.out.println(compareDates);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkDates() {
        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        String userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        databaseReference = databaseReference.child("users").child(userID).child("My Trips");


        // Getting data from database
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot children : dataSnapshot.getChildren()) {
                    for (DataSnapshot children1 : children.getChildren()) {
                        for (DataSnapshot children2 : children1.child("dates").getChildren()) {
                            try {
                                String date = formatter.format(dateFormat.parse(Uri.decode(Objects.requireNonNull(children2.getValue()).toString())));
                                dates2.add(date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }

                    }


                }
//
                System.out.println("C D = " + compareDates);
                System.out.println("D2 = " + dates2);
                contains = false;

                for (int i = 0; i < compareDates.size(); i++) {
                    if (dates2.contains(compareDates.get(i))) {
                        contains = true;
                    }
                }
                System.out.println(contains);


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
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle("Planner");
    }


}
