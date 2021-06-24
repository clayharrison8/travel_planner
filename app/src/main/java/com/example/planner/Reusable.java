package com.example.planner;

import android.content.Context;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planner.model.POI;
import com.example.planner.ui.DaysAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Reusable {
    private List<List<POI>> partitions = new ArrayList<>();

    // Splitting arraylist into a nested arraylist
    public List<List<POI>> createSubList(ArrayList<POI> cities, int dividedBy) {
        partitions.clear();
        // Looping through list and adding n cities to position i
        for (int i = 0; i < cities.size(); i += dividedBy) {
            partitions.add(cities.subList(i, Math.min(i + dividedBy, cities.size())));
        }

        return partitions;
    }

    // Based on code from GeekforGeeks.org. Available at: https://www.geeksforgeeks.org/haversine-formula-to-find-distance-between-two-points-on-a-sphere/
    public double calculateDistance(double lat1, double lat2, double lon1, double lon2) {
        // The math module contains a function named toRadians which converts from degrees to radians.
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers. Use 3956 for miles
        double r = 3963;

        // calculate the result
        return (c * r);
    }

    // Function to convert hours into minutes and seconds
    public String calculateTime(double distance) {
        double hours = distance / 3;
        double minutes = hours * 60;
        int minutes2 = (int) minutes;
//        System.out.println("Distance: " + formatter.format(distance) + " Miles");
        return minutes2 + " minutes by walking";
    }

    // Creating days list for planner
    public void setUpButton(Context context, RecyclerView recyclerView) {
        ArrayList<Integer> days = new ArrayList<>();

        days.clear();
        // Creating days list according to size of partitions
        for (int i = 0; i < partitions.size(); i++) {
            days.add(i);
        }

        // Setting up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        DaysAdapter adapter = new DaysAdapter(days);
        recyclerView.setAdapter(adapter);
    }

    // Filtering arraylist by price
    public ArrayList<POI> filterByPrice(ArrayList<POI> activitiesList, int count, double maximumPrice) {
        ArrayList<POI> updatedList = new ArrayList<>();
        double totalPrice = 0;

        // Checking if enough activities for algorithm to work with
        if (activitiesList.size() < count) {
//            Toast.makeText(view.getContext(), "Not enough activities", Toast.LENGTH_SHORT).show();
            System.out.println("Not enough activities");

            // Going back to planner page
//            ((FragmentActivity) view.getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new PlannerFragment()).commit();
        } else {
            count /= 2;
            // Step 1 - Initial adding of items
            for (int i = 0; i < activitiesList.size(); i++) {
                double price = Double.parseDouble(activitiesList.get(i).getBookingInfo().getPrice().getAmount());

                // Add item price and check if limit is exceeded
                totalPrice += price;
                if (totalPrice <= maximumPrice && updatedList.size() != count) {
                    // Adding most popular activities first, gives activities a chance to be in list
                    updatedList.add(activitiesList.get(i));

                } else {
                    // If price succeeded remove price to ensure totalPrice is below limit
                    totalPrice -= price;
                }

            }

            // Step 2 - Adding lowest and removing highest until 2 conditions are satisfied
            // Removing all items from List A that occur in List B
            activitiesList.removeAll(updatedList);

            // Sorting both lists in regards to price
            Collections.sort(updatedList);
            Collections.sort(activitiesList);

            System.out.println("Updated List size: " + updatedList.size());
            System.out.println("Count: " + count);
            System.out.println(updatedList.size() != count);
            System.out.println("Price: " + totalPrice);
            System.out.println("City Copy");

            for (int i = 0; i < activitiesList.size(); i++) {
                System.out.println(activitiesList.get(i).getBookingInfo().getPrice().getAmount());
            }
            loopUpdatedList(updatedList);

            // Constraints that need to be met
            while (updatedList.size() != count && totalPrice <= maximumPrice) {
                // Getting price for first object in cityCopy and last object in updatedList
                double cityCopyPrice = Double.parseDouble(activitiesList.get(0).getBookingInfo().getPrice().getAmount());
                double updatedListPrice = Double.parseDouble(updatedList.get(updatedList.size() - 1).getBookingInfo().getPrice().getAmount());

                totalPrice += cityCopyPrice;
                System.out.println("Price added: " + cityCopyPrice);
                System.out.println("Price: " + totalPrice);
                // Adding first element of cityCopy to updatedList and then removing it
                updatedList.add(activitiesList.get(0));
                activitiesList.remove(0);
                // Sorting updatedList by price
                Collections.sort(updatedList);


                // Checking if constraint isn't met
                if (totalPrice > maximumPrice) {
                    Collections.sort(updatedList);
                    // Removing last item from updatedList since it is the largest
                    updatedList.remove(updatedList.size() - 1);
                    // Updating totalPrice as last item has been removed
                    totalPrice -= updatedListPrice;
                    System.out.println("After removing, price is: " + totalPrice);
                    System.out.println("Updated List Size after removing " + updatedList.size());


                }

                System.out.println("City Copy");

                for (int a = 0; a < activitiesList.size(); a++) {
                    System.out.println(activitiesList.get(a).getBookingInfo().getPrice().getAmount());
                }
                loopUpdatedList(updatedList);
            }

        }

        if (updatedList.size() != count) {

            System.out.println("Please increase price");

        }


        return updatedList;

    }

    private void loopUpdatedList(ArrayList<POI> arrayList) {
        System.out.println("New List");
        for (int i = 0; i < arrayList.size(); i++) {
            System.out.println(arrayList.get(i).getBookingInfo().getPrice().getAmount());
        }
    }

    // Looping through fragments and hiding them
    public FragmentTransaction transactions(View view){
        FragmentTransaction transaction = ((FragmentActivity) view.getContext()).getSupportFragmentManager().beginTransaction();
        List<Fragment> fragments = ((FragmentActivity) view.getContext()).getSupportFragmentManager().getFragments();
        if (!fragments.isEmpty()) {
            for (Fragment fragment : fragments) {
                if (!fragment.isHidden()) {
                    transaction.hide(fragment);
                }
            }
        }
        return transaction;
    }


    public void setUpPOIRecyclerView(Context context, POIAdapter poiAdapter, RecyclerView recyclerView){
        // bind RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        // Create an instance of the adapter and set on RecyclerView
        recyclerView.setAdapter(poiAdapter);
    }
}
