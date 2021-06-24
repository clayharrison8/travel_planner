package com.example.planner.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Calling retrofit, this is the initial part of the call
public class RetrofitClient {
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://www.triposo.com/api/20190906/";
    public static TriposoAPI getService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit.create(TriposoAPI.class);
    }
}
