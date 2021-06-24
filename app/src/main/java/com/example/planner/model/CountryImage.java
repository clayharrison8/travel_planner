package com.example.planner.model;

import com.google.gson.annotations.SerializedName;

public class CountryImage {
    @SerializedName("sizes")
    private CountryImageSizes sizes;

    public CountryImageSizes getSizes() {
        return sizes;
    }

    public void setSizes(CountryImageSizes sizes) {
        this.sizes = sizes;
    }



}
