package com.example.android.quakereport;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by YB on 30.11.2016.
 */

public class ForecastContent {

    private String magnitude;
    private String location;
    private long timeInMilliseconds;

    public ForecastContent(String magnitude, String location, long time) {
        this.magnitude = magnitude;
        this.timeInMilliseconds = time;
        this.location = location;
    }

    public String getMagnitude() {
        return magnitude;
    }

    public String getLocation() {
        return location;
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
