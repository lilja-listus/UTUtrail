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
 * adapter for showing the list of my tickets.
 */

public class TicketsAdapter extends CursorAdapter {

    public TicketsAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_tickets, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView infoTextView = (TextView) view.findViewById(R.id.ticket_adapter);

        // Find the columns of attributes that we're interested in
        int infoColumnIndex = cursor.getColumnIndex(UsersEntry.COLUMN_TICKETS_INFO);

        // Read the  attributes from the Cursor for the current item
        String ticketInfo = cursor.getString(infoColumnIndex);

        // Update the TextViews with the attributes for the current item
        infoTextView.setText(ticketInfo);

    }
}
