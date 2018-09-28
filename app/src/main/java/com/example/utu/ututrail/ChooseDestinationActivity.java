/**
 * TO DO:
 * we need to allow user to choose the date of travelling and
 * to pass that date to the next activity (Choose Train)
 */
//todo: make the date of travelling and ammount of sits work
package com.example.utu.ututrail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * The page that allows user to choose from what city he travels
 * to what, choose data and time as well as the amount of tickets
 */


public class ChooseDestinationActivity extends AppCompatActivity implements TimePickerFragment.TimeDialogListener {

    // Declaring variables that will be passed to ChooseTrainActivity
    public static final String EXTRA_MESSAGE = "com.example.utu.ututrail.MESSAGE";

    private String fromWhere = "";
    private String where = "";
    private String dateOfTravelStr = "";
    private String numberOfTicketsStr = "";
    private static final String DIALOG_TIME = "ChooseDestinationActivity.TimeDialog";
    private Button timePickerAlertDialog;
    private String timeTravel = "";

    /**
     * creating choose_destination activity that contains 2 drop-down menus (spinners)
     * with the options of the cities. The list of the cities is in strings.xml
     * <p>
     * after clicking the submit button, the choises from the spinners are passed to the next
     * activity
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.choose_destination);
        final DatePicker simpleDatePicker = (DatePicker) findViewById(R.id.simpleDatePicker); // initiate a date picker

        timeTravel = "Ei valittu";

        //Spinners for departure and destination cities
        final Spinner spinnerFromWhere = (Spinner) findViewById(R.id.cities_spinner_mista);
        final Spinner spinnerWhere = (Spinner) findViewById(R.id.cities_spinner_mihin);

        //since they contain the same list of cities we call a function that creates identical
        //spinners
        destination(spinnerFromWhere);
        destination(spinnerWhere);

        //spinner for the amount of tickets
        final Spinner numberOfSeats = (Spinner) findViewById(R.id.number_of_tickets);
        ticketsSpinner(numberOfSeats);

        //time picker dialog that allows to choose time
        timePickerAlertDialog = (Button) findViewById(R.id.alert_dialog_time_picker);
        timePickerAlertDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment dialog = new TimePickerFragment();
                dialog.show(getSupportFragmentManager(), DIALOG_TIME);
            }
        });

        //Submit button
        Button submit = (Button) findViewById(R.id.submit_varaus);

        /**
         * when we click the submit button, take the user's choices
         * from the drop-down menus(spinners), time, date and amount of tickets and create a message out of it
         * that we pass to the next activity.
         * By clicking the submit button we also start new activity
         */
        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //creating the message
                final int day = simpleDatePicker.getDayOfMonth(); // get the selected day of the month

                // creating spinners
                final int month = simpleDatePicker.getMonth(); // get the selected month
                final int year = simpleDatePicker.getYear(); // get the selected year
                dateOfTravelStr = String.valueOf(day) + "." + String.valueOf(month) + "." + String.valueOf(year);

                // departure and destination
                fromWhere = String.valueOf(spinnerFromWhere.getSelectedItem());
                where = String.valueOf(spinnerWhere.getSelectedItem());

                //number of tickets
                numberOfTicketsStr = String.valueOf(numberOfSeats.getSelectedItem());

                //message that we pass to the next activity
                String message = "Mistä: " + fromWhere + ", mihin: " + where
                        + "\n" + "Päivä: " + dateOfTravelStr +
                        "\n" + "Lippujen määrä: " + numberOfTicketsStr
                        + "\n" + "Aika: " + timeTravel;

                Intent intent = new Intent(ChooseDestinationActivity.this, TripSummaryActivity.class);
                intent.putExtra(EXTRA_MESSAGE, message);

                //starting new activity and passing intent
                startActivity(intent);
            }

        });

    }


    /**
     * function that creates spinners of destination and departure
     *
     * @param spinner
     */
    public void destination(Spinner spinner) {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.cities_array, android.R.layout.simple_spinner_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }


    /**
     * spinner for the amount of tickets
     *
     * @param spinner
     */
    public void ticketsSpinner(Spinner spinner) {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.tickets_array, android.R.layout.simple_spinner_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }


    /**
     * Creating menu that allows to go to the admin view
     *
     * @param menu
     * @return
     */
    @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.admin:
                //todo: send here to the muokkaus of this page
                Intent intent = new Intent(ChooseDestinationActivity.this, AdminActivity.class);
                startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onFinishDialog(String time) {
        Toast.makeText(this, "Valittu aika : " + time, Toast.LENGTH_SHORT).show();
        timeTravel = time;
    }

}





