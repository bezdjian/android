package com.example.myfirstapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public class MainActivity extends AppCompatActivity {
    //localhost is for Android emulator, 10.0.2.2 is for the computer's localhost.
    //static String MY_REST_API_URL = "http://10.0.2.2:8090/rest/student/";
    static String MY_REST_API_URL = "http://10.0.2.2:8081/rest/user/";

    private ProgressBar progressBar;
    private String subjectSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText username = findViewById(R.id.username);
        final EditText pass = findViewById(R.id.password);
        //final AutoCompleteTextView autoCompleteTextView = findViewById(R.id.autoCompleteTextView);

        final String[] subjectsArray = {"Java","C#","Python","Swift","PHP"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,subjectsArray);

        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        // Make it like 100% width.
        /*autoCompleteTextView.setDropDownWidth(point.x);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(adapter);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                subjectSelected = adapterView.getAdapter().getItem(i).toString();
            }
        });*/

        final Button okButton = findViewById(R.id.button);
        okButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String first = username.getText().toString();
                String password = pass.getText().toString();
                //String subject = autoCompleteTextView.getText().toString();
                new AjaxRequestTask().execute(first, password, ""); //subject
            }
        });

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private class AjaxRequestTask extends AsyncTask<String, String, String> {

        static final int CONNECTION_TIMEOUT=10000;
        static final int READ_TIMEOUT=15000;
        HttpURLConnection conn;
        URL url = null;

        private String username = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //this method will be running on UI thread untill Ajax call is done.
            //progressBar = (ProgressBar) findViewById(R.id.progressBar);
            Log.i("onPreExecute", "HI!");
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            //Do AJAX Request
            try {
                // Enter URL address where your file resides
                username = params[0];
                String password = params[1];
                Log.i("doInBackground", "Username: " + username + " - Password: " + password);

                url = new URL(MY_REST_API_URL + "/userInfo/"+username+"/"+password);

                // Setup HttpURLConnection class to send and receive data.
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST"); // Make sure you have method = RequestMethod.POST on your API service.
                conn.setRequestProperty("Accept-Charset", "UTF-8");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                //conn.setDoOutput(true);
                conn.connect();
                Log.i("response", "conn.getResponseMessage(): --- " + conn.getResponseMessage() + ":" + conn.getResponseCode());

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {
                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    // Pass data to onPostExecute method
                    return(result.toString());

                }else{
                    //return("unsuccessful with response code: " + response_code);
                    return("unsuccessful");
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "malformed";
            } catch (ProtocolException e) {
                e.printStackTrace();
                return "protocol";
            } catch (IOException e) {
                e.printStackTrace();
                return "IO"; //e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);
            Context context = getApplicationContext();

            Log.v("Result", "OUT OF IF - RESULT: " + result);
            //result is the string text coming from doInBackground we called.
            if(result.contains("IO")){
                Toast toast = Toast.makeText(context, R.string.ioerror, Toast.LENGTH_LONG);
                toast.show();
            }else if(result.contains("protocol")) {
                Toast toast = Toast.makeText(context, R.string.protocolerror, Toast.LENGTH_LONG);
                toast.show();
            }else if(result.contains("malformed")) {
                Toast toast = Toast.makeText(context, R.string.malformederror, Toast.LENGTH_LONG);
                toast.show();
            }else{
                //new Intent, puts data in so you can get it from the other activity.
                Intent intent = new Intent(MainActivity.this, SuccessActivity.class);
                //Send result to SuccessActivity
                try{
                    //JSONArray jsonArray = new JSONArray(result);
                    JSONObject jsonObject = new JSONObject(result);

                    //intent.putExtra("result", jsonArray.toString());
                    intent.putExtra("result", jsonObject.toString());
                    startActivity(intent);
                    MainActivity.this.finish();
                }catch (JSONException e){
                    e.printStackTrace();
                    Log.v("JSON", "JSON Exception");
                    Toast toast = Toast.makeText(context, R.string.NOT_FOUND, Toast.LENGTH_LONG);
                    toast.show();
                }

            }
        }

    }
}
