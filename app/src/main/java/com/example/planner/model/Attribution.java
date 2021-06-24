package com.example.planner.model;

import com.google.gson.annotations.SerializedName;

public class Attribution {
    @SerializedName("source_id")
    private String source_id;
    @SerializedName("url")
    private String url;

    public String getSource_id() {
        return source_id;
    }

    public void setSource_id(String source_id) {
        this.source_id = source_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
