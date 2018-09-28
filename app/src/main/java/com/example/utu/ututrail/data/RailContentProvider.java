package com.example.utu.ututrail.data;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.utu.ututrail.data.RailContentContract.UsersEntry;


/**
 */

//Content provider for the app
public class RailContentProvider extends ContentProvider {


    // URI matcher code for the content URI for the whole table
    private static final int ITEMS = 100;
    private static final int TRAINS = 200;
    private static final int TICKETS = 300;

    // URI matcher code for the content URI for one item
    private static final int ITEMS_ID = 101;
    private static final int TRAINS_ID = 201;
    private static final int TICKETS_ID = 301;


    //UriMatcher object to match a content URI to a corresponding code.

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(RailContentContract.CONTENT_AUTHORITY, RailContentContract.PATH_ITEMS, ITEMS);
        uriMatcher.addURI(RailContentContract.CONTENT_AUTHORITY, RailContentContract.PATH_ITEMS + "/#", ITEMS_ID);
        uriMatcher.addURI(RailContentContract.CONTENT_AUTHORITY, RailContentContract.PATH_TRAINS, TRAINS);
        uriMatcher.addURI(RailContentContract.CONTENT_AUTHORITY, RailContentContract.PATH_TRAINS + "/#", TRAINS_ID);
        uriMatcher.addURI(RailContentContract.CONTENT_AUTHORITY, RailContentContract.PATH_TICKETS, TICKETS);
        uriMatcher.addURI(RailContentContract.CONTENT_AUTHORITY, RailContentContract.PATH_TICKETS + "/#", TICKETS_ID);
    }

    // Database helper object
    private RailContentHelper helper;

    @Override
    public boolean onCreate() {
        helper = new RailContentHelper(getContext());
        return true;

    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Get readable database
        SQLiteDatabase database = helper.getReadableDatabase();

        // Cursor to hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = uriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                cursor = database.query(UsersEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case ITEMS_ID:
                selection = UsersEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(UsersEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case TRAINS:
                cursor = database.query(UsersEntry.TABLE_NAME_TRAINS, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case TRAINS_ID:
                selection = UsersEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(UsersEntry.TABLE_NAME_TRAINS, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case TICKETS:
                cursor = database.query(UsersEntry.TABLE_NAME_TICKETS, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case TICKETS_ID:
                selection = UsersEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(UsersEntry.TABLE_NAME_TICKETS, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        //  notification URI on the Cursor
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the cursor
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                return insertItem(uri, contentValues);
            case TRAINS:
                return insertItem(uri, contentValues);
            case TICKETS:
                return insertItem(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    //Insert a new user and check whether data is valid
    private Uri insertItem(Uri uri, ContentValues values) {
        // Get writeable database
        SQLiteDatabase database = helper.getWritableDatabase();

        int match = uriMatcher.match(uri);

        switch (match) {

            //if the table is users
            case ITEMS:
                String login = values.getAsString(UsersEntry.COLUMN_USER_LOGIN);
                if (login == null) {
                    throw new IllegalArgumentException("Insert login ");
                }

                String password = values.getAsString(UsersEntry.COLUMN_USER_PASSWORD);
                if (password == null) {
                    throw new IllegalArgumentException("Insert password ");
                }
                String name = values.getAsString(UsersEntry.COLUMN_USER_NAME);
                if (name == null) {
                    throw new IllegalArgumentException("Insert name ");
                }
                String lastName = values.getAsString(UsersEntry.COLUMN_USER_LASTNAME);
                if (lastName == null) {
                    throw new IllegalArgumentException("Insert last name ");
                }
                String address = values.getAsString(UsersEntry.COLUMN_USER_ADDRESS);
                if (address == null) {
                    throw new IllegalArgumentException("Insert address ");
                }

                String email = values.getAsString(UsersEntry.COLUMN_USER_EMAIL);
                if (email == null) {
                    throw new IllegalArgumentException("Insert email ");
                }

                Integer card = values.getAsInteger(UsersEntry.COLUMN_USER_CARD);
                if (card != null && card < 0) {
                    throw new IllegalArgumentException("Insert valid card number");
                }


                // Insert the new product with the given values
                long id = database.insert(UsersEntry.TABLE_NAME, null, values);

                // to notify  listeners that the data has changed
                getContext().getContentResolver().notifyChange(uri, null);

                // Return the new URI with the ID (of the newly inserted row) appended at the end
                return ContentUris.withAppendedId(uri, id);
            case TRAINS:

                String nameTrain = values.getAsString(UsersEntry.COLUMN_TRAINS_NAME);
                if (nameTrain == null) {
                    throw new IllegalArgumentException("Insert train name ");
                }

                String type = values.getAsString(UsersEntry.COLUMN_TRAINS_TYPE);
                if (type == null) {
                    throw new IllegalArgumentException("Insert tain type ");
                }
                String duration = values.getAsString(UsersEntry.COLUMN_TRAINS_DURATION);
                if (duration == null) {
                    throw new IllegalArgumentException("Insert duration of the trip ");
                }
                String price = values.getAsString(UsersEntry.COLUMN_TRAINS_PRICE);
                if (price == null) {
                    throw new IllegalArgumentException("Insert price of the ticket ");
                }

                String services = values.getAsString(UsersEntry.COLUMN_TRAINS_SERVICE);
                if (services == null) {
                    throw new IllegalArgumentException("Insert the services offered ont the train ");
                }


                // Insert the new product with the given values
                long idTrains = database.insert(UsersEntry.TABLE_NAME_TRAINS, null, values);

                // to notify  listeners that the data has changed
                getContext().getContentResolver().notifyChange(uri, null);

                // Return the new URI with the ID (of the newly inserted row) appended at the end
                return ContentUris.withAppendedId(uri, idTrains);

                case TICKETS:

                String ticketInfo = values.getAsString(UsersEntry.COLUMN_TICKETS_INFO);
                if (ticketInfo == null) {
                    throw new IllegalArgumentException("No info about the ticket ");
                }



                // Insert the new product with the given values
                long idTickets = database.insert(UsersEntry.TABLE_NAME_TICKETS, null, values);

                // to notify  listeners that the data has changed
                getContext().getContentResolver().notifyChange(uri, null);

                // Return the new URI with the ID (of the newly inserted row) appended at the end
                return ContentUris.withAppendedId(uri, idTickets);


            default:
        throw new IllegalArgumentException("Update is not supported for " + uri);
    }

}

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                return updateItem(uri, contentValues, selection, selectionArgs);
            case ITEMS_ID:
                selection = UsersEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateItem(uri, contentValues, selection, selectionArgs);
            case TRAINS:
                return updateItem(uri, contentValues, selection, selectionArgs);
            case TRAINS_ID:
                selection = UsersEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateItem(uri, contentValues, selection, selectionArgs);
            case TICKETS:
                return updateItem(uri, contentValues, selection, selectionArgs);
            case TICKETS_ID:
                selection = UsersEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateItem(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    // to update items in the database with the given content values.

    private int updateItem(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        //to update the database getting writable database
        SQLiteDatabase database = helper.getWritableDatabase();

        final int match = uriMatcher.match(uri);
        switch (match) {
            case ITEMS:


                if (values.containsKey(UsersEntry.COLUMN_USER_LOGIN)) {
                    String login = values.getAsString(UsersEntry.COLUMN_USER_LOGIN);
                    if (login == null) {
                        throw new IllegalArgumentException("Insert a valid name ");
                    }
                }

                if (values.containsKey(UsersEntry.COLUMN_USER_PASSWORD)) {
                    String password = values.getAsString(UsersEntry.COLUMN_USER_PASSWORD);
                    if (password == null) {
                        throw new IllegalArgumentException("Insert password ");
                    }
                }
                if (values.containsKey(UsersEntry.COLUMN_USER_EMAIL)) {
                    String email = values.getAsString(UsersEntry.COLUMN_USER_EMAIL);
                    if (email == null) {
                        throw new IllegalArgumentException("Insert email ");
                    }
                }
                if (values.containsKey(UsersEntry.COLUMN_USER_NAME)) {
                    String name = values.getAsString(UsersEntry.COLUMN_USER_NAME);

                    if (name == null) {
                        throw new IllegalArgumentException("Insert name ");
                    }
                }
                if (values.containsKey(UsersEntry.COLUMN_USER_LASTNAME)) {
                    String lastName = values.getAsString(UsersEntry.COLUMN_USER_LASTNAME);
                    if (lastName == null) {
                        throw new IllegalArgumentException("Insert last name ");
                    }
                }
                if (values.containsKey(UsersEntry.COLUMN_USER_ADDRESS)) {
                    String address = values.getAsString(UsersEntry.COLUMN_USER_ADDRESS);
                    if (address == null) {
                        throw new IllegalArgumentException("Insert address ");
                    }
                }

                if (values.containsKey(UsersEntry.COLUMN_USER_CARD)) {
                    Integer card = values.getAsInteger(UsersEntry.COLUMN_USER_CARD);
                    if (card != null && card < 0) {
                        throw new IllegalArgumentException("Insert valid card number");
                    }
                }


                //if there is no new values, no update needed
                if (values.size() == 0) {
                    return 0;
                }


                // update and shows the number of rows updated
                int rowsUpdated = database.update(UsersEntry.TABLE_NAME, values, selection, selectionArgs);

                // to notify if at least one row is updated
                if (rowsUpdated != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                // Return the number of rows updated
                return rowsUpdated;

            case TRAINS:
                if (values.containsKey(UsersEntry.COLUMN_TRAINS_NAME)) {
                    String nameTrains = values.getAsString(UsersEntry.COLUMN_TRAINS_NAME);
                    if (nameTrains == null) {
                        throw new IllegalArgumentException("Insert a valid name of train ");
                    }
                }

                if (values.containsKey(UsersEntry.COLUMN_TRAINS_TYPE)) {
                    String type = values.getAsString(UsersEntry.COLUMN_TRAINS_TYPE);
                    if (type == null) {
                        throw new IllegalArgumentException("Insert type of the train ");
                    }
                }
                if (values.containsKey(UsersEntry.COLUMN_TRAINS_DURATION)) {
                    String duration = values.getAsString(UsersEntry.COLUMN_TRAINS_DURATION);
                    if (duration == null) {
                        throw new IllegalArgumentException("Insert duration of the trip ");
                    }
                }
                if (values.containsKey(UsersEntry.COLUMN_TRAINS_PRICE)) {
                    String price = values.getAsString(UsersEntry.COLUMN_TRAINS_PRICE);

                    if (price == null) {
                        throw new IllegalArgumentException("Insert price ");
                    }
                }
                if (values.containsKey(UsersEntry.COLUMN_TRAINS_SERVICE)) {
                    String services = values.getAsString(UsersEntry.COLUMN_TRAINS_SERVICE);
                    if (services == null) {
                        throw new IllegalArgumentException("Insert last name ");
                    }
                }

                //if there is no new values, no update needed
                if (values.size() == 0) {
                    return 0;
                }

                // update and shows the number of rows updated
                int rowsUpdatedTrain = database.update(UsersEntry.TABLE_NAME_TRAINS, values, selection, selectionArgs);

                // to notify if at least one row is updated
                if (rowsUpdatedTrain != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                // Return the number of rows updated
                return rowsUpdatedTrain;
            case TICKETS:
                if (values.containsKey(UsersEntry.COLUMN_TICKETS_INFO)) {
                    String nameTrains = values.getAsString(UsersEntry.COLUMN_TICKETS_INFO);
                    if (nameTrains == null) {
                        throw new IllegalArgumentException("Insert valid info ");
                    }
                }



                //if there is no new values, no update needed
                if (values.size() == 0) {
                    return 0;
                }

                // update and shows the number of rows updated
                int rowsUpdatedTicket = database.update(UsersEntry.TABLE_NAME_TICKETS, values, selection, selectionArgs);

                // to notify if at least one row is updated
                if (rowsUpdatedTicket != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                // Return the number of rows updated
                return rowsUpdatedTicket;
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = helper.getWritableDatabase();

        // Track the number of rows that were deleted
        int rowsDeleted;

        final int match = uriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(UsersEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ITEMS_ID:
                // Delete a single row given by the ID in the URI
                selection = UsersEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(UsersEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case TRAINS:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(UsersEntry.TABLE_NAME_TRAINS, selection, selectionArgs);
                break;
            case TRAINS_ID:
                // Delete a single row given by the ID in the URI
                selection = UsersEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(UsersEntry.TABLE_NAME_TRAINS, selection, selectionArgs);
                break;
            case TICKETS:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(UsersEntry.TABLE_NAME_TICKETS, selection, selectionArgs);
                break;
            case TICKETS_ID:
                // Delete a single row given by the ID in the URI
                selection = UsersEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(UsersEntry.TABLE_NAME_TICKETS, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        //notify if at least one row is deleted
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows deleted
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                return UsersEntry.CONTENT_LIST_TYPE;
            case ITEMS_ID:
                return UsersEntry.CONTENT_ITEM_TYPE;
            case TRAINS:
                return UsersEntry.CONTENT_LIST_TYPE_TRAINS;
            case TRAINS_ID:
                return UsersEntry.CONTENT_TRAIN_TYPE_TRAINS;
            case TICKETS:
                return UsersEntry.CONTENT_LIST_TYPE_TICKETS;
            case TICKETS_ID:
                return UsersEntry.CONTENT_TRAIN_TYPE_TICKETS;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

}