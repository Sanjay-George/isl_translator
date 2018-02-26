package com.example.divyaje.voice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ServerActivity extends AppCompatActivity {

    public String serverIP;
    Button mButton;
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
                Log.v("ServerIP : ", serverIP);
                Intent intent = new Intent(ServerActivity.this, MainActivity.class);
                intent.putExtra("ServerIP", serverIP);
                startActivity(intent);
            }
        });
    }
}
