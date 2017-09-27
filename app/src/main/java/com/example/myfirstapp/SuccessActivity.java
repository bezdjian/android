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

        TextView resultText = (TextView) findViewById(R.id.resultText);
        TextView jsonText = (TextView) findViewById(R.id.jsontext);

        Intent extras = getIntent();
        if(extras != null){
            try{
                String value = extras.getStringExtra("result");
                resultText.setText(value);

                JSONObject json = new JSONObject(value);
                Iterator<String> iterator = json.keys();
                String finalresult = "";
                while(iterator.hasNext()){
                    String key = iterator.next();
                    Object objValue = json.get(key);
                    Log.v("Row", key + "------ " + objValue.toString());
                    finalresult += key + ": " + objValue.toString() + "\n";
                }
                jsonText.setText(finalresult);
            }catch(JSONException e){
                e.printStackTrace();
            }

        }else{
            Log.v("Bundle", "----- Extras is null -----");
        }
    }
}
