/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private static final String USGS_REQUEST_URL =
            "http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2016-01-01&endtime=2016-05-02&minfelt=50&minmagnitude=2";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        QuakeAsyncTask quakeAsyncTask = new QuakeAsyncTask();
        try {
            quakeAsyncTask.execute(formURL(USGS_REQUEST_URL));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    private void updateUi(final ArrayList<ForecastContent> eatherquakes) {
        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // Create a custom {@link ForecastListAdapter} of earthquakes
        ForecastListAdapter forecastAdapter = new ForecastListAdapter(this, eatherquakes);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ForecastContent forecastContent = eatherquakes.get(position);
                String url = forecastContent.getQuakeInfoUrl();
                Intent browser = new Intent(Intent.ACTION_VIEW);
                browser.setData(Uri.parse(url));
                startActivity(browser);
            }
        });

        earthquakeListView.setAdapter(forecastAdapter);
    }

    private static URL formURL (String urlAddress) throws MalformedURLException {
        URL url = null;
        if (TextUtils.isEmpty(urlAddress)) {
            return null;
        }
        url = new URL(urlAddress);
        return url;
    }

    private class QuakeAsyncTask extends AsyncTask<URL,Void,ArrayList<ForecastContent>> {

        @Override
        protected ArrayList<ForecastContent> doInBackground(URL... params) {
            return QueryUtils.extractEarthquakes(params[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<ForecastContent> forecastContents) {
            updateUi(forecastContents);
        }
    }

}
