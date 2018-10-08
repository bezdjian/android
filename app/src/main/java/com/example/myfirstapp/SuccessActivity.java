package com.example.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
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

        TextView textViewID = (TextView) findViewById(R.id.textViewID);
        TextView textViewFullname = (TextView) findViewById(R.id.textViewFullname);
        TextView textViewSubject = (TextView) findViewById(R.id.textViewSubject);
        TextView textViewCreated = (TextView) findViewById(R.id.textViewCreated);
        TextView textViewSubjectDescription = (TextView) findViewById(R.id.textViewSubjectDescription);

        Intent extras = getIntent();
        if(extras != null){
            try{
                String value = extras.getStringExtra("result");
                JSONObject jsonObject = new JSONObject(value);

                textViewID.setText("ID: " + jsonObject.getString("id"));
                textViewFullname.setText("Fullname: " + jsonObject.getString("firstname") + " " + jsonObject.getString("lastname"));
                textViewSubject.setText("Email: " + jsonObject.getString("email"));
                textViewCreated.setText("Role: " + jsonObject.getString("role"));
                textViewSubjectDescription.setText("Country: " + jsonObject.getString("country"));
            }catch(JSONException e){
                e.printStackTrace();
            }

        }else{
            Log.v("Bundle", "----- Extras is null -----");
        }
    }
}
