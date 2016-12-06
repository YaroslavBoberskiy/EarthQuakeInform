package com.example.android.quakereport;


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

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Return a list of {@link ForecastContent} objects that has been built up from
     * parsing a JSON response.
     */

    private static String httpConnection (URL url) throws IOException {
        HttpURLConnection httpConnection = null;
        InputStream inputStream = null;
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        try {
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod("GET");
            httpConnection.setConnectTimeout(15000);
            httpConnection.setReadTimeout(10000);
            httpConnection.connect();

            if (httpConnection.getResponseCode() == 200) {
                inputStream = httpConnection.getInputStream();
                jsonResponse = fetchJsonData(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
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

    private static String fetchJsonData (InputStream inputStream) throws IOException {
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

    public static ArrayList<ForecastContent> extractEarthquakes(URL url) {

        ArrayList<ForecastContent> earthquakes = new ArrayList<>();

        try {

            Double mag;
            String place;
            long time;
            String quakeInfoUrl;

            String jsonString = httpConnection(url);

            JSONObject rootJsonObject = new JSONObject(jsonString);
            JSONArray featuresJsonArray = rootJsonObject.optJSONArray("features");
            for (int i = 0; i < featuresJsonArray.length(); i ++) {
                JSONObject featuresJsonObject = featuresJsonArray.optJSONObject(i);
                JSONObject propertiesJsonObject = featuresJsonObject.getJSONObject("properties");
                mag = propertiesJsonObject.getDouble("mag");
                place = propertiesJsonObject.getString("place");
                time = propertiesJsonObject.getLong("time");
                quakeInfoUrl = propertiesJsonObject.getString("url");
                earthquakes.add(new ForecastContent(mag, place, time, quakeInfoUrl));
            }


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Return the list of earthquakes
        return earthquakes;
    }

}
