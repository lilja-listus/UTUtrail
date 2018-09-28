package com.example.utu.ututrail;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.utu.ututrail.data.RailContentContract;

/**
 * Adapter for the train catalog
 *
 */

public class TrainAdapter  extends CursorAdapter {

    public TrainAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.activity_about_trip, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        // Find individual views that we want to modify in the list item layout
        TextView trainName = (TextView) view.findViewById(R.id.train_name);
        TextView trainType = (TextView) view.findViewById(R.id.train_type);
        TextView tripDuration = (TextView) view.findViewById(R.id.trip_duration);
        TextView ticketPrice = (TextView) view.findViewById(R.id.price);
        TextView trainServices = (TextView) view.findViewById(R.id.train_services);

        // Find the columns of attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(RailContentContract.UsersEntry.COLUMN_TRAINS_NAME);
        int typeColumnIndex = cursor.getColumnIndex(RailContentContract.UsersEntry.COLUMN_TRAINS_TYPE);
        int durationColumnIndex = cursor.getColumnIndex(RailContentContract.UsersEntry.COLUMN_TRAINS_DURATION);
        int priceColumnIndex = cursor.getColumnIndex(RailContentContract.UsersEntry.COLUMN_TRAINS_PRICE);
        int servicesColumnIndex = cursor.getColumnIndex(RailContentContract.UsersEntry.COLUMN_TRAINS_SERVICE);

        // Read the  attributes from the Cursor for the current item
        String trainNameText = cursor.getString(nameColumnIndex);
        String trainTypeText = cursor.getString(typeColumnIndex);
        String durationText = cursor.getString(durationColumnIndex);
        String priceText = cursor.getString(priceColumnIndex);
        String servicesText = cursor.getString(servicesColumnIndex);
        // Update the TextViews with the attributes for the current item

        trainName.setText(trainNameText);
        trainType.setText(trainTypeText);
        tripDuration.setText(durationText);
        ticketPrice.setText(priceText);
        trainServices.setText(servicesText);

    }
}

