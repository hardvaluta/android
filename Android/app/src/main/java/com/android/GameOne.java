package com.android;

import android.content.DialogInterface;
import android.graphics.Color;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import android.util.Log;

import java.util.Locale;

public class GameOne extends AppCompatActivity {
    public static final int gameId = 1;
    private boolean isSingleGame = true;
    private String[] preString = {"Hej, jag ", "Erik spelar ", "Mattias sparkar på ", "Varför finns det ", "Vem är var det som ", "Vilken "};
    private String[] postString = {" Andreas.", " med sina vänner.", ", som ligger ner.", "?", "?", " är bäst?"};
    private String[][] words = {{"heter", "har", "var", "finns"}, {"fotboll", "träd", "kaffekopp", "svenska"}, {"Fred", "William", "Victor", "Edvin", "Jimmie", "Philip"}, {"krig", "fred", "vänsterprasslare", "pennvässare", "analklåda"}, {"sjöng så fint asså", "fes", "fez", "Anders and"}, {"varmkorv", "boogie", "vafan", "en sista"}};
    private Random rand = new Random();
    private int currentScore = 0;
    private int maxScore = 0;
    private boolean finishedSentence = false;
    private TextView textView;
    private Button wordButton1;
    private Button wordButton2;
    private Button wordButton3;
    private Button wordButton4;
    private Button nextSentenceButton;
    private ImageView qImage;
    private int currentSentenceIdx = 0;
    private Integer[] wordButtonIdArray = new Integer[]{R.id.wordButton1, R.id.wordButton2, R.id.wordButton3, R.id.wordButton4};

    private TextToSpeechEngine ttsEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_one);
        textView = ((TextView)findViewById(R.id.textView));
        wordButton1 = ((Button)findViewById(R.id.wordButton1));
        wordButton2 = ((Button)findViewById(R.id.wordButton2));
        wordButton3 = ((Button)findViewById(R.id.wordButton3));
        wordButton4 = ((Button)findViewById(R.id.wordButton4));
        nextSentenceButton = ((Button)findViewById(R.id.nextSentenceButton));
        qImage = (ImageView)findViewById(R.id.questionImage);

        com.android.Client client = com.android.Client.getInstance(this.getApplicationContext());


        client.requestData(com.android.Client.QUESTION, 1, new VolleyCallback<ArrayList<Question>>() {

            public void onSuccessResponse(ArrayList<Question> qArray) {
                Question q=qArray.get(0);
                wordButton1.setText(q.getA());
                wordButton2.setText(q.getB());
                wordButton3.setText(q.getC());
                wordButton4.setText(q.getD());
                textView.setText(q.getText());
                qImage.setImageBitmap(q.getImg());
            }
        });

        //Text-To-Speech on "Question" click
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tmpString;
                //String tmpString2 = "Erik spelar ______ med sina vänner.";
                //tmpString = tmpString2;
                tmpString = textView.getText().toString();
                String parts[] = tmpString.split("______");
                ttsEngine.speak(parts[0].toString());
                while (ttsEngine.isSpeaking())
                {
                    try
                    {
                        Thread.sleep(200);
                    } catch (InterruptedException ex)
                    {
                        Thread.currentThread().interrupt();
                    }
                }
                ttsEngine.speak(parts[1].toString());
            }
        });

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

        ttsEngine = TextToSpeechEngine.getInstance(this);
    }

    private void wordButtonPressed(int buttonIdPressed) {
        if (finishedSentence == false) {
            findViewById(wordButtonIdArray[0]).setBackgroundColor(Color.GREEN);
            if (buttonIdPressed == wordButtonIdArray[0]) {
                currentScore++;
            } else {
                findViewById(buttonIdPressed).setBackgroundColor(Color.RED);
            }
            maxScore++;
            nextSentenceButton.setText(currentScore + "/" + maxScore);
            finishedSentence = true;
        }
        ttsEngine.speak(((Button)findViewById(buttonIdPressed)).getText().toString());
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
    public void onBackPressed() {
        //Display alert message when back button has been pressed
        backButtonHandler();
        return;
    }

    public void backButtonHandler() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                GameOne.this);
        // Setting Dialog Title
        alertDialog.setTitle("Lämna spelet?");
        // Setting Dialog Message
        alertDialog.setMessage("Är du säker på att du vill lämna spelet?");
        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.dialog_icon);

        alertDialog.setNegativeButton("NEJ",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        dialog.cancel();
                    }
                });
        // Setting Negative "NO" Button
        alertDialog.setPositiveButton("JA",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        // Setting Positive "Yes" Button

        // Showing Alert Message
        alertDialog.show();
    }

}
