package com.example.android.amne_challenge.result;

/**
 * Created by avaniarora on 10/22/17.
 */

public class AgencyResult {
    public String agency = null;
    public Double distance = null;

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public AgencyResult(String agency, Double distance) {
        this.agency = agency;
        this.distance = distance;
    }
}
