package com.example.test_game;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

public class Test_game extends AppCompatActivity {

    private ImageView image;
    private Button buttonAns1;
    private Button buttonAns2;
    private Button buttonAns3;
    private Button buttonAns4;

    private final String TAG = "Mytag";

    //counter how many question we answered
    private int currentQuestion;

    //current random posistionof button
    private int currentRightAnswerPosition;

    //current random index of right URL and Name
    private int currentQuestionPosition;

    private ArrayList<String> allNameDownloader;
    private ArrayList<String> allImageDownloader;
    private ArrayList<Button> arrayButtons;

    private String currentRightURL;
    private String currentRightName;

    private int rightAnswer = 0;
    private int wrongAnswer = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_game);

        image = findViewById(R.id.actor);
        buttonAns1 = findViewById(R.id.btnAnswer);
        buttonAns2 = findViewById(R.id.btnAnswer1);
        buttonAns3 = findViewById(R.id.btnAnswer2);
        buttonAns4 = findViewById(R.id.btnAnswer3);

        arrayButtons = new ArrayList<>();
        arrayButtons.add(buttonAns1);
        arrayButtons.add(buttonAns2);
        arrayButtons.add(buttonAns3);
        arrayButtons.add(buttonAns4);

        for (int i = 0; i < arrayButtons.size(); i++) {
            final int finalI = i;
            arrayButtons.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playGame(finalI);
                }
            });
        }
        initUrlsAndNames();
        playGame(-1);
    }

    private void playGame(int position) {
        generaeRandomQuestion();
        int total = 0;
        for (int i = 0; i < allImageDownloader.size(); i++) {
            total += i;
        }

        if(total == allImageDownloader.size()-1){
            String sc = getResources().getString(R.string.totalScore);
            String new_sc = String.format(sc, rightAnswer, wrongAnswer);
            Intent intent = new Intent(this, Total_score.class);
            intent.putExtra("total", new_sc);
        }

        if (position == currentRightAnswerPosition) {
            rightAnswer++;
            Toast.makeText(this, "Right", Toast.LENGTH_SHORT).show();
            String sc = getResources().getString(R.string.rightAns);
            String new_sc = String.format(sc, rightAnswer);
            TextView rightAns = findViewById(R.id.rightAns);
            rightAns.setText(new_sc);

        } else if (position == -1) {

        } else {
            wrongAnswer++;
            Toast.makeText(this, "Wrong", Toast.LENGTH_SHORT).show();
            String sc = getResources().getString(R.string.wrongtAns);
            String new_sc = String.format(sc, wrongAnswer);
            TextView wrongAns = findViewById(R.id.wrongAns);
            wrongAns.setText(new_sc);
        }
        Glide.with(this)
                .load(currentRightURL)
                .into(image);
        currentRightAnswerPosition = generateRightAnswerPositiion();

        for (int i = 0; i < arrayButtons.size(); i++) {
            if (i == currentRightAnswerPosition) {
                arrayButtons.get(i).setText(currentRightName);
            } else {
                String wrongAnswer = allNameDownloader.get(generateWrongAnswer());
                arrayButtons.get(i).setText(wrongAnswer);
            }
        }
    }

    private int generateRightAnswerPositiion() {
        Random r = new Random();
        return r.nextInt(arrayButtons.size());
    }

    private int generateWrongAnswer() {
        Random r = new Random();
        return r.nextInt(allNameDownloader.size() - 1) + 1;
    }

    private void generaeRandomQuestion() {
        Random r = new Random();
        currentQuestionPosition = r.nextInt(allNameDownloader.size() - currentQuestion) + currentQuestion;

        currentRightURL = allImageDownloader.get(currentQuestionPosition);
        currentRightName = allNameDownloader.get(currentQuestionPosition);


        allImageDownloader.remove(currentQuestionPosition);
        allImageDownloader.add(0, currentRightURL);

        allNameDownloader.remove(currentQuestionPosition);
        allImageDownloader.add(0, currentRightName);


        currentQuestion++;
    }

    private void initUrlsAndNames() {
        AsyncImgDownloader asyncImgDownloader = new AsyncImgDownloader();
        AsyncNameDownloader asyncNameDownloader = new AsyncNameDownloader();
        AsyncPageDownloader asyncPageDownloader = new AsyncPageDownloader();

        try {
            String pageCode = asyncPageDownloader.execute("http://www.posh24.se/kandisar").get();
            allNameDownloader = asyncNameDownloader.execute(pageCode).get();
            allImageDownloader = asyncImgDownloader.execute(pageCode).get();

            Log.i(TAG, allImageDownloader.toString());
            Log.i(TAG, allNameDownloader.toString());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    class AsyncPageDownloader extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            URL url = null;
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            InputStreamReader inputStreamReader = null;
            BufferedReader bufferedReader = null;
            StringBuilder stringBuilder = new StringBuilder();
            try {
                url = new URL("http://www.posh24.se/kandisar");
                urlConnection = (HttpURLConnection) url.openConnection();
                inputStream = urlConnection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);

                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (inputStreamReader != null) {
                    try {
                        inputStreamReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return stringBuilder.toString();
        }
    }

    class AsyncNameDownloader extends AsyncTask<String, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            ArrayList<String> allNames = new ArrayList<>();
            Pattern pattern = Pattern.compile(" alt=\"(.*?)\"");
            Matcher matcher = pattern.matcher(strings[0]);
            while (matcher.find()) {
                String downloadAddress = matcher.group(1);
                allNames.add(downloadAddress);
            }
            return allNames;
        }
    }

    class AsyncImgDownloader extends AsyncTask<String, Void, ArrayList<String>> {


        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            ArrayList<String> allUrls = new ArrayList<>();
            Pattern patternImg = Pattern.compile("<img src=\"(.*?)\"");
            Matcher matcherImg = patternImg.matcher(strings[0]);
            while (matcherImg.find()) {
                String downloadAddress = matcherImg.group(1);
                allUrls.add(downloadAddress);
            }

            return allUrls;
        }
    }
}
