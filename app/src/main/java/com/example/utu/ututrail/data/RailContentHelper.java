package com.example.utu.ututrail.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.utu.ututrail.AppUser;
import com.example.utu.ututrail.TrainType;
import com.example.utu.ututrail.data.RailContentContract.UsersEntry;

import java.util.ArrayList;
import java.util.List;

import static com.example.utu.ututrail.data.RailContentContract.UsersEntry.TABLE_NAME_TICKETS;
import static com.example.utu.ututrail.data.RailContentContract.UsersEntry.TABLE_NAME_TRAINS;

/**
 * Database helper that creates database and tables in it as well as contains methods
 * to work with ti
 */
public class RailContentHelper extends SQLiteOpenHelper {

    // the database name
    private static final String DATABASE_NAME = "users.db";
    private static final int DATABASE_VERSION = 1;

    //to construct a new instance
    public RailContentHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // String with SQL statement for creating the table
        String SQL_CREATE_USERS_TABLE = "CREATE TABLE " + UsersEntry.TABLE_NAME + " ("
                + UsersEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + UsersEntry.COLUMN_USER_LOGIN + " TEXT NOT NULL, "
                + UsersEntry.COLUMN_USER_PASSWORD + " TEXT NOT NULL, "
                + UsersEntry.COLUMN_USER_EMAIL + " TEXT NOT NULL, "
                + UsersEntry.COLUMN_USER_NAME + " TEXT NOT NULL, "
                + UsersEntry.COLUMN_USER_LASTNAME + " TEXT NOT NULL, "
                + UsersEntry.COLUMN_USER_ADDRESS + " TEXT NOT NULL, "
                + UsersEntry.COLUMN_USER_CARD + " INTEGER NOT NULL);";


        // String with SQL statement for creating the table of trains
        String SQL_CREATE_TRAINS_TABLE = "CREATE TABLE " + TABLE_NAME_TRAINS + " ("
                + UsersEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + UsersEntry.COLUMN_TRAINS_NAME + " TEXT NOT NULL, "
                + UsersEntry.COLUMN_TRAINS_TYPE + " TEXT NOT NULL, "
                + UsersEntry.COLUMN_TRAINS_DURATION + " TEXT NOT NULL, "
                + UsersEntry.COLUMN_TRAINS_PRICE + " TEXT NOT NULL, "
                + UsersEntry.COLUMN_TRAINS_SERVICE + " TEXT NOT NULL);";


        String SQL_CREATE_TICKETS_TABLE = "CREATE TABLE " + TABLE_NAME_TICKETS + " ("
                + UsersEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + UsersEntry.COLUMN_TICKETS_INFO + " TEXT NOT NULL);";

        // Execute the SQL statements
        db.execSQL(SQL_CREATE_TRAINS_TABLE);
        db.execSQL(SQL_CREATE_USERS_TABLE);
        db.execSQL(SQL_CREATE_TICKETS_TABLE);

        //for tables to have initial values we hardcode them
        hardcodeValues(db);
        hardcodeTickets(db);
        hardcodeAdmin(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    /**
     * This method is to fetch all users if needed
     *
     * @return userList
     */
    public List<AppUser> getAllUser() {
        // array of columns to fetch
        String[] columns = {
                UsersEntry._ID,
                UsersEntry.COLUMN_USER_EMAIL,
                UsersEntry.COLUMN_USER_NAME,
                UsersEntry.COLUMN_USER_PASSWORD
        };
        // sorting orders
        String sortOrder =
                UsersEntry.COLUMN_USER_NAME + " ASC";
        List<AppUser> userList = new ArrayList<AppUser>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        Cursor cursor = db.query(UsersEntry.TABLE_NAME, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order

        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                AppUser user = new AppUser();
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(UsersEntry._ID))));
                user.setName(cursor.getString(cursor.getColumnIndex(UsersEntry.COLUMN_USER_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(UsersEntry.COLUMN_USER_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(UsersEntry.COLUMN_USER_PASSWORD)));
                // Adding user record to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return userList;
    }

    /**
     * This method is to fetch all trains
     *
     * @return list
     */
    public List<TrainType> getAllTrains() {
        // array of columns to fetch
        String[] columns = {
                UsersEntry._ID,
                UsersEntry.COLUMN_TRAINS_NAME,
                UsersEntry.COLUMN_TRAINS_TYPE,
                UsersEntry.COLUMN_TRAINS_DURATION,
                UsersEntry.COLUMN_TRAINS_PRICE,
                UsersEntry.COLUMN_TRAINS_SERVICE
        };

        // sorting orders
        String sortOrder =
                UsersEntry.COLUMN_TRAINS_NAME + " ASC";
        List<TrainType> trainList = new ArrayList<TrainType>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        Cursor cursor = db.query(TABLE_NAME_TRAINS, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order

        if (cursor.moveToFirst()) {
            do {
                TrainType train = new TrainType();
                train.setName(cursor.getString(cursor.getColumnIndex(UsersEntry.COLUMN_TRAINS_NAME)));
                train.setType(cursor.getString(cursor.getColumnIndex(UsersEntry.COLUMN_TRAINS_TYPE)));
                train.setDuration(cursor.getString(cursor.getColumnIndex(UsersEntry.COLUMN_TRAINS_DURATION)));
                train.setPrice(cursor.getString(cursor.getColumnIndex(UsersEntry.COLUMN_TRAINS_PRICE)));
                train.setService(cursor.getString(cursor.getColumnIndex(UsersEntry.COLUMN_TRAINS_SERVICE)));

                // Adding train record to list
                trainList.add(train);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return trainlist
        return trainList;
    }


    /**
     * This method to update user record
     *
     * @param user
     */
    public void updateUser(AppUser user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UsersEntry.COLUMN_USER_NAME, user.getName());
        values.put(UsersEntry.COLUMN_USER_EMAIL, user.getEmail());
        values.put(UsersEntry.COLUMN_USER_PASSWORD, user.getPassword());

        // updating row
        db.update(UsersEntry.TABLE_NAME, values, UsersEntry._ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    /**
     * This method to update train record
     *
     * @param train
     */
    public void updateTrain(TrainType train) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UsersEntry.COLUMN_TRAINS_NAME, train.getName());
        values.put(UsersEntry.COLUMN_TRAINS_TYPE, train.getType());
        values.put(UsersEntry.COLUMN_TRAINS_DURATION, train.getDuration());
        values.put(UsersEntry.COLUMN_TRAINS_PRICE, train.getPrice());
        values.put(UsersEntry.COLUMN_TRAINS_SERVICE, train.getService());

        // updating row
        db.update(TABLE_NAME_TRAINS, values, UsersEntry._ID + " = ?",
                new String[]{String.valueOf(train.getName())});
        db.close();
    }


    /**
     * This method is to delete user record
     *
     * @param user
     */
    public void deleteUser(AppUser user) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(UsersEntry.TABLE_NAME, UsersEntry._ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }


    /**
     * This method is to delete train record
     *
     * @param train
     */
    public void deleteTrain(TrainType train) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_NAME_TRAINS, UsersEntry._ID + " = ?",
                new String[]{String.valueOf(train.getName())});
        db.close();
    }

    /**
     * This method to check user exist or not based on the user name
     *
     * @param login
     * @return true/false
     */
    public boolean checkUser(String login) {

        // array of columns to fetch
        String[] columns = {
                UsersEntry._ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = UsersEntry.COLUMN_USER_LOGIN + " = ?";
        // selection argument
        String[] selectionArgs = {login};

        // query user table with condition

        Cursor cursor = db.query(UsersEntry.TABLE_NAME, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    /**
     * Method to check whether the user exists, its login and password
     *
     * @param
     * @param password
     * @return true/false
     */
    public boolean checkUser(String login, String password) {

        // array of columns to fetch
        String[] columns = {
                UsersEntry._ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = UsersEntry.COLUMN_USER_LOGIN + " = ?" + " AND " + UsersEntry.COLUMN_USER_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {login, password};

        // query user table with conditions
        Cursor cursor = db.query(UsersEntry.TABLE_NAME, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    /**
     * Hardcoded trains for the app
     *
     * @param database
     */

    public void hardcodeValues(SQLiteDatabase database) {
        //database = getWritableDatabase();

        ContentValues initialValues = new ContentValues();
        initialValues.put("name", "UTU1");
        initialValues.put("type", "fast");
        initialValues.put("duration", "2 hours");
        initialValues.put("price", "20 euros");
        initialValues.put("service", "food, familyplace, wifi");

        database.insert(UsersEntry.TABLE_NAME_TRAINS, null, initialValues);
        initialValues.put("name", "UTU2");
        initialValues.put("type", "slow");
        initialValues.put("duration", "5 hours");
        initialValues.put("price", "30 euros");
        initialValues.put("service", "wifi");

        database.insert(UsersEntry.TABLE_NAME_TRAINS, null, initialValues);
        initialValues.put("name", "UTU3");
        initialValues.put("type", "slow");
        initialValues.put("duration", "5 hours");
        initialValues.put("price", "30 euros");
        initialValues.put("service", "wifi");

        database.insert(UsersEntry.TABLE_NAME_TRAINS, null, initialValues);
        initialValues.put("name", "UTU4");
        initialValues.put("type", "slow");
        initialValues.put("duration", "5 hours");
        initialValues.put("price", "30 euros");
        initialValues.put("service", "wifi");

        database.insert(UsersEntry.TABLE_NAME_TRAINS, null, initialValues);
        initialValues.put("name", "UTU5");
        initialValues.put("type", "slow");
        initialValues.put("duration", "5 houts");
        initialValues.put("price", "30 euros");
        initialValues.put("service", "wifi");

        database.insert(UsersEntry.TABLE_NAME_TRAINS, null, initialValues);
        initialValues.put("name", "UTU6");
        initialValues.put("type", "fast");
        initialValues.put("duration", "5 hours");
        initialValues.put("price", "20 euros");
        initialValues.put("service", "wifi");

        database.insert(UsersEntry.TABLE_NAME_TRAINS, null, initialValues);
    }

    public void hardcodeTickets(SQLiteDatabase database) {
        ContentValues initialValues = new ContentValues();
        initialValues.put("info", "Date: 20.4.2018, From Helsinki to Turku, UTU2");

        database.insert(UsersEntry.TABLE_NAME_TICKETS, null, initialValues);
    }

    /**
     * Method to hardcode the admin user
     *
     * @param database
     */
    public void hardcodeAdmin(SQLiteDatabase database) {
        ContentValues initialValues = new ContentValues();
        initialValues.put("name", "admin");
        initialValues.put("login", "admin");
        initialValues.put("password", "1111");
        initialValues.put("lastname", "admin");
        initialValues.put("email", "admin");
        initialValues.put("address", "admin");
        initialValues.put("card", "00000");

        database.insert(UsersEntry.TABLE_NAME, null, initialValues);
    }

}