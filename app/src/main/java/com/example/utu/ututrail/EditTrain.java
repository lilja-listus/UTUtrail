package com.example.utu.ututrail;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.utu.ututrail.data.RailContentContract;


/**
 * Signup activity for editing info about trains
 */

public class EditTrain extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private EditText trainName, trainType, tripDuration, ticketPrice, trainServices;

    private static final int ITEMS_LOADER = 0;

    private String trainNameStr, trainTypeStr, tripDurationStr, ticketPriceStr, trainServicesStr;

    private Uri currentItemUri;

    private boolean mItemHasChanged = false;
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mItemHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_train);
        Intent intent = getIntent();
        currentItemUri = intent.getData();
        if (currentItemUri == null) {
            setTitle(getString(R.string.editor_activity_title_new_item));
            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.editor_activity_title_edit_item));

            getLoaderManager().initLoader(ITEMS_LOADER, null, this);

        }
        trainName = (EditText) findViewById(R.id.edit_train_name);
        trainType = (EditText) findViewById(R.id.edit_train_type);
        tripDuration = (EditText) findViewById(R.id.edit_trip_duration);
        ticketPrice = (EditText) findViewById(R.id.edit_price);
        trainServices = (EditText) findViewById(R.id.edit_train_services);

        trainName.setOnTouchListener(mTouchListener);
        trainType.setOnTouchListener(mTouchListener);
        tripDuration.setOnTouchListener(mTouchListener);
        ticketPrice.setOnTouchListener(mTouchListener);
        trainServices.setOnTouchListener(mTouchListener);


        Button saveButton = (Button) findViewById(R.id.edit_save);

        intent = new Intent(this, AdminActivity.class);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo: Make sure it doesn't pass if the pass doesn't match

                //todo: make sure it doesn't start a new activity if the user didn't fill in everything
                Intent intent = new Intent(EditTrain.this, ChooseSeatActivity.class);

                startActivity(intent);

            }

        });
    }

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_signup, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (currentItemUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveItem();
                return true;
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                if (!mItemHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditTrain.this);
                    return true;
                }


                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(EditTrain.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (!mItemHasChanged) {
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

// Show a dialog that warns the user there are unsaved changes that will be lost
//if they continue leaving the editor.

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /*
    /**
     * Prompt the user to confirm that they want to delete this item.
     */
    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteItem();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Perform the deletion of the item in the database.
     */
    private void deleteItem() {
        if (currentItemUri != null) {
            int rowsDeleted = getContentResolver().delete(currentItemUri, null, null);

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.editor_delete_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        // Close the activity
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
        // If the loader is invalidated, clear out all the data from the input fields.

        trainName.setText("");
        trainType.setText("");
        tripDuration.setText("");
        ticketPrice.setText("");
        trainServices.setText("");

    }
}



