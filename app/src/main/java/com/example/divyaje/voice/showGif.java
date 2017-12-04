package com.example.divyaje.voice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.VideoView;

public class showGif extends AppCompatActivity {

    private TextView text1;
    private VideoView vid1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_gif);

        text1 = findViewById(R.id.textView);
        vid1 = findViewById(R.id.videoView);

        String url = getIntent().getStringExtra("URL");
        String qText = getIntent().getStringExtra("qText");

        text1.setText(qText);

    }
}
