package com.example.test_game;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.tvURL);
        imageView = findViewById(R.id.imPicture);
        Glide.with(this)
                .load("https://images.unsplash.com/photo-1534278931827-8a259344abe7?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&w=1000&q=80")
                .into(imageView);
//        Picasso.get()
//                .load("https://images.unsplash.com/photo-1534278931827-8a259344abe7?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&w=1000&q=80")
//                .into(imageView);
    textView.setText(String.format(getResources().getString(R.string.url_path), "https://images.unsplash.com/photo-1534278931827-8a259344abe7?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&w=1000&q=80"));
//        AsyncImageLoader asyncImageLoader = new AsyncImageLoader();
//        asyncImageLoader.execute("https://images.unsplash.com/photo-1534278931827-8a259344abe7?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&w=1000&q=80");
//        try {
//            imageView.setImageBitmap(asyncImageLoader.get());
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

    }
}

//class AsyncImageLoader extends AsyncTask<String,String,Bitmap>{
//
//    @Override
//    protected Bitmap doInBackground(String... strings) {
//        InputStream inputStream = null;
//        Bitmap bitmap;
//        HttpsURLConnection urlConnection = null;
//        try {
//            URL url = new URL(strings[0]);
//            urlConnection = (HttpsURLConnection) url.openConnection();
//            inputStream = urlConnection.getInputStream();
//            bitmap = BitmapFactory.decodeStream(inputStream);
//            return bitmap;
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }finally {
//            urlConnection.disconnect();
//            try {
//                inputStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }
//}
