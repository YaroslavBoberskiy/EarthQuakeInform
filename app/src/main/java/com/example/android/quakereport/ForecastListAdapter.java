package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

public class ForecastListAdapter extends ArrayAdapter<ForecastContent> {

    private ForecastContent forecastContent;
    private View listItemView;
    private String city;
    private String km;
    private String location;
    private String magnitude;
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

        DecimalFormat formatter = new DecimalFormat("0.0");
        magnitude = formatter.format(forecastContent.getMagnitude());

        TextView magnitudeTv = (TextView) listItemView.findViewById(R.id.magTextView);
        TextView cityTv = (TextView) listItemView.findViewById(R.id.locTextView);
        TextView dateTv = (TextView) listItemView.findViewById(R.id.dateTextView);
        TextView timeTv = (TextView) listItemView.findViewById(R.id.timeTextView);
        TextView kmTv = (TextView) listItemView.findViewById(R.id.kmTextView);

        dateTv.setText(forecastContent.getDate());
        timeTv.setText(forecastContent.getTime());
        cityTv.setText(city);
        kmTv.setText(km);
        magnitudeTv.setText(magnitude);

        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeTv.getBackground();
        int magnitudeColor = getMagnitudeColor(forecastContent.getMagnitude());
        magnitudeCircle.setColor(magnitudeColor);

        return listItemView;
    }

    private int getMagnitudeColor (double magnitude) {
        int magnitudeColorResID;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResID = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResID = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResID = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResID = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResID = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResID = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResID = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResID = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResID = R.color.magnitude9;
                break;
            default:
                magnitudeColorResID = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResID);
    }

}
