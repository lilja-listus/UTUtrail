package com.example.utu.ututrail;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * The activity that allows the user to choose tickets and extra services for the
 * trip like place with a pet.
 *
 * The app gets the list of available tickets from server.
 *
 */


public class ChooseSeatActivity extends AppCompatActivity {

    // variables for our ticket
    private String chosenSeat = "0";
    private String TAG = ChooseSeatActivity.class.getSimpleName();
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_seat);
        final TableLayout chooseSeatTable = (TableLayout) findViewById(R.id.paikka);

        //the table is invisible until a carriage is chosen
        chooseSeatTable.setVisibility(View.INVISIBLE);

        /**
         * starts asynk task that gets data from server and works on it
         */
        new ChooseSeatActivity.GetContacts().execute();

        //Continue button
        Button continueToOrder = (Button) findViewById(R.id.continue_to_complete);
        continueToOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ChooseSeatActivity.this, PaymentActivity.class);
                startActivity(intent);
            }

        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.admin:
                Intent intent = new Intent(ChooseSeatActivity.this, AdminActivity.class);
                startActivity(intent);
        }
        switch (id) {
            case R.id.my_tickets:

                Intent intent = new Intent(ChooseSeatActivity.this, TicketsCatalog.class);
                startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * AsyncTask class that gets JSON data of tickets, shows only those that are available
     * (status is true). Allows the user to choose tickets.
     *
     */

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        List<Seat> seats = new ArrayList<>();
        private String chosenCarriage = "0";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            }

            @Override

            //Gets data in JSON and creates an array of seats
            protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String url = "http://qmiproducts.com/the_script.php";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    // Getting JSON Array node
                    JSONArray json = new JSONArray(jsonStr);
                    String seat = "Seat Not Available";
                    String status = "Status Not Available";
                    // looping through All Contacts

                    for (int i = 0; i < json.length(); i++) {
                        JSONObject c = json.getJSONObject(i);
                        seat = c.getString("Seat");
                        status = c.getString("Status");
                        Seat trainSeat = new Seat(seat, status);
                        seats.add(trainSeat);
                        //resultsSeats += "\n" + seat + ", " + status + ", " ;
                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            chosenCarriage = chooseCarriage();

            // This button reloads activity and allows to choose a new ticket
            Button addTicket = (Button) findViewById(R.id.add_ticket);
            addTicket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }

            });
        }

        /**
         * method to make a least of TextVies for seats. It goes through all data and uses only
         * those with the method "True"
         *
         * @param
         * @return seatViews
         */

        private void setUpTheVies(String chosenCarriage) {
            final TableLayout chooseSeatTable = (TableLayout) findViewById(R.id.paikka);
            TableLayout chooseVaunu = (TableLayout) findViewById(R.id.vaunu);

            String carriageIdentifier = "";
            if (chosenCarriage.equals("0")) {
                //     chosenData.setText("Make your choice");

            }

            //to choose the rigght carriage
            if (chosenCarriage.equals("1")) {
                carriageIdentifier = "A";
            }
            if (chosenCarriage.equals("2")) {
                carriageIdentifier = "B";
            }


            ArrayList<TextView> seatViews = new ArrayList<>();
            seatViews = defineViewsForSeats(seatViews);

            ArrayList<String> listOfSeats = new ArrayList<>();
            ArrayList<View> availableSeatsView = new ArrayList<>();

            for (int k = 0; k < seats.size(); k++) {
                Seat oneSeat = seats.get(k);
                if (oneSeat.getSeatNro().contains(carriageIdentifier)) {
                    if (oneSeat.getStatus().equals("True")) {

                        listOfSeats.add(oneSeat.getSeatNro());
                    }
                }
            }
            //the availavle tickets
            for (int m = 0; m < listOfSeats.size(); m++) {
                final TextView currentView = seatViews.get(m);
                currentView.setText(listOfSeats.get(m));
                availableSeatsView.add(seatViews.get(m));

            }

            makeSeatsClikable(availableSeatsView, listOfSeats);

        }

        /**
         * Method to define views for the seats and create an arraylist of them
         *
         * @param seatViews
         * @return seatViews
         */
        private ArrayList<TextView> defineViewsForSeats(ArrayList<TextView> seatViews) {
            TextView seat1 = (TextView) findViewById(R.id.seat1);
            TextView seat2 = (TextView) findViewById(R.id.seat2);
            TextView seat3 = (TextView) findViewById(R.id.seat3);
            TextView seat4 = (TextView) findViewById(R.id.seat4);
            TextView seat5 = (TextView) findViewById(R.id.seat5);
            TextView seat6 = (TextView) findViewById(R.id.seat6);
            TextView seat7 = (TextView) findViewById(R.id.seat7);
            TextView seat8 = (TextView) findViewById(R.id.seat8);
            TextView seat9 = (TextView) findViewById(R.id.seat9);
            TextView seat10 = (TextView) findViewById(R.id.seat10);

            seatViews.add(seat1);
            seatViews.add(seat2);
            seatViews.add(seat3);
            seatViews.add(seat4);
            seatViews.add(seat5);
            seatViews.add(seat6);
            seatViews.add(seat7);
            seatViews.add(seat8);
            seatViews.add(seat9);
            seatViews.add(seat10);
            return seatViews;

        }

        /**
         * The method defines a carriages and makes them clickable
         *
         * @return chosenCarriage
         */
        private String chooseCarriage() {

            //carriage
            final TextView carriage1 = (TextView) findViewById(R.id.vaunu1);
            final TextView carriage2 = (TextView) findViewById(R.id.vaunu2);
            final TableLayout chooseSeatTable = (TableLayout) findViewById(R.id.paikka);
            chooseSeatTable.setVisibility(View.INVISIBLE);

            //making carriages cliackable
            carriage1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<TextView> seatViews = new ArrayList<>();
                    seatViews = defineViewsForSeats(seatViews);
                    clearViews(seatViews);
                    chosenCarriage = "1";
                    onChosenCarriage(carriage1, chosenCarriage, carriage2, chooseSeatTable);
                    setUpTheVies(chosenCarriage);

                }
            });

            carriage2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<TextView> seatViews = new ArrayList<>();
                    seatViews = defineViewsForSeats(seatViews);
                    clearViews(seatViews);
                    chosenCarriage = "2";
                    onChosenCarriage(carriage2, chosenCarriage, carriage1, chooseSeatTable);
                    setUpTheVies(chosenCarriage);
                }
            });
            return chosenCarriage;

        }

        /**
         * Method that changes the background color of the chosen carriage into green and
         * if the other one was previously changed, it is changed back.
         *
         * The table with available tickets is becoming visible
         *
         * @param carriage
         * @param chosenCarriage
         * @param otherCarriage
         * @param chooseSeatTable
         */

        private void onChosenCarriage(TextView carriage, String chosenCarriage, TextView otherCarriage, TableLayout chooseSeatTable) {
            carriage.setBackgroundColor(0xFF00FF00);
            otherCarriage.setBackgroundColor(0xFFFFFF);
            Toast.makeText(ChooseSeatActivity.this, "Vaunu " + chosenCarriage, Toast.LENGTH_SHORT).show();
            chooseSeatTable.setVisibility(View.VISIBLE);

        }

        /**
         * The Method to clear the Views
         * @param seatViews
         */
        private void clearViews(ArrayList<TextView>seatViews) {
            for (int m = 0; m < 10; m++) {
                TextView currentView = seatViews.get(m);
                currentView.setText("");
            }
        }

        /**
         * The method goes through all available tickets, defines the carriage they belong to
         * and sets the info TextView to reflect the info about the chosen ticket
         *
         * @param availableSeatsView
         * @param listOfSeats
         */

        private void makeSeatsClikable(ArrayList <View> availableSeatsView, final ArrayList<String> listOfSeats) {
            final TextView setTravelData = findViewById(R.id.info);

            for (int z =0; z< availableSeatsView.size(); z++) {
                View currentViewHere = availableSeatsView.get(z);


                currentViewHere.setVisibility(View.VISIBLE);
                final String chosenSeat = listOfSeats.get(z);

                currentViewHere.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ChooseSeatActivity.this,  chosenSeat, Toast.LENGTH_SHORT).show();

                        String vaunu = "";
                        if (chosenSeat.contains("A")){
                            vaunu = "1";
                        }

                        if (chosenSeat.contains("B")){
                            vaunu = "2";
                        }
                        setTravelData.setText("Vaunu: " + vaunu + ",paikka: " + chosenSeat);

                    }
                });
            }
        }
    }

}