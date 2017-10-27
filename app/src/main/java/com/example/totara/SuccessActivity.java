package com.example.totara;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.totara.ajax.TotaraIntegrarion;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by harout on 2017-09-27.
 */

public class SuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.success);

        TextView textViewUsername = (TextView) findViewById(R.id.textViewUsername);
        TextView textViewFirstname = (TextView) findViewById(R.id.textViewFirstname);
        TextView textViewLastname = (TextView) findViewById(R.id.textViewLastname);
        TextView textViewEmail = (TextView) findViewById(R.id.textViewEmail);

        String userid = "";

        Intent extras = getIntent();
        if(extras != null){
            try{
                String value = extras.getStringExtra("user");
                JSONObject jsonObject = new JSONObject(value);

                Iterator<String> iterator = jsonObject.keys();
                while(iterator.hasNext()){
                    String key = iterator.next();
                    Object objValue = jsonObject.get(key);
                    Log.v("KEY----", key + "------ " + objValue.toString());

                    switch (key){
                        case "username":
                            textViewUsername.setText(objValue.toString());
                            break;
                        case "firstname":
                            textViewFirstname.setText(objValue.toString());
                            break;
                        case "lastname":
                            textViewLastname.setText(objValue.toString());
                            break;
                        case "email":
                            textViewEmail.setText(objValue.toString());
                            break;
                        case "id":
                            userid = objValue.toString();
                            break;
                    }
                }


                // Now get the user's courses.
                new AjaxRequestTask().execute(userid);


            }catch(JSONException e){
                e.printStackTrace();
            }

        }else{
            Log.v("Bundle", "----- Extras is null -----");
        }
    }

    private class AjaxRequestTask extends AsyncTask<String, String, String> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //this method will be running on UI thread untill Ajax call is done.
        }

        @Override
        protected String doInBackground(String... params) {
            //Do AJAX Request
            int userid = Integer.parseInt(params[0]);
            return TotaraIntegrarion.getUserCourses(userid);

        }

        @Override
        protected void onPostExecute(String result) {
            Context context = getApplicationContext();

            Log.v("Result,success activity", "OUT OF IF - RESULT: " + result);
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
                //Intent intent = new Intent(SuccessActivity.this, SuccessActivity.class);
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
