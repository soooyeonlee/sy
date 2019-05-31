package com.example.ip_inventory;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;

    // URL to get contacts JSON
    //private static String url = "READ";

    ArrayList<HashMap<String, String>> contactList;

    private String id, name, designation, salary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        contactList = new ArrayList<>();

        lv = (ListView) findViewById(R.id.list);

        new Handler().execute();

        // OnItem Click
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Employee employee = (Employee) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(ViewActivity.this, EditActivity.class);

                id = ((TextView) view.findViewById(R.id.id)).getText().toString();
                name = ((TextView) view.findViewById(R.id.name)).getText().toString();
                designation = ((TextView) view.findViewById(R.id.designation)).getText().toString();
                salary = ((TextView) view.findViewById(R.id.salary)).getText().toString();




//                String id = employee.getId();
//                String name = employee.getName();
//                String designation = employee.getDesignation();
//                String salary = employee.getSalary();

                intent.putExtra("ID", id);
                intent.putExtra("NAME", name);
                intent.putExtra("DESIGNATION", designation);
                intent.putExtra("SALARY", salary);

                startActivity(intent);
            }
        });
    }

    public void addEmployee(View view) {
        Intent intent = new Intent(ViewActivity.this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class Handler extends AsyncTask<Void, Void, Void> {

        private ListAdapter adapter;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(ViewActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            JsonParser sh = new JsonParser();

            // Making a request to url and getting response
            String jsonStr = sh.convertJson(Constant.READ);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray employeeArray = jsonObj.getJSONArray("result");

                    // looping through All Contacts
                    for (int i = 0; i < employeeArray.length(); i++) {
                        JSONObject c = employeeArray.getJSONObject(i);

                        String id = c.getString("id");
                        String name = c.getString("name");
                        String designation = c.getString("designation");
                        String salary = c.getString("salary");

                        // Phone node is JSON Object
//                        JSONObject phone = c.getJSONObject("phone");
//                        String mobile = phone.getString("mobile");
//                        String home = phone.getString("home");
//                        String office = phone.getString("office");

                        // tmp hash map for single contact
                        HashMap<String, String> employee = new HashMap<>();

                        // adding each child node to HashMap key => value
                        employee.put("id", id);
                        employee.put("name", name);
                        employee.put("designation", designation);
                        employee.put("salary", salary);

                        // adding contact to contact list
                        contactList.add(employee);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
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
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    ViewActivity.this, contactList,
                    R.layout.list_item, new String[]{"id", "name", "designation",
                    "salary"}, new int[]{R.id.id, R.id.name,
                    R.id.designation, R.id.salary});

            lv.setAdapter(adapter);


        }

    }

}


