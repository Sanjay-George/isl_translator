package com.example.divyaje.voice;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;


public class MainActivity extends Activity {
    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1001;

    private EditText metTextHint;
    public ListView mlvTextMatches;
    private Spinner msTextMatches;
    private Button mbtSpeak;
    public String textMatch;
    private DatabaseReference mDB;



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        metTextHint = findViewById(R.id.etTextHint);
        mlvTextMatches = (ListView) findViewById(R.id.lvTextMatches);
        msTextMatches = findViewById(R.id.sNoofMatches);
        mbtSpeak = findViewById(R.id.btspeak);
        checkVoiceRecognition();

        /*Initialize database reference*/
        mDB = FirebaseDatabase.getInstance().getReference();

    }

    public void checkVoiceRecognition() {
        // Check if voice recognition is present
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() == 0) {
            mbtSpeak.setEnabled(false);
            mbtSpeak.setText("Voice recognizer not present");
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
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, metTextHint.getText()
                .toString());

        // Given an hint to the recognizer about what the user is going to say
        //There are two form of language model available
        //1.LANGUAGE_MODEL_WEB_SEARCH : For short phrases
        //2.LANGUAGE_MODEL_FREE_FORM  : If not sure about the words or phrases and its domain.
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);

        // If number of Matches is not selected then return show toast message
        if (msTextMatches.getSelectedItemPosition() == AdapterView.INVALID_POSITION) {
            Toast.makeText(this, "Please select No. of Matches from spinner",
                    LENGTH_SHORT).show();
            return;
        }

        int noOfMatches = Integer.parseInt(msTextMatches.getSelectedItem()
                .toString());
        // Specify how many results you want to receive. The results will be
        // sorted where the first result is the one with higher confidence.
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, noOfMatches);
        //Start the Voice recognizer activity for the result.
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE)

            //If Voice recognition is successful then it returns RESULT_OK
            if(resultCode == RESULT_OK) {

                ArrayList<String> textMatchList = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                if (!textMatchList.isEmpty()) {
                    // If first Match contains the 'search' word
                    // Then start web search.
                    if (textMatchList.get(0).contains("search")) {

                        String searchQuery = textMatchList.get(0);
                        searchQuery = searchQuery.replace("search","");
                        Intent search = new Intent(Intent.ACTION_WEB_SEARCH);
                        search.putExtra(SearchManager.QUERY, searchQuery);
                        startActivity(search);
                    } else {
                        // populate the Matches
                        mlvTextMatches
                                .setAdapter(new ArrayAdapter<String>(this,
                                        android.R.layout.simple_list_item_1,
                                        textMatchList));
                    }

                }
                //Result code for various error.
            }else if(resultCode == RecognizerIntent.RESULT_AUDIO_ERROR){
                showToastMessage("Audio Error");
            }else if(resultCode == RecognizerIntent.RESULT_CLIENT_ERROR){
                showToastMessage("Client Error");
            }else if(resultCode == RecognizerIntent.RESULT_NETWORK_ERROR){
                showToastMessage("Network Error");
            }else if(resultCode == RecognizerIntent.RESULT_NO_MATCH){
                showToastMessage("No Match");
            }else if(resultCode == RecognizerIntent.RESULT_SERVER_ERROR){
                showToastMessage("Server Error");
            }
        super.onActivityResult(requestCode, resultCode, data);

//         CHECK FROM HERE
        mlvTextMatches.setClickable(true);
        mlvTextMatches.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Object o = mlvTextMatches.getItemAtPosition(position);
                textMatch = (String)o;
//                mDB.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        Log.e("HELLO", "HI");
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        Log.e("Error message : ", "Error here 1");
//                    }
//                });
                Toast.makeText(getApplicationContext(), textMatch, LENGTH_SHORT).show();
                Log.e("Debug message : ", "Works here 1");
                fetchGif(textMatch);
            }
        });

    }

//    class translationData{
//
//    }

    private void fetchGif(String textMatch){

        Log.e("Debug message :", "Works here 2");

        mDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.e("DATA TYPE : ", dataSnapshot.getClass().getName());
                Log.e("THIS THING : ", dataSnapshot.getValue().toString());

                /// START DOING FROM HERE, PEOPLE OF THIS NATION
                // DON'T HESITATE
                // GO FOR IT
                // DO THIS.
                Object myOb = new Object();
                myOb = dataSnapshot.getValue();
                Log.e("Test1 : ", myOb.toString());


//                try {
//                    JSONObject test = new JSONObject((String) dataSnapshot.getValue());
//                    Log.e("ENGLISH", test.toString());
//                    HashMap<String, String> meMap = new HashMap<String, String>();
//                    meMap = dataSnapshot.getValue();
//                    Iterator myVeryOwnIterator = meMap.keySet().iterator();
//                    while(myVeryOwnIterator.hasNext()) {
//                        String key=(String)myVeryOwnIterator.next();
//                        String value=(String)meMap.get(key);
//
//                        Log.e("key : ", key);
//                        Log.e("value : ", value);
//
////                        Toast.makeText(getApplicationContext(), "Key: "+key+" Value: "+value, Toast.LENGTH_LONG).show();
//                    }
////
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

//                for(dataSnapshot.getChildren()){
//                    Log
//                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error message : ", "Error here 1");
            }
        });



//        Intent intent = new Intent(getApplicationContext(), showGif.class);
//
////        final String url = String.valueOf(new String[1]);
//        final String[] url = new String[]{""};
//
//        ValueEventListener valueEventListener = mDB.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                Log.e("Print this d :", "This shit got printed mofoofoda fodajifidjaf odajfidajf adofj doafjadofj iajf oidj af");
//
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.e("Errororororo : ", "Error happened dakjf adkfjad lfkjad;fjkad; fdakf;ja; fkj da");
//
//            }
//        });

//        mDB.addValueEventListener(new ValueEventListener(){
//
//            ("Print", "hello thea efihdf iad faidjf adifj adifjdaifj adijf iajdfijdaifjidajf idjifadifjiadjfa hey");
//
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
////                url[0] = dataSnapshot.getValue(String.class);
////                Log.e("E_Value", "Hello darkness my old friend");
////                url = (String) dataSnapshot.getValue();
////                Log.e("E_VALUE", "Data: " + child1);
//                Log.i("this shit" , dataSnapshot.getValue().toString());
//
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                System.out.println("The read failed: " + databaseError.getCode());
//
//            }
//        });
//        intent.putExtra("URL", url[0]);
//        intent.putExtra("qText", textMatch);
//        startActivity(intent );

    }
//
    /*  Helper method to show the toast message */
    void showToastMessage(String message){
        Toast.makeText(this, message, LENGTH_SHORT).show();
    }


}