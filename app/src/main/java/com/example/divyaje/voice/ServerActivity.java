package com.example.divyaje.voice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class ServerActivity extends AppCompatActivity {

    public String serverIP;
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
        if(mEdit.getText().toString()!="")
            getIP();
        else Toast.makeText(this, "Please enter valid IP", Toast.LENGTH_SHORT).show();
    }

    public void getIP(){

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                serverIP = mEdit.getText().toString();
                Log.v("ServerIP : ", serverIP);
                Intent intent = new Intent(ServerActivity.this, MainActivity.class);
                intent.putExtra("ServerIP", serverIP);
                startActivity(intent);
            }
        });
    }
}
