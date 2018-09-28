package com.example.utu.ututrail;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.utu.ututrail.data.RailContentContract.UsersEntry;


/**
 * adapter for showing the list of users.
 */

public class UserAdapter extends CursorAdapter {

    public UserAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView loginTextView = (TextView) view.findViewById(R.id.login_adapter);
        TextView nameTextView = (TextView) view.findViewById(R.id.name_adapter);
        TextView lastNameTextView = (TextView) view.findViewById(R.id.last_name_adapter);


        // Find the columns of attributes that we're interested in
        int loginColumnIndex = cursor.getColumnIndex(UsersEntry.COLUMN_USER_LOGIN);
        int nameColumnIndex = cursor.getColumnIndex(UsersEntry.COLUMN_USER_NAME);
        int lastNameColumnIndex = cursor.getColumnIndex(UsersEntry.COLUMN_USER_LASTNAME);
        int idColumnIndex = cursor.getColumnIndex(UsersEntry._ID);

        // Read the  attributes from the Cursor for the current item
        String userName = cursor.getString(nameColumnIndex);
        String userLastName = cursor.getString(lastNameColumnIndex);
        String userLogin = cursor.getString(loginColumnIndex);
        final int itemId = cursor.getInt(idColumnIndex);

        // Update the TextViews with the attributes for the current item
        nameTextView.setText(userName);
        lastNameTextView.setText(userLastName);
        loginTextView.setText(userLogin);


    }
}
