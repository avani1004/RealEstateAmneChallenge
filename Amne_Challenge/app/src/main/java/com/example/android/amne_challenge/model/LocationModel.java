package com.example.android.amne_challenge.model;

/**
 * Created by avaniarora on 10/21/17.
 */

public class LocationModel {
    public String name = null;
    public LocationGeometry geometry = null;

    public LocationModel(String name, LocationGeometry geometry) {
        this.name = name;
        this.geometry = geometry;
    }
}
