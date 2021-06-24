package com.example.planner;

import com.example.planner.model.POI;

public interface CustomClickListener {
    void cardClicked(POI POI);
    void PopUpClicked(POI POI, Integer integer);
}
