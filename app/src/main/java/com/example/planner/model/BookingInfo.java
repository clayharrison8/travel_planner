package com.example.planner.model;

import com.google.gson.annotations.SerializedName;

public class BookingInfo {
    @SerializedName("vendor")
    private String vendor;
    @SerializedName("vendor_object_id")
    private String vendor_object_id;
    @SerializedName("vendor_object_url")
    private String vendor_object_url;
    @SerializedName("price")
    private Price price;

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getVendor_object_id() {
        return vendor_object_id;
    }

    public void setVendor_object_id(String vendor_object_id) {
        this.vendor_object_id = vendor_object_id;
    }

    public String getVendor_object_url() {
        return vendor_object_url;
    }

    public void setVendor_object_url(String vendor_object_url) {
        this.vendor_object_url = vendor_object_url;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }
}
