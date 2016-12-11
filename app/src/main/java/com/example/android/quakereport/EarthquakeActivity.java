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

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<ForecastContent>> {

    public static final String LOG_TAG = EarthquakeActivity.class.getSimpleName();
    private static final int EARTHQUAKE_LOADER_ID = 1;
    private static final String USGS_REQUEST_URL =
            "http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";
    private ForecastListAdapter forecastAdapter;
    private ListView earthquakeListView;
    private TextView emptyTextView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Find a reference to the {@link ListView} in the layout
        earthquakeListView = (ListView) findViewById(R.id.list);
        forecastAdapter = new ForecastListAdapter(this, new ArrayList<ForecastContent>());
        earthquakeListView.setAdapter(forecastAdapter);
        emptyTextView = (TextView) findViewById(R.id.emptyView);
        progressBar = (ProgressBar) findViewById(R.id.loadingSpinner);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            emptyTextView.setText("");
            earthquakeListView.setEmptyView(emptyTextView);
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        } else {
            progressBar.setVisibility(View.GONE);
            emptyTextView.setText(R.string.No_internet_conn);
            earthquakeListView.setEmptyView(emptyTextView);
        }

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected earthquake.
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                ForecastContent currentEarthquake = forecastAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri earthquakeUri = Uri.parse(currentEarthquake.getQuakeInfoUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

    }

    private void updateUi(final List<ForecastContent> eatherquakes) {

        forecastAdapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (eatherquakes != null && !eatherquakes.isEmpty()) {
            forecastAdapter.addAll(eatherquakes);
        } else {
            emptyTextView.setText(R.string.No_earthquake_info);
            earthquakeListView.setEmptyView(emptyTextView);
        }
    }

    @Override
    public Loader<List<ForecastContent>> onCreateLoader(int id, Bundle args) {
        Log.i(LOG_TAG, "onCreateLoader");
        // Create a new loader for the given URL
        return new EarthquakeLoader(this, USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<ForecastContent>> loader, List<ForecastContent> data) {
        Log.i(LOG_TAG, "onLoadFinished");
        progressBar.setVisibility(View.GONE);
        updateUi(data);
    }

    @Override
    public void onLoaderReset(Loader<List<ForecastContent>> loader) {
        Log.i(LOG_TAG, "onLoaderReset");
        forecastAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
