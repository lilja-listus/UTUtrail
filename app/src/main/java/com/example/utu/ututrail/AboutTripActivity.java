package com.example.utu.ututrail;
/**
 * The activity needed for train adapter to get info about a train
 */

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.utu.ututrail.data.RailContentContract;
import com.example.utu.ututrail.data.RailContentContract.UsersEntry;

public class AboutTripActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private TextView trainName, trainType, tripDuration, ticketPrice, trainServices;
    private Uri currentItemUri;

    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_trip);
        Intent intent = getIntent();

        trainName = (TextView) findViewById(R.id.train_name);
        trainType = (TextView) findViewById(R.id.train_type);
        tripDuration = (TextView) findViewById(R.id.trip_duration);
        ticketPrice = (TextView) findViewById(R.id.price);
        trainServices = (TextView) findViewById(R.id.train_services);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String[] projection = {
                UsersEntry._ID,
                UsersEntry.COLUMN_TRAINS_NAME,
                UsersEntry.COLUMN_TRAINS_TYPE,
                UsersEntry.COLUMN_TRAINS_DURATION,
                UsersEntry.COLUMN_TRAINS_PRICE,
                UsersEntry.COLUMN_TRAINS_SERVICE};

// This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                currentItemUri,
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        if (cursor.moveToFirst()) {

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

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}