package com.example.planner.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class StructuredContent {
    @SerializedName("sections")
    private ArrayList<StructuredContentSection> sections;

    public ArrayList<StructuredContentSection> getSections() {
        return sections;
    }

    public void setSections(ArrayList<StructuredContentSection> sections) {
        this.sections = sections;
    }

}
