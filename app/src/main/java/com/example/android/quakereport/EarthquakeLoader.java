package com.example.android.quakereport;

import android.content.Context;
import android.content.AsyncTaskLoader;
import android.util.Log;

import java.util.List;

/**
 * Created by YB on 07.12.2016.
 */

public class EarthquakeLoader extends AsyncTaskLoader {

    public static final String LOG_TAG = EarthquakeActivity.class.getSimpleName();
    private String urlAddress;

    public EarthquakeLoader(Context context, String urlAddress) {
        super(context);
        this.urlAddress = urlAddress;
    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG, "onStartLoading");
        forceLoad();
    }

    @Override
    public List<ForecastContent> loadInBackground() {
        Log.i(LOG_TAG, "loadInBackground");
        return QueryUtils.extractEarthquakes(urlAddress);
    }


}
