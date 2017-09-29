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
                JSONArray jsonArray = new JSONArray(value);

                JSONObject jsonObject;
                for(int i=0; i<jsonArray.length(); i++){
                    jsonObject = jsonArray.getJSONObject(i);
                    Iterator<String> iterator = jsonObject.keys();
                    while(iterator.hasNext()){
                        String key = iterator.next();
                        Object objValue = jsonObject.get(key);
                        Log.v("Row", key + "------ " + objValue.toString());

                        switch (key){
                            case "studentId":
                                textViewID.setText("ID: " + objValue);
                                break;
                            case "studentName":
                                textViewFullname.setText("Fullname: " + objValue);
                                break;
                            case "studentSubject":
                                textViewSubject.setText("Subject: " + objValue);
                                break;
                            case "dateAdded":
                                textViewCreated.setText("Created: " + objValue);
                                break;
                            case "subjectDescription":
                                textViewSubjectDescription.setText("Description: " + objValue);
                                break;
                        }
                    }
                }
            }catch(JSONException e){
                e.printStackTrace();
            }

        }else{
            Log.v("Bundle", "----- Extras is null -----");
        }
    }
}
