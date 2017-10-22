package com.example.android.amne_challenge.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by avaniarora on 10/22/17.
 */

public class LocationItem {
    public String lat = null;
    public String lng = null;

    public void LocationItem(String lat, String lng) {
        this.lat = lat;
        this.lng = lng;
    }
}
