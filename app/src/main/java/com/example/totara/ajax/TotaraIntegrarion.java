package com.example.totara.ajax;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by harout on 2017-10-24.
 */
public class TotaraIntegrarion{

    static final int CONNECTION_TIMEOUT=10000;
    static final int READ_TIMEOUT=15000;
    private static final String REST_API_URL = "https://start.learning.nu";
    private static final String REST_API_RESOURCE = "/webservice/rest/server.php?";
    private static final String REST_API_REST_FORMAT = "moodlewsrestformat=json";
    // This is for the local WS functions by the name of Android
    private static final String REST_API_TOKEN_LOCAL = "wstoken=1AROBKTyOkh0fPDkflV78O2yQ5eI4lvQ";
    // This is for the core WS functions by the name of WS Android
    private static final String REST_API_TOKEN_CORE = "wstoken=hHQadSfScFunOTn2tWC0bAdCFc2a2Ak0";

    //FUNCTIONS
    private static final String REST_API_FUNCTION_AUTHENTICATE = "wsfunction=local_android_services_auth_user";
    private static final String REST_API_FUNCTION_USER_COURSES = "wsfunction=core_enrol_get_users_courses";

    private static HttpURLConnection conn;
    private static URL url = null;

    public static String authenticate(String username, String password){
        try{
            url = new URL(REST_API_URL + REST_API_RESOURCE + "&username="+username+"&password="+password+"&"+REST_API_TOKEN_LOCAL+"&"+REST_API_FUNCTION_AUTHENTICATE+"&"+REST_API_REST_FORMAT);
            Log.v("doInBackground URL", url.toString());

            // Setup HttpURLConnection class to send and receive data from php and mysql
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST"); // Make sure you have method = RequestMethod.POST on your API service.
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            // setDoInput and setDoOutput method depict handling of both send and receive
            //conn.setDoInput(true);
            //conn.setDoOutput(true);
            conn.connect();
            Log.v("response--------", "conn.getResponseMessage(): --- " + conn.getResponseMessage() + ":" + conn.getResponseCode());

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
                return("unsuccessful ???????????? " + response_code);
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


    public static String getUserCourses(int id){
        try{
            url = new URL(REST_API_URL + REST_API_RESOURCE + "&userid="+id+"&"+REST_API_TOKEN_CORE+"&"+REST_API_FUNCTION_USER_COURSES+"&"+REST_API_REST_FORMAT);
            Log.v("doInBackground URL", url.toString());

            // Setup HttpURLConnection class to send and receive data from php and mysql
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST"); // Make sure you have method = RequestMethod.POST on your API service.
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            // setDoInput and setDoOutput method depict handling of both send and receive
            //conn.setDoInput(true);
            //conn.setDoOutput(true);
            conn.connect();
            Log.v("response--------", "conn.getResponseMessage(): --- " + conn.getResponseMessage() + ":" + conn.getResponseCode());

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
                return("unsuccessful ???????????? " + response_code);
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


}
