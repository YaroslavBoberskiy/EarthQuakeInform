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

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Create a fake list of custom earthquake locations.
        final ArrayList<ForecastContent> earthquakes = new ArrayList<>();
        earthquakes.add(new ForecastContent("2.5", "San Francisco", "30.11.2016"));
        earthquakes.add(new ForecastContent("4.5", "London", "02.09.2014"));
        earthquakes.add(new ForecastContent("2.0", "Tokyo", "11.11.2007"));
        earthquakes.add(new ForecastContent("5.7", "Mexico City", "09.10.2012"));
        earthquakes.add(new ForecastContent("1.2", "Moscow", "23.08.2001"));
        earthquakes.add(new ForecastContent("7.5", "Rio de Janeiro", "15.05.2011"));
        earthquakes.add(new ForecastContent("3.9", "San Francisco", "14.10.2008"));
        earthquakes.add(new ForecastContent("3.9", "Paris", "10.10.2013"));


        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // Create a custom {@link ForecastListAdapter} of earthquakes
        ForecastListAdapter forecastAdapter = new ForecastListAdapter(this, earthquakes);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ForecastContent forecastContent = earthquakes.get(position);
                Toast.makeText(getApplicationContext(), forecastContent.getLocation(), Toast.LENGTH_SHORT).show();
            }
        });

        earthquakeListView.setAdapter(forecastAdapter);

    }
}
