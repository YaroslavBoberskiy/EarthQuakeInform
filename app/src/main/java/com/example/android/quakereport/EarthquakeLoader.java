package com.example.android.quakereport;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

/**
 * Created by YB on 07.12.2016.
 */

public class EarthquakeLoader extends AsyncTaskLoader {

    public EarthquakeLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<ForecastContent> loadInBackground() {
        return null;
    }


}
