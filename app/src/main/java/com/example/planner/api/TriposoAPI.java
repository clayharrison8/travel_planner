package com.example.planner.api;

import com.example.planner.model.InitialResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

// GET requests for Retrofit
public interface TriposoAPI {

    String account = "EXP1M0G9";
    String token = "gdgka5mszhqure20uedc3cmle7skcsbo";
    String authentication = "account=" + account + "&token=" + token;

    // Used for the Search Bar
    @GET("location.json?tag_labels=country&count=100&order_by=-score&fields=id,name,type,score,country_id,parent_id,snippet,images,coordinates,structured_content&" + authentication)
    Call<InitialResponse> getSearchBar();

    // Used for Planner Dropdown List
    @GET("location.json?tag_labels=city&count=20&fields=id,name,score,country_id,parent_id,type,snippet,images,coordinates,structured_content&" + authentication)
    Call<InitialResponse> getPlannerList();

    // Used for the Catalogue page
    @GET("location.json?tag_labels=country&count=10&order_by=-score&fields=id,name,type,score,country_id,parent_id,snippet,images,coordinates,structured_content,properties&" + authentication)
    Call<InitialResponse> getCountryList();

    // Used for when clicks on city item
    @GET("location.json?fields=id,name,type,score,country_id,parent_id,snippet,images,coordinates,structured_content&" + authentication)
    Call<InitialResponse> getCity(@Query("id") String id);

    // Used for home page and planner dropdown list
    @GET("location.json?tag_labels=city&count=10&fields=id,name,score,country_id,parent_id,type,snippet,images,coordinates,structured_content&" + authentication)
    Call<InitialResponse> getPopularCityList();

    // Used for getting popular cities of specific country
    @GET("location.json?&tag_labels=city&count=10&order_by=-score&fields=id,name,score,type,country_id,parent_id,snippet,images,coordinates,structured_content&" + authentication)
    Call<InitialResponse> getCitiesList(@Query("part_of") String partOf);

    // Getting popular attractions in city/country
    @GET("poi.json?&count=10&order_by=-score&fields=id,name,score,snippet,images,coordinates,structured_content,booking_info&" + authentication)
    Call<InitialResponse> getAttractions(@Query("location_id") String locationId,
                                         @Query("bookable") String bookable,
                                         @Query("score") String score,
                                         @Query("tag_labels") String tag_labels);

    // Used for Planner page
    @GET("poi.json?&order_by=-score&tag_labels=!lunch|!dinner|!breakfast&fields=id,name,score,snippet,images,coordinates,structured_content,booking_info&" + authentication)
    Call<InitialResponse> getPlannerResults(@Query("location_id") String locationId,
                                            @Query("bookable") String bookable,
                                            @Query("annotate") String annotate,
                                            @Query("tag_labels") String tag_labels,
                                            @Query("count") int count);

    // Getting popular tours ranked by score for a specific city
    @GET("tour.json?count=10&order_by=-score&fields=id,name,score,images,structured_content,booking_info&" + authentication)
    Call<InitialResponse> getTours(@Query("location_ids") String locationId);

    // Getting regions ranked by score for a specific country
    @GET("location.json?count=10&type=region&fields=id,name,type,score,country_id,parent_id,snippet,images,coordinates,structured_content&" + authentication)
    Call<InitialResponse> getRegions(@Query("part_of") String partOf);

    // Getting hotels ranked by score for a specific city
    @GET("poi.json?count=10&tag_labels=hotels&fields=id,name,score,snippet,images,coordinates,structured_content&" + authentication)
    Call<InitialResponse> getHotels(@Query("location_id") String locationId,
                                    @Query("score") String score);

    // Getting nightlife activities ranked by score for a specific city
    @GET("poi.json?count=10&tag_labels=nightlife&fields=id,name,score,snippet,images,coordinates,structured_content&" + authentication)
    Call<InitialResponse> getNightlife(@Query("location_id") String locationId,
                                       @Query("score") String score);

    @GET("poi.json?count=10&tag_labels=eatingout&fields=id,name,score,snippet,images,coordinates,structured_content&" + authentication)
    Call<InitialResponse> getEatDrink(@Query("location_id") String locationId,
                                      @Query("score") String score);


}
