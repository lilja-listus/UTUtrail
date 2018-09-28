package com.example.utu.ututrail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Activity that opens in the very end
 */


public class GoodByeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_bye);

        Button tikets = (Button) findViewById(R.id.check_liput);
        tikets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoodByeActivity.this, TicketsCatalog.class);
                startActivity(intent);
            }

        });

    }

}