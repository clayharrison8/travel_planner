package com.example.planner.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InitialResponse {
    @SerializedName("more")
    @Expose
    private String more;
    @SerializedName("results")
    @Expose
    private List<POI> results;

    public String getMore() { return more; }

    public void setMore(String more) {
        this.more = more;
    }

    public void setResults(List<POI> results) {
        this.results = results;
    }

    public List<POI> getResults() { return results; }

}
