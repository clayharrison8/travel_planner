package com.example.planner.model;

import com.google.gson.annotations.SerializedName;

public class Price {
    @SerializedName("currency")
    private String currency;
    @SerializedName("amount")
    private String amount;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
