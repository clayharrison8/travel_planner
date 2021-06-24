package com.example.planner.model;

import com.google.gson.annotations.SerializedName;

public class CountryImageSizes {
    @SerializedName("medium")
    private CountryImageMediumSize medium;
    @SerializedName("original")
    private CountryImageOriginalSize original;

    public CountryImageMediumSize getMedium() {
        return medium;
    }

    public void setMedium(CountryImageMediumSize medium) {
        this.medium = medium;
    }

    public CountryImageOriginalSize getOriginal() {
        return original;
    }

    public void setOriginal(CountryImageOriginalSize original) {
        this.original = original;
    }
}
