package com.example.utu.ututrail;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.utu.ututrail.data.RailContentContract;


/**
 * CLEAN IT
 *
 *
 */

public class TrainTypeOrder extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private TextView trainName, trainType, tripDuration, ticketPrice, trainServices;

    private static final int ITEMS_LOADER = 0;
    String CONTENT_URI = RailContentContract.UsersEntry.CONTENT_URI_TRAIN + "";


    private long id = 1;

    private String trainNameStr, trainTypeStr, tripDurationStr, ticketPriceStr, trainServicesStr;

    private Uri currentItemUri;

    private boolean mItemHasChanged = false;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }
    public static final String EXTRA_MESSAGE2 = "com.example.utu.ututrail.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_type_order);



        //get exra

        Intent intentNew = getIntent();
         String messageExtra = intentNew.getStringExtra(TripSummaryActivity.EXTRA_MESSAGE2);

        if (messageExtra.contains("Helsinki")){
            id = 2;
        }

        if (messageExtra.contains("Oulu")){
            id = 3;
        }


        currentItemUri = ContentUris.withAppendedId(Uri.parse(CONTENT_URI), id);



        if (currentItemUri == null) {
//            setTitle(getString(R.string.editor_activity_title_new_item));
            invalidateOptionsMenu();
        } else {
           // setTitle(getString(R.string.editor_activity_title_edit_item));

            getLoaderManager().initLoader(ITEMS_LOADER, null, this);

        }
        trainName = (TextView) findViewById(R.id.edit_train_name_order);
        trainType = (TextView) findViewById(R.id.edit_train_type_order);
        tripDuration = (TextView) findViewById(R.id.edit_trip_duration_order);
        ticketPrice = (TextView) findViewById(R.id.edit_price_order);
        trainServices = (TextView) findViewById(R.id.edit_train_services_order);


        Button saveButton = (Button) findViewById(R.id.edit_save);

        Intent intent = new Intent(this, ChooseSeatActivity.class);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo: Make sure it doesn't pass if the pass doesn't match

                //todo: make sure it doesn't start a new activity if the user didn't fill in everything
                Intent intent = new Intent(TrainTypeOrder.this, ChooseSeatActivity.class);

                startActivity(intent);

            }

        });
    }
    /**
     * Get user input from editor and save item into database.
     */




    private void saveItem() {
//todo: make sure it doesn't submit anything if the data is not entered

        trainNameStr = trainName.getText().toString();
        trainTypeStr = trainType.getText().toString();
        tripDurationStr = tripDuration.getText().toString();
        ticketPriceStr = ticketPrice.getText().toString();
        trainServicesStr = trainServices.getText().toString();

        ContentValues values = new ContentValues();

        values.put(RailContentContract.UsersEntry.COLUMN_TRAINS_NAME, trainNameStr);
        values.put(RailContentContract.UsersEntry.COLUMN_TRAINS_TYPE, trainTypeStr);
        values.put(RailContentContract.UsersEntry.COLUMN_TRAINS_DURATION, tripDurationStr);
        values.put(RailContentContract.UsersEntry.COLUMN_TRAINS_PRICE, ticketPriceStr);
        values.put(RailContentContract.UsersEntry.COLUMN_TRAINS_SERVICE, trainServicesStr);


        // to determine if this is a new or existing item
        if (currentItemUri == null) {
            Uri newUri = getContentResolver().insert(RailContentContract.UsersEntry.CONTENT_URI_TRAIN, values);

            if (newUri == null) {
                Toast.makeText(this, getString(R.string.editor_insert_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_insert_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {

            int rowsAffected = getContentResolver().update(currentItemUri, values, null, null);

            //to show the toast message whether it was successful
            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.editor_update_failed),
                        Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, getString(R.string.editor_update_successful), Toast.LENGTH_SHORT);

            }


        }
        finish();
    }



    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String[] projection = {
                RailContentContract.UsersEntry._ID,
                RailContentContract.UsersEntry.COLUMN_TRAINS_NAME,
                RailContentContract.UsersEntry.COLUMN_TRAINS_TYPE,
                RailContentContract.UsersEntry.COLUMN_TRAINS_DURATION,
                RailContentContract.UsersEntry.COLUMN_TRAINS_PRICE,
                RailContentContract.UsersEntry.COLUMN_TRAINS_SERVICE};

// This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                currentItemUri,
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader,Cursor cursor) {
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
    public void onLoaderReset(Loader<Cursor> loader){
        // If the loader is invalidated, clear out all the data from the input fields.



    }
}



