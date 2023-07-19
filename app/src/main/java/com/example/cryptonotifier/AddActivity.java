package com.example.cryptonotifier;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import utils.CustomListAdapter;
import utils.ListItem;

public class AddActivity extends AppCompatActivity {

    private static final String TAG = "AddActivity";
    private static final String API_URL = "https://www.binance.com/api/v3/ticker/price"; // Replace with your actual API URL

    private ArrayList<ListItem> originalItems = new ArrayList<>();
    private ArrayList<ListItem> filteredItems = new ArrayList<>();
    private CustomListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

//        ListView listView = findViewById(R.id.listView);
//
//        ArrayList<ListItem> items = new ArrayList<>();
//        items.add(new ListItem("Item 1", false));
//        items.add(new ListItem("Item 2", true));
//        items.add(new ListItem("Item 3", false));
//
//        CustomListAdapter adapter = new CustomListAdapter(this, R.layout.activity_add, items);
//        listView.setAdapter(adapter);

        ListView listView = findViewById(R.id.listView);
        adapter = new CustomListAdapter(AddActivity.this, R.layout.activity_add, filteredItems);
        listView.setAdapter(adapter);

        parseJsonData();

        SearchView searchView = findViewById(R.id.search_bar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                System.out.println("TextSubmit");
                // Perform search when the user submits the query (e.g., by pressing Enter)
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter the data based on the user's input
                System.out.println("TextChanged");
                Log.d(TAG, "Search query: " + newText);
                filterData(newText);
                return true;
            }
        });
    }

    private void filterData(String query) {
        filteredItems.clear();
        TextUtils.isEmpty("query");

        if (TextUtils.isEmpty(query)) {
            filteredItems.addAll(originalItems);
        } else {
            for (ListItem item : originalItems) {
                if (item.getText().contains(query)) {
                    filteredItems.add(item);
                }
            }
        }
        adapter.notifyDataChanged(filteredItems);
    }

    private boolean getSwitchStateFromSharedPreferences(String symbol) {
        SharedPreferences sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPrefs.getBoolean(symbol, false); // Default value is false if not found
    }

    private boolean getSwitchStateFromOriginalItems(String symbol) {
        for (ListItem item : originalItems) {
            if (item.getText().equals(symbol)) {
                return item.isSwitchState();
            }
        }
        return false; // Default value if the item is not found in originalItems
    }


    private void parseJsonData() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Create a JSON request using JsonArrayRequest.
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, API_URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Handle the JSON response
                        try {
                            ArrayList<ListItem> items = new ArrayList<>();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String symbol = jsonObject.getString("symbol");
                                // Check if the item already exists in originalItems, and retain its switch state
                                boolean switchState = getSwitchStateFromSharedPreferences(symbol);
                                items.add(new ListItem(symbol, switchState));
                            }

                            originalItems.clear();
                            originalItems.addAll(items);
                            filteredItems.clear();
                            filteredItems.addAll(items);
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AddActivity.this, "Error parsing JSON", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors here
                        Log.e(TAG, "Error: " + error.getMessage());
                        Toast.makeText(AddActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest);
    }
}