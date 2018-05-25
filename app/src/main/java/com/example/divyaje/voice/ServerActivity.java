package com.example.divyaje.voice;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import static android.widget.Toast.LENGTH_SHORT;

public class ServerActivity extends AppCompatActivity {

    public String serverIP;
    public String result;
    ImageButton mButton;
    EditText mEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        mButton = findViewById(R.id.button2);
        mEdit = findViewById(R.id.serverInput);
//        if(getIntent().getStringArrayExtra("InvalidIP")) {
//            String errMsg = getIntent().getStringExtra("InvalidIP");
//        }
//        Toast.makeText(this, errMsg, LENGTH_SHORT).show();
        getIP();
    }

    public void getIP(){

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                serverIP = mEdit.getText().toString();
                Log.e("ServerIP : ", serverIP);
                new HttpGetRequest().execute();
            }
        });
    }

    /*  Helper method to show the toast message */
    void showToastMessage(String message){
        Toast.makeText(this, message, LENGTH_SHORT).show();
    }

    public class HttpGetRequest extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;
        @Override
        protected String doInBackground(String... params){
            //String stringUrl = params[0];

            String inputLine;
            String url = "http://" + serverIP + ":5000/ ";
            Log.e("url",url);
            try {
                //Create a URL object holding our url
                URL myUrl = new URL(url);

                //Create a connection
                HttpURLConnection connection =(HttpURLConnection)
                        myUrl.openConnection();
                //Set methods and timeouts
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);

                //Connect to our url
                connection.connect();
                //Create a new InputStreamReader
                InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
                int responseCode = connection.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK) {
                    //Create a new buffered reader and String Builder
                    BufferedReader reader = new BufferedReader(streamReader);
                    StringBuilder stringBuilder = new StringBuilder();
                    Intent intent = new Intent(ServerActivity.this, MainActivity.class);
                    intent.putExtra("ServerIP", serverIP);
                    startActivity(intent);
                    //Check if the line we are reading is not null
                    if((inputLine = reader.readLine()) == null){
                        Log.e("blank","wrong ip");
                        showToastMessage("Wrong IP");
                        //Intent i = new Intent(ServerActivity.this, ServerActivity.class);
                        // startActivity(i);
                    }
                    while ((inputLine = reader.readLine()) != null) {
                        stringBuilder.append(inputLine);
                    }
                    //Close our InputStream and Buffered reader
                    reader.close();
                    streamReader.close();
                    //Set our result equal to our stringBuilder
                    result = stringBuilder.toString();
                    Log.e("res", result);
                }
            }
            catch (SocketTimeoutException e)
            {
                e.printStackTrace();
                result = "error";
                Log.e("error1",e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ServerActivity.this, "Invalid IP", Toast.LENGTH_LONG).show();
                    }
                });
            }
            catch(IOException e){
                e.printStackTrace();
                result = "error";
                Log.e("error2",e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ServerActivity.this, "Invalid IP", Toast.LENGTH_LONG).show();
                    }
                });
            }

            if(result == null)
            {
                Log.e("wrong","wrong");
/*
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ServerActivity.this, "Invalid IP", Toast.LENGTH_LONG).show();
                    }
                });
*/
            }

            return result;
        }
        protected void onPostExecute(String result){
            super.onPostExecute(result);

        }
    }


}