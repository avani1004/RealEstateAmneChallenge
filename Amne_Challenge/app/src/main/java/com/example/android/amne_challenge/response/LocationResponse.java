package com.example.android.amne_challenge.response;

import com.example.android.amne_challenge.model.LocationModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by avaniarora on 10/21/17.
 */

public class LocationResponse {
    @SerializedName("results")
    public List<LocationModel> locationList;
}

