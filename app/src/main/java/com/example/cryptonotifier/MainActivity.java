package com.example.cryptonotifier;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import utils.CustomListAdapter;
import utils.ListItem;
import utils.MyNotificationManager;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String API_URL = "https://www.binance.com/api/v3/ticker/price?symbol=ETHUSDT"; // Replace with your actual API URL

    private static final String CHANNEL_ID = "my_channel_id";
    private static final int NOTIFICATION_ID = 1;
    private MyNotificationManager myNotificationManager;
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        parseJsonData();

        // Initialize the Handler and Runnable
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                parseJsonData();
                handler.postDelayed(this, 10000); // Run the Runnable every 5 seconds
            }
        };

        // Start the periodic request after 5 seconds
        handler.postDelayed(runnable, 10000);
    }

//        ListView listView = findViewById(R.id.listView);
//
//        ArrayList<ListItem> items = new ArrayList<>();
//        items.add(new ListItem("Item 1", false));
//        items.add(new ListItem("Item 2", true));
//        items.add(new ListItem("Item 3", false));
//
//        CustomListAdapter adapter = new CustomListAdapter(this, R.layout.activity_main, items);
//        listView.setAdapter(adapter);

    public void addNewActivity(View v) {
        Intent intent = new Intent(this, AddActivity.class);
        startActivity(intent);
    }

    private void parseJsonData() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Create a JSON request using JsonObjectRequest since we are expecting a single JSON object.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, API_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Request!");
                        // Handle the JSON response
                        try {
                            String symbol = response.getString("symbol");
                            String price = response.getString("price");
                            String value = String.format("%s %s", symbol, price);

                            // Assuming ListItem class constructor takes two parameters (value and a boolean)
                            ArrayList<ListItem> items = new ArrayList<>();
                            items.add(new ListItem(value, false));

                            // Now you have the parsed data, set up the ListView with the custom adapter
                            ListView listView = findViewById(R.id.listView);
                            CustomListAdapter adapter = new CustomListAdapter(MainActivity.this, R.layout.activity_main, items);
                            listView.setAdapter(adapter);


                            MyNotificationManager.showNotification(MainActivity.this, symbol, price);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Error parsing JSON", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors here
                        Log.e(TAG, "Error: " + error.getMessage());
                        Toast.makeText(MainActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }
}

//public class MainActivity extends AppCompatActivity {
//
//    private static final String TAG = "MainActivity";
//    private static final String API_URL = "https://www.binance.com/api/v3/ticker/price?symbol=ETHUSDT"; // Replace with your actual API URL
//
//
//    private static final String CHANNEL_ID = "my_channel_id";
//    private static final int NOTIFICATION_ID = 1;
//    private MyNotificationManager myNotificationManager;
//    private Handler handler;
//    private Runnable runnable;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        parseJsonData();
//
//        // Initialize the Handler and Runnable
//        handler = new Handler();
//        runnable = new Runnable() {
//            @Override
//            public void run() {
//                parseJsonData(); // Call the method to fetch and parse JSON data
//                handler.postDelayed(this, 5000); // Run the Runnable every 5 seconds
//            }
//        };
//
//        // Start the periodic request after 5 seconds
//        handler.postDelayed(runnable, 1000);
//    }
//
////        ListView listView = findViewById(R.id.listView);
////
////        ArrayList<ListItem> items = new ArrayList<>();
////        items.add(new ListItem("Item 1", false));
////        items.add(new ListItem("Item 2", true));
////        items.add(new ListItem("Item 3", false));
////
////        CustomListAdapter adapter = new CustomListAdapter(this, R.layout.activity_main, items);
////        listView.setAdapter(adapter);
//
//    public void addNewActivity(View v) {
//        Intent intent = new Intent(this, AddActivity.class);
//        startActivity(intent);
//    }
//
//    private void parseJsonData() {
//        // Instantiate the RequestQueue.
//        RequestQueue queue = Volley.newRequestQueue(this);
//
//        // Create a JSON request using JsonArrayRequest.
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, API_URL, null,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        // Handle the JSON response
//                        try {
//                            ArrayList<ListItem> items = new ArrayList<>();
//
//                            for (int i = 0; i < response.length(); i++) {
//                                JSONObject jsonObject = response.getJSONObject(i);
//                                String symbol = jsonObject.getString("symbol");
//                                String price = jsonObject.getString("price");
//                                String value = String.format("%s %s", symbol, price);
//                                items.add(new ListItem(value, false));
//                            }
//
//                            // Now you have the parsed data, set up the ListView with the custom adapter
//                            ListView listView = findViewById(R.id.listView);
//                            CustomListAdapter adapter = new CustomListAdapter(MainActivity.this, R.layout.activity_main, items);
//                            listView.setAdapter(adapter);
//
////                            myNotificationManager.showNotification();
//
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Toast.makeText(MainActivity.this, "Error parsing JSON", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // Handle errors here
//                        Log.e(TAG, "Error: " + error.getMessage());
//                        Toast.makeText(MainActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//        // Add the request to the RequestQueue.
//        queue.add(jsonArrayRequest);
//    }
//}