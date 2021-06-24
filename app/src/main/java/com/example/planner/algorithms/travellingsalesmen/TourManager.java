package com.example.planner.algorithms.travellingsalesmen;

import com.example.planner.model.POI;

import java.util.ArrayList;

// Based on genetic algorithm TSP tutorial by TheProjectSpot.com. Available at: http://www.theprojectspot.com/tutorial-post/applying-a-genetic-algorithm-to-the-travelling-salesman-problem/5
public class TourManager {

    // Holds our cities
    private static ArrayList<POI> destinations = new ArrayList<>();

    // Get a city
    static POI getDestination(int index){
        return destinations.get(index);
    }

    // Get the number of destination cities
    static int numberOfDestinations(){
        return destinations.size();
    }

    // Setting initial list of destinations
    public static void setDestinationsList(ArrayList<POI> destinations1){
        destinations = destinations1;
    }

    // Get the number of destination cities
    public static void clearDestinationsList(){
        destinations.clear();
    }
}