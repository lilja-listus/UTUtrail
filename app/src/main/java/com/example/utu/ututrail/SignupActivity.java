package com.example.utu.ututrail;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.utu.ututrail.data.RailContentContract.UsersEntry;

import static java.lang.Integer.parseInt;

/**
 * Signup activity for creating a new user
 */

public class SignupActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    final Context context = this;
//todo: needs to be cleaned
    //todo: cant send data if the fields are empty

    private EditText name;
    private EditText address;
    private EditText email;
    private EditText pass2;
    private EditText usname;
    private EditText card;
    private EditText pass1;
    private EditText lastName;
    private static final int ITEMS_LOADER = 0;

    private String namestr;
    private String addressstr;
    private String emailstr;
    private String pass2str;
    private String unamestr;
    private String cardstr;
    private String passs1tr;
    private String lastNamestr;
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
        setContentView(R.layout.signup_activity);
        Intent intent = getIntent();
        currentItemUri = intent.getData();
        if (currentItemUri == null) {
            setTitle(getString(R.string.editor_activity_title_new_item));
            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.editor_activity_title_edit_item));

            getLoaderManager().initLoader(ITEMS_LOADER, null, this);

        }
        name = (EditText) findViewById(R.id.TFname);
        address = (EditText) findViewById(R.id.TFaddress);
        email = (EditText) findViewById(R.id.TFemail);
        pass2 = (EditText) findViewById(R.id.TFconfirmpassword);
        usname = (EditText) findViewById(R.id.TFusername);
        card = (EditText) findViewById(R.id.TFcreditcard);
        pass1 = (EditText) findViewById(R.id.TFpassword);
        lastName = (EditText) findViewById(R.id.Lastname);

        name.setOnTouchListener(mTouchListener);
        address.setOnTouchListener(mTouchListener);
        email.setOnTouchListener(mTouchListener);
        pass2.setOnTouchListener(mTouchListener);
        usname.setOnTouchListener(mTouchListener);
        card.setOnTouchListener(mTouchListener);
        pass1.setOnTouchListener(mTouchListener);
        lastName.setOnTouchListener(mTouchListener);


    }

    /**
     * Get user input from editor and save item into database.
     */

//todo:delete the button
    private void saveItem() {
//todo: make sure it doesn't submit anything if the data is not entered

        namestr = name.getText().toString();
        addressstr = address.getText().toString();
        emailstr = email.getText().toString();
        pass2str = pass2.getText().toString();
        unamestr = usname.getText().toString();
        cardstr = card.getText().toString();
        passs1tr = pass1.getText().toString();
        lastNamestr = lastName.getText().toString();

        ContentValues values = new ContentValues();
        values.put(UsersEntry.COLUMN_USER_LOGIN, unamestr);
        if (!passs1tr.equals(pass2str)) {
            Toast pass = Toast.makeText(SignupActivity.this, "password doesn't match!", Toast.LENGTH_SHORT);
            pass.show();

        }
        values.put(UsersEntry.COLUMN_USER_PASSWORD, passs1tr);
        values.put(UsersEntry.COLUMN_USER_EMAIL, emailstr);
        values.put(UsersEntry.COLUMN_USER_NAME, namestr);
        values.put(UsersEntry.COLUMN_USER_LASTNAME, lastNamestr);
        values.put(UsersEntry.COLUMN_USER_ADDRESS, addressstr);


        int cardNumber = 0;
        if (!TextUtils.isEmpty(cardstr)) {
            cardNumber = parseInt(cardstr);
        }
        values.put(UsersEntry.COLUMN_USER_CARD, cardstr);

        // to determine if this is a new or existing item
        if (currentItemUri == null) {
            Uri newUri = getContentResolver().insert(UsersEntry.CONTENT_URI, values);

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

            case android.R.id.home:
                if (!mItemHasChanged) {
                    NavUtils.navigateUpFromSameTask(SignupActivity.this);
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.

                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(SignupActivity.this);
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
                UsersEntry._ID,
                UsersEntry.COLUMN_USER_LOGIN,
                UsersEntry.COLUMN_USER_PASSWORD,
                UsersEntry.COLUMN_USER_EMAIL,
                UsersEntry.COLUMN_USER_NAME,
                UsersEntry.COLUMN_USER_LASTNAME,
                UsersEntry.COLUMN_USER_ADDRESS,
                UsersEntry.COLUMN_USER_CARD};

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
            int loginColumnIndex = cursor.getColumnIndex(UsersEntry.COLUMN_USER_LOGIN);
            int passwordColumnIndex = cursor.getColumnIndex(UsersEntry.COLUMN_USER_PASSWORD);
            int nameColumnIndex = cursor.getColumnIndex(UsersEntry.COLUMN_USER_NAME);
            int lastNameColumnIndex = cursor.getColumnIndex(UsersEntry.COLUMN_USER_LASTNAME);
            int emailColumnIndex = cursor.getColumnIndex(UsersEntry.COLUMN_USER_EMAIL);
            int addressColumnIndex = cursor.getColumnIndex(UsersEntry.COLUMN_USER_ADDRESS);
            int cardColumnIndex = cursor.getColumnIndex(UsersEntry.COLUMN_USER_CARD);


            // Extract out the value from the Cursor for the given column index
            String loginValue = cursor.getString(loginColumnIndex);
            String passwordValue = cursor.getString(passwordColumnIndex);
            String nameValue = cursor.getString(nameColumnIndex);
            String lastNameValue = cursor.getString(lastNameColumnIndex);
            String emailValue = cursor.getString(emailColumnIndex);
            String addressValue = cursor.getString(addressColumnIndex);
            int cardValue = cursor.getInt(cardColumnIndex);


            // Update the views on the screen with the values from the database
            name.setText(nameValue);
            address.setText(addressValue);
            email.setText(emailValue);
            pass1.setText(passwordValue);
            usname.setText(loginValue);
            card.setText(Integer.toString(cardValue));
            lastName.setText(lastNameValue);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        name.setText("");
        card.setText("0");
        address.setText("");
        email.setText("");
        pass1.setText("");
        usname.setText("");
        pass2.setText("");
        lastName.setText("");


    }
}



