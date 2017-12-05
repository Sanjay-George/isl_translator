package com.example.divyaje.voice;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;


public class showGif extends AppCompatActivity {

//    private TextView text1;
//    private VideoView vid1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_gif);

        Log.e("Test message : ", "This works");

        TextView text1 = (TextView) findViewById(R.id.textView);
        ImageView image1 = (ImageView) findViewById(R.id.imageView);

        String url = getIntent().getStringExtra("url");
        String english = getIntent().getStringExtra("english");

        // DISPLAY
        text1.setText(english);

        // DOWNLOAD IMG
        ImageDownloader task = new ImageDownloader();
        Bitmap image;

        try {
            image = task.execute(url).get();
            image1.setImageBitmap(image);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }

    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {

            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream input = connection.getInputStream();
                Bitmap img = BitmapFactory.decodeStream(input);
                return img;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

}
