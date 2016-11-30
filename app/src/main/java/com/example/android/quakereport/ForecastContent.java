package com.example.android.quakereport;

/**
 * Created by YB on 30.11.2016.
 */

public class ForecastContent {

    private String magnitude;
    private String location;
    private String date;

    public ForecastContent(String magnitude, String location, String time) {
        this.magnitude = magnitude;
        this.location = location;
        this.date = time;
    }

    public String getMagnitude() {
        return magnitude;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }
}
