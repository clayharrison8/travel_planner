package com.example.planner;

import com.example.planner.api.RetrofitClient;
import com.example.planner.api.TriposoAPI;
import com.example.planner.model.InitialResponse;
import com.example.planner.model.POI;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JSONRequests {
    private TriposoAPI triposoAPI = RetrofitClient.getService();
    private Call<InitialResponse> call;
    private InitialResponse initialResponse;
    private ArrayList <POI> searchableList = new ArrayList<>();
    private ArrayList <POI> plannerList = new ArrayList<>();
    private DatabaseReference databaseReference;

    void getPopularCities(final RetrofitAdapter retrofitAdapter) {

        call = triposoAPI.getPopularCityList();
        call.enqueue(new Callback<InitialResponse>() {
            @Override
            public void onResponse(@NotNull Call<InitialResponse> call, @NotNull Response<InitialResponse> response) {
                initialResponse = response.body();
                if (initialResponse != null && initialResponse.getResults() != null) {
                    retrofitAdapter.setPOIList((ArrayList<POI>) initialResponse.getResults());
                }
            }

            @Override
            public void onFailure(@NotNull Call<InitialResponse> call, @NotNull Throwable t) {
            }
        });
    }

    public void getAttractions(POI poi, final POIAdapter poiAdapter, String bookable, String sightseeing) {
        call = triposoAPI.getAttractions(poi.getId(), bookable, sightseeing, MainActivity.getInterests());
        call.enqueue(new Callback<InitialResponse>() {
            @Override
            public void onResponse(@NotNull Call<InitialResponse> call, @NotNull Response<InitialResponse> response) {
                initialResponse = response.body();
                if (initialResponse != null && initialResponse.getResults() != null) {
                    poiAdapter.setPOIList((ArrayList<POI>) initialResponse.getResults());
                }
            }

            @Override
            public void onFailure(@NotNull Call<InitialResponse> call, @NotNull Throwable t) {
            }
        });
    }

    public void getPopularCountries(final RetrofitAdapter retrofitAdapter) {
        Call <InitialResponse> call = triposoAPI.getCountryList();
        call.enqueue(new Callback<InitialResponse>() {
            @Override
            public void onResponse(@NotNull Call<InitialResponse> call, @NotNull Response<InitialResponse> response) {
                InitialResponse initialResponse = response.body();
                if (initialResponse != null && initialResponse.getResults() != null) {

                    retrofitAdapter.setPOIList((ArrayList<POI>) initialResponse.getResults());
                }
            }

            @Override
            public void onFailure(@NotNull Call<InitialResponse> call, @NotNull Throwable t) {
            }
        });
    }

    public void getRegions(POI poi, final RetrofitAdapter retrofitAdapter) {
        call = triposoAPI.getRegions(poi.getId());
        call.enqueue(new Callback<InitialResponse>() {
            @Override
            public void onResponse(@NotNull Call<InitialResponse> call, @NotNull Response<InitialResponse> response) {
                initialResponse = response.body();
                if (initialResponse != null && initialResponse.getResults() != null) {
                    retrofitAdapter.setPOIList((ArrayList<POI>) initialResponse.getResults());
                }
            }

            @Override
            public void onFailure(@NotNull Call<InitialResponse> call, @NotNull Throwable t) {
            }
        });
    }

    public void getTours(final POIAdapter poiAdapter, POI poi) {
        call = triposoAPI.getTours(poi.getId());
        call.enqueue(new Callback<InitialResponse>() {
            @Override
            public void onResponse(@NotNull Call<InitialResponse> call, @NotNull Response<InitialResponse> response) {
                initialResponse = response.body();
                if (initialResponse != null && initialResponse.getResults() != null) {
                    poiAdapter.setPOIList((ArrayList<POI>) initialResponse.getResults());
                }
            }

            @Override
            public void onFailure(@NotNull Call<InitialResponse> call, @NotNull Throwable t) {
            }
        });
    }

    public void getRestaurants(POI poi, final POIAdapter poiAdapter, String eat_and_drink) {
        call = triposoAPI.getEatDrink(poi.getId(), eat_and_drink);
        call.enqueue(new Callback<InitialResponse>() {
            @Override
            public void onResponse(@NotNull Call<InitialResponse> call, @NotNull Response<InitialResponse> response) {
                initialResponse = response.body();
                if (initialResponse != null && initialResponse.getResults() != null) {
                    poiAdapter.setPOIList((ArrayList<POI>) initialResponse.getResults());

                }
            }

            @Override
            public void onFailure(@NotNull Call<InitialResponse> call, @NotNull Throwable t) {
            }
        });
    }

    public void getNightlife(POI poi, final POIAdapter poiAdapter, String nightlife) {
        call = triposoAPI.getNightlife(poi.getId(), nightlife);
        call.enqueue(new Callback<InitialResponse>() {
            @Override
            public void onResponse(@NotNull Call<InitialResponse> call, @NotNull Response<InitialResponse> response) {
                initialResponse = response.body();
                if (initialResponse != null && initialResponse.getResults() != null) {
                    poiAdapter.setPOIList((ArrayList<POI>) initialResponse.getResults());
                }
            }

            @Override
            public void onFailure(@NotNull Call<InitialResponse> call, @NotNull Throwable t) {
            }
        });
    }


    public void getCity(POI poi, final RetrofitAdapter retrofitAdapter) {
        call = triposoAPI.getCitiesList(poi.getId());
        call.enqueue(new Callback<InitialResponse>() {
            @Override
            public void onResponse(@NotNull Call<InitialResponse> call, @NotNull Response<InitialResponse> response) {
                initialResponse = response.body();
                if (initialResponse != null && initialResponse.getResults() != null) {
                    retrofitAdapter.setPOIList((ArrayList<POI>) initialResponse.getResults());
                }
            }

            @Override
            public void onFailure(@NotNull Call<InitialResponse> call, @NotNull Throwable t) {
            }
        });
    }

    public void getHotels(POI poi, String hotel_score, final POIAdapter poiAdapter) {
        call = triposoAPI.getHotels(poi.getId(), hotel_score);
        call.enqueue(new Callback<InitialResponse>() {
            @Override
            public void onResponse(@NotNull Call<InitialResponse> call, @NotNull Response<InitialResponse> response) {
                InitialResponse cityResponse = response.body();
                if (cityResponse != null && cityResponse.getResults() != null) {
                    poiAdapter.setPOIList((ArrayList<POI>) cityResponse.getResults());
                }
            }

            @Override
            public void onFailure(@NotNull Call<InitialResponse> call, @NotNull Throwable t) {
            }
        });
    }

    void getSearchBar() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Searchable List");
        Call <InitialResponse> call = triposoAPI.getSearchBar();
        call.enqueue(new Callback<InitialResponse>() {
            @Override
            public void onResponse(@NotNull Call<InitialResponse> call, @NotNull Response<InitialResponse> response) {
                InitialResponse cityResponse = response.body();
                if (cityResponse != null && cityResponse.getResults() != null) {
                    searchableList = (ArrayList<POI>) cityResponse.getResults();
                    databaseReference.setValue(searchableList);
                }
            }

            @Override
            public void onFailure(@NotNull Call<InitialResponse> call, @NotNull Throwable t) {
            }
        });
    }

    void getPlannerCityList() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Planner List");
        Call <InitialResponse> call = triposoAPI.getPlannerList();
        call.enqueue(new Callback<InitialResponse>() {
            @Override
            public void onResponse(@NotNull Call<InitialResponse> call, @NotNull Response<InitialResponse> response) {
                InitialResponse cityResponse = response.body();
                if (cityResponse != null && cityResponse.getResults() != null) {
                    plannerList = (ArrayList<POI>) cityResponse.getResults();
                    databaseReference.setValue(plannerList);
                }
            }

            @Override
            public void onFailure(@NotNull Call<InitialResponse> call, @NotNull Throwable t) {
            }
        });
    }





}
