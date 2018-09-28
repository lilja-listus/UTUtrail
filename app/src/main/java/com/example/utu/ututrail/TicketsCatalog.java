package com.example.utu.ututrail;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.example.utu.ututrail.data.RailContentContract.UsersEntry;

/**
 * Catalog activity for showing the list of tickets
 *
 */

public class TicketsCatalog extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    //data loader
    private static final int PRODUCT_LOADER = 0;

    TicketsAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickets_catalog);


        // Find the ListView which will be populated with data
        ListView itemsListView = (ListView) findViewById(R.id.tickets_list);


        // empty view for when there is no data
        View emptyView = findViewById(R.id.empty_view_tickets);
        itemsListView.setEmptyView(emptyView);

        // adapter to create a list item for each row of data in the Cursor

        mCursorAdapter = new TicketsAdapter(this, null);
        itemsListView.setAdapter(mCursorAdapter);



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
                UsersEntry.COLUMN_TICKETS_INFO,
};


        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                UsersEntry.CONTENT_URI_TICKET,   // Provider content URI to query
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
