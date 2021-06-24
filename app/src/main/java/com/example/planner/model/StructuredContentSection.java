package com.example.planner.model;

import com.google.gson.annotations.SerializedName;

public class StructuredContentSection {
    @SerializedName("body")
    private String body;

    @SerializedName("title")
    private String title;

    public StructuredContentSection(){}

    public StructuredContentSection(String line1, String line2) {
        body = line1;
        title = line2;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
