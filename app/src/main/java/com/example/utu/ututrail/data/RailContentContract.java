package com.example.utu.ututrail.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * API Contract for the app
 */
public final class RailContentContract {

    // uri
    public static final String CONTENT_AUTHORITY = "com.example.utu.ututrail.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final Uri BASE_CONTENT_URI_TRAIN = Uri.parse("content://" + CONTENT_AUTHORITY);
    //path to the table of users
    public static final String PATH_ITEMS = "items";
    //path to the table of trains
    public static final String PATH_TRAINS = "trains";
    //path to the table of tickets
    public static final String PATH_TICKETS = "tickets";

    // the empty constructor
    private RailContentContract() {
    }

    /**
     * Inner class to define values for the database table.
     */
    public static final class UsersEntry implements BaseColumns {

        // The content URI to access the data in the provider for both tables
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ITEMS);
        public static final Uri CONTENT_URI_TRAIN = Uri.withAppendedPath(BASE_CONTENT_URI_TRAIN, PATH_TRAINS);
        public static final Uri CONTENT_URI_TICKET = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_TICKETS);


        // The MIME type of for a list of users
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;

        // The MIME type of for one user
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;


        // The MIME type of for a list of trains
        public static final String CONTENT_LIST_TYPE_TRAINS =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAINS;

        // The MIME type of for one train
        public static final String CONTENT_TRAIN_TYPE_TRAINS =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAINS;

        // The MIME type of for a list of tickets
        public static final String CONTENT_LIST_TYPE_TICKETS =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TICKETS;

        // The MIME type of for one ticket
        public static final String CONTENT_TRAIN_TYPE_TICKETS =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TICKETS;

        // Names of database tables
        public final static String TABLE_NAME = "items";
        public final static String TABLE_NAME_TRAINS = "trains";
        public final static String TABLE_NAME_TICKETS = "tickets";

        //the table of users
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_USER_LOGIN = "login";
        public final static String COLUMN_USER_PASSWORD = "password";
        public final static String COLUMN_USER_NAME = "name";
        public final static String COLUMN_USER_LASTNAME = "lastname";
        public static final String COLUMN_USER_EMAIL = "email";
        public static final String COLUMN_USER_ADDRESS = "address";
        public static final String COLUMN_USER_CARD = "card";

        //for table of trains
        public final static String COLUMN_TRAINS_DURATION = "duration";
        public final static String COLUMN_TRAINS_NAME = "name";
        public final static String COLUMN_TRAINS_TYPE = "type";
        public final static String COLUMN_TRAINS_PRICE = "price";
        public static final String COLUMN_TRAINS_SERVICE = "service";

        //for table of tickets
        public final static String COLUMN_TICKETS_INFO = "info";
    }
}