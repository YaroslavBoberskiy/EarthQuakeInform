package com.example.android.quakereport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ForecastListAdapter extends ArrayAdapter<ForecastContent> {

    private ForecastContent forecastContent;
    private View listItemView;
    private String city;
    private String km;
    private String location;
    private static final String LOCATION_SEPARATOR = " of ";

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
        location = forecastContent.getLocation();

        if (location.contains(LOCATION_SEPARATOR)) {
            km = location.split(LOCATION_SEPARATOR)[0] + LOCATION_SEPARATOR;
            city = location.split(LOCATION_SEPARATOR)[1];
        } else {
            km = getContext().getString(R.string.near_the);
            city = location;
        }

        TextView magnitudeTv = (TextView) listItemView.findViewById(R.id.magTextView);
        TextView cityTv = (TextView) listItemView.findViewById(R.id.locTextView);
        TextView dateTv = (TextView) listItemView.findViewById(R.id.dateTextView);
        TextView timeTv = (TextView) listItemView.findViewById(R.id.timeTextView);
        TextView kmTv = (TextView) listItemView.findViewById(R.id.kmTextView);

        magnitudeTv.setText(forecastContent.getMagnitude());
        dateTv.setText(forecastContent.getDate());
        timeTv.setText(forecastContent.getTime());
        cityTv.setText(city);
        kmTv.setText(km);


        return listItemView;
    }

}
