package com.example.android.quakereport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by YB on 30.11.2016.
 */

public class ForecastListAdapter extends ArrayAdapter<ForecastContent> {

    private ForecastContent forecastContent;
    private View listItemView;

    public ForecastListAdapter(Context context, List<ForecastContent> objects) {
        super(context, 0, objects);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.forecast_list_item, parent, false);
        }

        forecastContent = getItem(position);

        TextView magnitudeTv = (TextView) listItemView.findViewById(R.id.magTextView);
        TextView locationTv = (TextView) listItemView.findViewById(R.id.locTextView);
        TextView dateTv = (TextView) listItemView.findViewById(R.id.dateTextView);

        magnitudeTv.setText(forecastContent.getMagnitude());
        locationTv.setText(forecastContent.getLocation());
        dateTv.setText(forecastContent.getDate());

        return listItemView;
    }
}
