package com.example.divyaje.voice;

import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;


public class MainActivity extends AppCompatActivity {
    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1001;
    public String textMatch;
    public String result;
    public String text;
    private ListView mlvTextMatches;
//    private Spinner msTextMatches;
    private ImageButton mbtSpeak;
    private String promptText = "Speak now";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//      metTextHint = findViewById(R.id.etTextHint);
        mlvTextMatches = findViewById(R.id.lvTextMatches);
//        msTextMatches = findViewById(R.id.sNoofMatches);
        mbtSpeak = findViewById(R.id.btspeak);
        checkVoiceRecognition();
    }

    public void checkVoiceRecognition() {
        /*Check if voice recognition is present*/
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() == 0) {
            mbtSpeak.setEnabled(false);
//          mbtSpeak.setText("Voice recognizer not present");
            Toast.makeText(this, "Voice recognizer not present",
                    LENGTH_SHORT).show();
        }
    }


    public void speak(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        // Specify the calling package to identify your application
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass()
                .getPackage().getName());

        // Display an hint to the user about what he should say.
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, promptText);

        // Given an hint to the recognizer about what the user is going to say
        //There are two form of language model available
        //1.LANGUAGE_MODEL_WEB_SEARCH : For short phrases
        //2.LANGUAGE_MODEL_FREE_FORM  : If not sure about the words or phrases and its domain.
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);

        int noOfMatches = 3;
        // Specify how many results you want to receive. The results will be
        // sorted where the first result is the one with higher confidence.
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, noOfMatches);
        //Start the Voice recognizer activity for the result.
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    /*  Helper method to show the toast message */
    void showToastMessage(String message){
        Toast.makeText(this, message, LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE)

            //If Voice recognition is successful then it returns RESULT_OK
            if (resultCode == RESULT_OK) {

                ArrayList<String> textMatchList = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                if (!textMatchList.isEmpty()) {

                    mlvTextMatches.setAdapter(new ArrayAdapter<String>(this,
                            android.R.layout.simple_list_item_1,
                            textMatchList) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);
                            TextView textView = view.findViewById(android.R.id.text1);
                            return view;
                        }


                    });

//                    }
                    //Result code for various error.
                } else if (resultCode == RecognizerIntent.RESULT_AUDIO_ERROR) {
                    showToastMessage("Audio Error");
                } else if (resultCode == RecognizerIntent.RESULT_CLIENT_ERROR) {
                    showToastMessage("Client Error");
                } else if (resultCode == RecognizerIntent.RESULT_NETWORK_ERROR) {
                    showToastMessage("Network Error");
                } else if (resultCode == RecognizerIntent.RESULT_NO_MATCH) {
                    showToastMessage("No Match");
                } else if (resultCode == RecognizerIntent.RESULT_SERVER_ERROR) {
                    showToastMessage("Server Error");
                }
                super.onActivityResult(requestCode, resultCode, data);

//              CHECK FROM HERE
                mlvTextMatches.setClickable(true);
                mlvTextMatches.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        Object o = mlvTextMatches.getItemAtPosition(position);
                        text = (String) o;
                        textMatch = text.replaceAll(" ","%20");


                        try {
                            new HttpGetRequest().execute();
                        } catch (Exception e) {
                            e.printStackTrace();
                            showToastMessage("Unable to connect to server. Try again later");
                        }

                    }
                });

            }
    }


    public class HttpGetRequest extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;
        @Override
        protected String doInBackground(String... params){
            //String stringUrl = params[0];
            String inputLine;
            String ip = getIntent().getStringExtra("ServerIP");

            // REMOVE IP CHECK IF PERMANENT IP ADDRESS
            if (ip == null){
//                showToastMessage("Unable to connect to Server. Reconnect");
                result = null;
                return result;
            }

            String url = "";
            // SPELL FEATURE : if user says spell
            if (textMatch.contains("spell")){
                url = "http://" + ip + ":5000/spell/"+textMatch;  // CHANGE THIS IF IP FIXED
            } else{
                // NORMAL TRANSLATION : English to ISL
                url = "http://" + ip + ":5000/engtoisl/"+textMatch;  // CHANGE THIS IF IP FIXED
            }

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
                    //Check if the line we are reading is not null
                    while ((inputLine = reader.readLine()) != null) {
                        stringBuilder.append(inputLine);
                    }
                    //Close our InputStream and Buffered reader
                    reader.close();
                    streamReader.close();
                    //Set our result equal to our stringBuilder
                    result = stringBuilder.toString();
                } else {
                    showToastMessage("Invalid IP address");
                    return null;
                }
            } catch(SocketTimeoutException e){
                e.printStackTrace();
                Log.e("error", e.toString());
                result = null;
                return null;
            } catch(IOException e){
                e.printStackTrace();
                Log.e("error",e.toString());
                result = null;
                return null;
            }
            Log.e("result",result);

            if(result.equals("0")){
                Log.e("error", result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Oops! Unable to translate", Toast.LENGTH_LONG).show();
                    }
                });
                return result;
            } else if(!result.isEmpty())
            {
                Log.e("S",result);
                Log.e("E",text);
                Intent intent = new Intent(getApplicationContext(), showGif.class);
                intent.putExtra("english", text);
                //intent.putExtra("url" , sign);
                intent.putExtra("sign",result);
                startActivity(intent);
           }
           return result;
        }
        protected void onPostExecute(String result){
            super.onPostExecute(result);

        }
    }
}