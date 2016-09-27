package com.android1337;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android1337.Sentence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.util.Log;
import java.util.Locale;

public class MenuActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private View contentView;
    private String[] preString = {"Hej, jag ", "Erik spelar ", "Mattias sparkar på ", "Varför finns det ", "Vem är var det som ", "Vilken "};
    private String[] postString = {" Andreas.", " med sina vänner.", ", som ligger ner.", "?", "?", " är bäst?"};
    private String[][] words = {{"heter", "har", "var", "finns"}, {"fotboll", "träd", "kaffekopp", "svenska"}, {"Fred", "William", "Victor", "Edvin", "Jimmie", "Philip"}, {"krig", "fred", "vänsterprasslare", "pennvässare", "analklåda"}, {"sjöng så fint asså", "fes", "fez", "Anders and"}, {"varmkorv", "boogie", "vafan", "en sista"}};
    private List<Sentence> sentenceList = new ArrayList<Sentence>();
    private Sentence s = null;
    private Random rand = new Random();
    private int currentScore = 0;
    private int currentSentence = 0;
    private int maxScore = 0;
    private boolean finishedSentence = false;
    String test;
    private TextView textView;
    private Button wordButton1;
    private Button wordButton2;
    private Button wordButton3;
    private Button wordButton4;
    private Button nextSentenceButton;
    private TextView scoreTextView;
    private int currentSentenceIdx = 0;
    private Integer[] wordButtonIdArray = new Integer[]{R.id.wordButton1, R.id.wordButton2, R.id.wordButton3, R.id.wordButton4};

    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent intent = getIntent();


        textView = ((TextView)findViewById(R.id.textView));
        wordButton1 = ((Button)findViewById(R.id.wordButton1));
        wordButton2 = ((Button)findViewById(R.id.wordButton2));
        wordButton3 = ((Button)findViewById(R.id.wordButton3));
        wordButton4 = ((Button)findViewById(R.id.wordButton4));
        nextSentenceButton = ((Button)findViewById(R.id.nextSentenceButton));
        scoreTextView = (TextView) findViewById(R.id.scoreTextView);
        Client client = Client.getInstance(this.getApplicationContext());


        client.reqQuestion(1, new VolleyCallback<Question>() {

            public void onSuccessResponse(Question q) {
                test=q.getA();
                wordButton1.setText(q.getA());
                wordButton2.setText(q.getB());
                wordButton3.setText(q.getC());
                wordButton4.setText(q.getD());
                textView.setText(q.getText());
            }
        });

        System.out.println(test);

        wordButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wordButtonPressed(R.id.wordButton1);
            }
        });

        wordButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wordButtonPressed(R.id.wordButton2);
            }
        });

        wordButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wordButtonPressed(R.id.wordButton3);
            }
        });

        wordButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wordButtonPressed(R.id.wordButton4);
            }
        });

        nextSentenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextSentence();
            }
        });

       // nextSentence();

        tts = new TextToSpeech(this, this);
    }

    private void wordButtonPressed(int buttonIdPressed) {
        SharedPreferences settings = getSharedPreferences(MainMenu.PREF_FILE_NAME, 0);
        int totalScore = settings.getInt("totalScore", 0);
        if (finishedSentence == false) {
            ((Button) findViewById(wordButtonIdArray[0])).setBackgroundColor(Color.GREEN);
            if (buttonIdPressed == wordButtonIdArray[0]) {
                currentScore++;
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("totalScore", ++totalScore);
                editor.commit();
            } else {
                ((Button) findViewById(buttonIdPressed)).setBackgroundColor(Color.RED);
            }
            maxScore++;
            scoreTextView.setText("Poäng: "+currentScore+"/"+maxScore);
            finishedSentence = true;

        }
            tts.speak(((Button)findViewById(buttonIdPressed)).getText().toString(), TextToSpeech.QUEUE_FLUSH, null);

    }

    private void nextSentence() {
        if (finishedSentence) {
            currentSentenceIdx = rand.nextInt(preString.length);

            Collections.shuffle(Arrays.asList(wordButtonIdArray));

            textView.setText(preString[currentSentenceIdx] + "______" + postString[currentSentenceIdx]);
            ((Button) findViewById(wordButtonIdArray[0])).setText(words[currentSentenceIdx][0]);
            ((Button) findViewById(wordButtonIdArray[1])).setText(words[currentSentenceIdx][1]);
            ((Button) findViewById(wordButtonIdArray[2])).setText(words[currentSentenceIdx][2]);
            ((Button) findViewById(wordButtonIdArray[3])).setText(words[currentSentenceIdx][3]);
            wordButton1.setBackgroundColor(Color.LTGRAY);
            wordButton2.setBackgroundColor(Color.LTGRAY);
            wordButton3.setBackgroundColor(Color.LTGRAY);
            wordButton4.setBackgroundColor(Color.LTGRAY);
            finishedSentence = false;
        }
    }

    @Override
    public void onInit(int status){
        if (status == TextToSpeech.SUCCESS){
            Locale lang   = tts.getLanguage();
            int result = tts.setLanguage(lang);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "inget språkstöd");
            }else {
                //nothing
            }
        }else{
            Log.e("TTS", "uppstart kaputt");
        }
    }

}
