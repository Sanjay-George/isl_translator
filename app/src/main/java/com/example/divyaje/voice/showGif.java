package com.example.divyaje.voice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.VideoView;


public class showGif extends AppCompatActivity {

//    private TextView text1;
//    private VideoView vid1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_gif);

        Log.e("Test message : ", "This works");

        TextView text1 = (TextView) findViewById(R.id.textView);
        VideoView video1 = (VideoView) findViewById(R.id.videoView);

        String url = getIntent().getStringExtra("url");
        String english = getIntent().getStringExtra("english");

        // DISPLAY
        text1.setText(english);



    }
}
