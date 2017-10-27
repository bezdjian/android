package com.example.totara;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.totara.ajax.TotaraIntegrarion;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Iterator;


public class MainActivity extends AppCompatActivity {
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText usernameInput = (EditText) findViewById(R.id.username);
        final EditText passwordInput = (EditText) findViewById(R.id.password);

        final Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String username = usernameInput.getText().toString();
                String password = passwordInput.getText().toString();
                new AjaxRequestTask().execute(username, password);
            }
        });

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private class AjaxRequestTask extends AsyncTask<String, String, String> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //this method will be running on UI thread untill Ajax call is done.
            //progressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            //Do AJAX Request
            String username = params[0];
            String password = params[1];
            return TotaraIntegrarion.authenticate(username, password);

        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);
            Context context = getApplicationContext();

            Log.v("Result-----------------", "OUT OF IF - RESULT: " + result);
            //result is the string text coming from the api function we called.
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
                    JSONObject jsonObject = new JSONObject(result);

                    Iterator<String> iterator = jsonObject.keys();
                    while(iterator.hasNext()) {
                        String key = iterator.next();
                        Object objValue = jsonObject.get(key);
                        Log.v("KEY----", key + "------ " + objValue.toString());
                        switch (key){
                            case "status":
                                if(!objValue.toString().equals("success")){
                                    Toast toast = Toast.makeText(context, R.string.invalidusernamepassword, Toast.LENGTH_LONG);
                                    toast.show();
                                }
                                break;
                            case "user":
                                //Send the user json to success..
                                JSONObject jsonObjectusers = new JSONObject(objValue.toString());
                                intent.putExtra("user", jsonObjectusers.toString());
                                startActivity(intent);
                                MainActivity.this.finish();
                                break;
                        }
                    }
                    /*JSONObject jsonObject = new JSONObject(result);

                    intent.putExtra("result", jsonObject.toString());
                    startActivity(intent);
                    MainActivity.this.finish();*/
                }catch (JSONException e){
                    e.printStackTrace();
                    Log.v("JSON--------", "JSON Exception");
                    Toast toast = Toast.makeText(context, R.string.unsuccessfull, Toast.LENGTH_LONG);
                    toast.show();
                }

            }
        }

    }
}
