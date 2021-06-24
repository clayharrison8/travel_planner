package com.example.planner.algorithms.travellingsalesmen;

import com.example.planner.Reusable;
import com.example.planner.model.POI;

import java.util.ArrayList;
import java.util.Collections;

// Based on genetic algorithm TSP tutorial by TheProjectSpot.com. Available at: http://www.theprojectspot.com/tutorial-post/applying-a-genetic-algorithm-to-the-travelling-salesman-problem/5
public class Tour {


    // Holds our tour of cities
    private ArrayList<POI> tour = new ArrayList<>();
    private Reusable reusable = new Reusable();

    // Cache
    private double fitness = 0;
    private int distance = 0;

    // Constructs a blank tour
    Tour() {
        for (int i = 0; i < TourManager.numberOfDestinations(); i++) {
            tour.add(null);
        }
    }

    ArrayList<POI> getTour(){
        return tour;
    }

    // Creates a random individual
    void generateIndividual() {
        // Loop through all our destination cities and add them to our tour
        for (int i = 0; i < TourManager.numberOfDestinations(); i++) {
            setDestination(i, TourManager.getDestination(i));
        }
        // Randomly reorder the tour
        Collections.shuffle(tour);
    }

    // Gets a destination from the tour
    POI getDestination(int i) {
        return tour.get(i);
    }

    // Sets a city in a certain position within a tour
    void setDestination(int i, POI destination) {
        tour.set(i, destination);
        // If the tours been altered we need to reset the fitness and distance
        fitness = 0;
        distance = 0;
    }

    // Gets the tours fitness
    double getFitness() {
        if (fitness == 0) {
            fitness = 1 / (double) getDistance();
        }
        return fitness;
    }

    // Gets the total distance of the tour
    public int getDistance() {
        if (distance == 0) {
            int tourDistance = 0;
            // Loop through our tour's destinations

            for (int i = 0; i < tourSize(); i++) {
                // Get destination we're travelling from
                POI currentDestination = getDestination(i);
                // Destination we're travelling to
                POI nextDestination;
                // Check we're not on our tour's last destination, if we are, set our tour's final destination to our starting destination
                if (i + 1 < tourSize()) {
                    nextDestination = getDestination(i + 1);
                } else {
                    nextDestination = getDestination(0);
                }
                // Get the distance between the two cities
                tourDistance += reusable.calculateDistance(currentDestination.getCoordinates().getLat(), nextDestination.getCoordinates().getLat(), currentDestination.getCoordinates().getLng(), nextDestination.getCoordinates().getLng());
            }
            distance = tourDistance;
        }
        return distance;
    }

    // Get number of cities on our tour
    int tourSize() {
        return tour.size();
    }

    // Check if the tour contains a city
    boolean containsDestination(POI destination) {
        return tour.contains(destination);
    }

}