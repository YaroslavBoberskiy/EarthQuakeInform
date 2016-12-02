package com.example.android.quakereport;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by YB on 30.11.2016.
 */

public class ForecastContent {

    private double magnitude;
    private String location;
    private long timeInMilliseconds;
    private String quakeInfoUrl;

    public ForecastContent(double magnitude, String location, long time, String quakeInfoUrl) {
        this.magnitude = magnitude;
        this.timeInMilliseconds = time;
        this.location = location;
        this.quakeInfoUrl = quakeInfoUrl;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public String getLocation() {
        return location;
    }

    public String getQuakeInfoUrl() {
        return quakeInfoUrl;
    }

    public long getTimeInMilliseconds() {
        return timeInMilliseconds;
    }

    public String getTime () {
        Date dateObject = new Date(getTimeInMilliseconds());
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        String formattedTime = timeFormat.format(dateObject);
        return formattedTime;
    }

    public String getDate () {
        Date dateObject = new Date(getTimeInMilliseconds());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String formattedDate = dateFormat.format(dateObject);
        return formattedDate;
    }

}
