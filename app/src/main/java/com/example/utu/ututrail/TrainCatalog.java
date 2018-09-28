package com.example.utu.ututrail;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.utu.ututrail.data.RailContentContract.UsersEntry;

import java.util.ArrayList;

/**
 * Catalog activity for showing the list of trains that admin can access
 *
 */

public class TrainCatalog extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    public static ArrayList<String[]> listOfTrains;

    //data loader
    private static final int PRODUCT_LOADER = 0;
    //String CONTENT_URI = UsersEntry.CONTENT_URI_TRAIN + "";

    TrainAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.train_catalog);


        // Find the ListView which will be populated with data
        ListView itemsListView = (ListView) findViewById(R.id.train_list);


        // empty view for when there is no data
        View emptyView = findViewById(R.id.empty_view_train);


        itemsListView.setEmptyView(emptyView);

        // adapter to create a list item for each row of data in the Cursor

        mCursorAdapter = new TrainAdapter(this, null);
        itemsListView.setAdapter(mCursorAdapter);

        // Setup the item click listener
        itemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent editIntent = new Intent(TrainCatalog.this, EditTrain.class);
                Uri currentItemUri = ContentUris.withAppendedId(UsersEntry.CONTENT_URI_TRAIN, id);
                editIntent.setData(currentItemUri);
                startActivity(editIntent);
            }
        });


        // ititialize  the loader
        getLoaderManager().initLoader(PRODUCT_LOADER, null, this);
    }

    /**
     * Helper method to delete all items in the database.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                UsersEntry._ID,
                UsersEntry.COLUMN_TRAINS_NAME,
                UsersEntry.COLUMN_TRAINS_TYPE,
                UsersEntry.COLUMN_TRAINS_DURATION,
                UsersEntry.COLUMN_TRAINS_PRICE,
                UsersEntry.COLUMN_TRAINS_SERVICE};


        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                UsersEntry.CONTENT_URI_TRAIN,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update with new cursor containing updated data
        mCursorAdapter.swapCursor(data);


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted
        mCursorAdapter.swapCursor(null);
    }
}
