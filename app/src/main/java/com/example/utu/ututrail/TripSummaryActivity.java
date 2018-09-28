package com.example.utu.ututrail;

/**
 * Activity that gets information about the trip from the ChooseDestination activity
 * and shows the summary of them
 *
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TripSummaryActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.utu.ututrail.MESSAGE";
    public static final String EXTRA_MESSAGE2 = "com.example.utu.ututrail.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_summary);

        TextView setTravelData = (TextView) findViewById(R.id.summary_of_ticket);

        //from ChooseDestinationActivity
        Intent intent = getIntent();
        final String message = intent.getStringExtra(ChooseDestinationActivity.EXTRA_MESSAGE);

        setTravelData.setText(message);
        final String messageExtra = message;
        Button continueToSeats = (Button) findViewById(R.id.continue_to_choosing_ticket);
        final Intent intentContinue = new Intent(TripSummaryActivity.this, TrainTypeOrder.class);

        continueToSeats.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                intentContinue.putExtra(EXTRA_MESSAGE2, messageExtra);
                startActivity(intentContinue);

            }

        });

        }
}
