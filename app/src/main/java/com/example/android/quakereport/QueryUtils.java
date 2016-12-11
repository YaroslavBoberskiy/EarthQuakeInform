package com.example.android.quakereport;


import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Make an HTTP request to the given URL and return a String for Json parsing as the response.
     */
    private static String httpConnection (URL url) throws IOException {
        HttpURLConnection httpConnection = null;
        InputStream inputStream = null;
        String jsonResponse = "";

        // If the URL is null, then return empty.
        if (url == null) {
            Log.e(LOG_TAG, "URL object in httpConnection method is null");
            return jsonResponse;
        }

        try {
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod("GET");
            httpConnection.setConnectTimeout(15000);
            httpConnection.setReadTimeout(10000);
            httpConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream.
            if (httpConnection.getResponseCode() == 200) {
                inputStream = httpConnection.getInputStream();
                jsonResponse = fetchDataFromStream(inputStream);
            } else {
                // If the request was not successful, then return empty
                Log.e(LOG_TAG, "Error response code: " + httpConnection.getResponseCode());
                return jsonResponse;
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String fetchDataFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL formURL (String urlAddress) {
        URL url = null;
        if (TextUtils.isEmpty(urlAddress)) {
            return null;
        }
        try {
            url = new URL(urlAddress);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }


    // Try to parse the JSON response string. If there's a problem with the way the JSON
    // is formatted, a JSONException exception object will be thrown.
    // Catch the exception so the app doesn't crash, and print the error message to the logs.

    public static ArrayList<ForecastContent> extractEarthquakes(String urlAddress) {

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArrayList<ForecastContent> earthquakes = new ArrayList<>();

        try {

            Double magnitude;
            String place;
            long time;
            String quakeInfoUrl;

            String jsonString = httpConnection(formURL(urlAddress));

            // If the JSON string is empty or null, then return null.
            if (TextUtils.isEmpty(jsonString)) {
                return null;
            }

            JSONObject rootJsonObject = new JSONObject(jsonString);

            // Extract the JSONArray associated with the key called "features",
            // which represents a list of features (or earthquakes).
            JSONArray featuresJsonArray = rootJsonObject.optJSONArray("features");

            if (featuresJsonArray.length() > 0) {
                // For each earthquake in the earthquakeArray, create an {@link Earthquake} object
                for (int i = 0; i < featuresJsonArray.length(); i ++) {
                    JSONObject featuresJsonObject = featuresJsonArray.optJSONObject(i);
                    JSONObject propertiesJsonObject = featuresJsonObject.getJSONObject("properties");

                    // Extract the value for the key called "mag"
                    magnitude = propertiesJsonObject.getDouble("mag");
                    // Extract the value for the key called "place"
                    place = propertiesJsonObject.getString("place");
                    // Extract the value for the key called "time"
                    time = propertiesJsonObject.getLong("time");
                    // Extract the value for the key called "url"
                    quakeInfoUrl = propertiesJsonObject.getString("url");

                    // Add ForecastContent object to the earthquakes ArrayList
                    earthquakes.add(new ForecastContent(magnitude, place, time, quakeInfoUrl));
            }
            }
            else {
                return null;
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Return the list of earthquakes
        return earthquakes;
    }

}
