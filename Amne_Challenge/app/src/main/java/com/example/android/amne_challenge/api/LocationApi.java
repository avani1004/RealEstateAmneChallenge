package com.example.android.amne_challenge.api;

import com.example.android.amne_challenge.response.LocationResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by avaniarora on 10/21/17.
 */

public interface LocationApi {
    @GET
    Call<LocationResponse> getLocation(@Url String url);

}
//https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=42.985620, -71.454722&radius=2000&type=restaurant&key=AIzaSyBH6PRnIHOpHbqbolQ3RuJPGybTjLRN1Vk