package com.example.planner.model;

import com.google.gson.annotations.SerializedName;

public class Coordinates {
    @SerializedName("latitude")
    private double lat;
    @SerializedName("longitude")
    private double lng;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }


}
