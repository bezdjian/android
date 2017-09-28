package com.example.myfirstapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public class MainActivity extends AppCompatActivity {
    //localhost is for Android emulator, 10.0.2.2 is for the computer's localhost.
    static String MY_REST_API_URL = "http://10.0.2.2:8090/rest/student/";
    //static String MY_REST_API_URL = "http://10.0.2.2:8080/test.php";

    private TextView hello;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText firstname = (EditText) findViewById(R.id.firstname);
        final EditText lastname = (EditText) findViewById(R.id.lastname);
        hello = (TextView) findViewById(R.id.hello);
        final Button okButton = (Button) findViewById(R.id.button);
        okButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                hello.setText(firstname.getText()  + " " + lastname.getText());
                String first = firstname.getText().toString();
                String last = lastname.getText().toString();
                new AjaxRequestTask().execute(first, last);
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

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //this method will be running on UI thread untill Ajax call is done.
            //progressBar = (ProgressBar) findViewById(R.id.progressBar);
            Log.v("progress", "HI!");
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            //Do AJAX Request
            try {
                // Enter URL address where your file resides
                url = new URL(MY_REST_API_URL + "/add/?name="+params[0]+"&subject="+params[1]);

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST"); // Getting 405 responseCode here..
                conn.setRequestProperty("Accept-Charset", "UTF-8");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                //conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("name", params[0])
                        .appendQueryParameter("subject", params[1]);
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();
                Log.v("response", "conn.getResponseMessage(): --- " + conn.getResponseMessage() + ":" + conn.getResponseCode());

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
                return "exception Malformed";
            } catch (ProtocolException e) {
                e.printStackTrace();
                return "exception Protocol";
            } catch (IOException e) {
                e.printStackTrace();
                return "exception IO";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);
            Log.v("Result", "OUT OF IF - RESULT: " + result);
            //result is the string text coming from the api function we called.
            if(result.contains("IO")){
                MainActivity.this.hello.setText(R.string.ioerror);
            }else if(result.contains("protocol")) {
                //MainActivity.this.hello.setText(R.string.error);
                MainActivity.this.hello.setText(R.string.protocolerror);
            }else if(result.contains("malformed")) {
                //MainActivity.this.hello.setText(R.string.error);
                MainActivity.this.hello.setText(R.string.malformederror);
            }else{
                //new Intent, puts data in so you can get it from the other activity.
                Intent intent = new Intent(MainActivity.this, SuccessActivity.class);
                //Send result to SuccessActivity
                try{
                    JSONObject json = new JSONObject(result);
                    intent.putExtra("result", json.toString());
                    startActivity(intent);
                    MainActivity.this.finish();
                }catch (JSONException e){
                    e.printStackTrace();
                    Log.v("JSON", "JSON Exception");
                }

            }
        }

    }
}
