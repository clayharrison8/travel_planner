package com.example.planner.algorithms.travellingsalesmen;


import com.example.planner.model.POI;

import java.util.ArrayList;

// Based on genetic algorithm TSP tutorial by TheProjectSpot.com. Available at: http://www.theprojectspot.com/tutorial-post/applying-a-genetic-algorithm-to-the-travelling-salesman-problem/5
public class Population {

    // Holds population of tours
    private Tour[] tours;
    private ArrayList<POI> arrayList = new ArrayList<>();

    // Construct a population
    public Population(int populationSize, boolean initialise) {
        tours = new Tour[populationSize];
        // If we need to initialise a population of tours do so
        if (initialise) {
            // Loop and create individuals
            for (int i = 0; i < populationSize(); i++) {
                Tour newTour = new Tour();
                newTour.generateIndividual();
                saveTour(i, newTour);
            }
        }
    }

    // Saves a tour
    void saveTour(int i, Tour tour) {
        tours[i] = tour;
    }

    // Gets a tour from population
    Tour getTour(int i) {
        return tours[i];
    }

    // Gets the best tour in the population
    public Tour getFittest() {
        Tour fittest = tours[0];
        // Loop through individuals to find fittest
        for (int i = 1; i < populationSize(); i++) {
            if (fittest.getFitness() <= getTour(i).getFitness()) {
                fittest = getTour(i);
            }
        }
        arrayList = fittest.getTour();

        return fittest;
    }

    public ArrayList<POI> getList(){
        return arrayList;
    }


    // Gets population size
    int populationSize() {
        return tours.length;
    }
}