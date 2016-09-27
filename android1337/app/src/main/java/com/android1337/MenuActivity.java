package com.android1337;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
    private ImageView qImage;
    private int currentSentenceIdx = 0;
    private Integer[] wordButtonIdArray = new Integer[]{R.id.wordButton1, R.id.wordButton2, R.id.wordButton3, R.id.wordButton4};

    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        textView = ((TextView)findViewById(R.id.textView));
        wordButton1 = ((Button)findViewById(R.id.wordButton1));
        wordButton2 = ((Button)findViewById(R.id.wordButton2));
        wordButton3 = ((Button)findViewById(R.id.wordButton3));
        wordButton4 = ((Button)findViewById(R.id.wordButton4));
        nextSentenceButton = ((Button)findViewById(R.id.nextSentenceButton));
        qImage = (ImageView)findViewById(R.id.questionImage);

        Client client = Client.getInstance(this.getApplicationContext());


        client.requestData(Client.QUESTION, 1, new VolleyCallback<Question>() {

            public void onSuccessResponse(Question q) {
                test=q.getA();
                wordButton1.setText(q.getA());
                wordButton2.setText(q.getB());
                wordButton3.setText(q.getC());
                wordButton4.setText(q.getD());
                textView.setText(q.getText());
                /*
                client.requestData(Client.IMAGE, q.getImageId(), new VolleyCallback<Bitmap>(){
                    public void onSuccessResponse(Bitmap bm){
                        qImage.setImageBitmap(bm);
                    }
                });*/
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
        if (finishedSentence == false) {
            ((Button) findViewById(wordButtonIdArray[0])).setBackgroundColor(Color.GREEN);
            if (buttonIdPressed == wordButtonIdArray[0]) {
                currentScore++;
            } else {
                ((Button) findViewById(buttonIdPressed)).setBackgroundColor(Color.RED);
            }
            maxScore++;
            nextSentenceButton.setText(currentScore + "/" + maxScore);
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

    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        //String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        //textView.setText(message);

        //ViewGroup layout = (ViewGroup) findViewById(R.id.);
        //layout.addView(textView);
    //}

    //public GameActivity() {
        createSentenceList();

        textView = ((TextView)findViewById(R.id.textView));
        wordButton1 = ((Button)findViewById(R.id.wordButton1));
        wordButton2 = ((Button)findViewById(R.id.wordButton2));
        wordButton3 = ((Button)findViewById(R.id.wordButton3));
        wordButton4 = ((Button)findViewById(R.id.wordButton4));
        nextSentenceButton = ((Button)findViewById(R.id.nextSentenceButton));

        wordButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkForCorrectAnswer(1);
            }
        });
        wordButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkForCorrectAnswer(2);
            }
        });
        wordButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkForCorrectAnswer(3);
            }
        });
        wordButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkForCorrectAnswer(4);
            }
        });
        nextSentenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(finishedSentence) {
                    showNextSentence();
                    finishedSentence = false;
                }
            }
        });

        showNextSentence();

    }

    private void createSentenceList() {

        for (int i = 0; i < preString.length; i++) {
            sentenceList.add(new Sentence(preString[i], Arrays.asList(words[i]), postString[i]));
        }
    }

    private void showNextSentence() {
        if (currentSentence >= sentenceList.size() - 1) {
            showSentence(sentenceList.get(currentSentence++));
        } else {
            textView.setText("Score: " + currentScore + ", max: " + maxScore);
        }
    }

    private void showSentence(Sentence s) {

        textView.setText(s.toString());
        wordButton1.setText(s.wordList.get(1));
        wordButton1.setBackgroundColor(Color.parseColor("GRAY"));
        wordButton2.setText(s.wordList.get(2));
        wordButton2.setBackgroundColor(Color.parseColor("GRAY"));
        wordButton3.setText(s.wordList.get(3));
        wordButton3.setBackgroundColor(Color.parseColor("GRAY"));
        wordButton4.setText(s.wordList.get(4));
        wordButton4.setBackgroundColor(Color.parseColor("GRAY"));
        correctButtonIndex = 1;

    }

    private void checkForCorrectAnswer(int chosenButtonIndex) {
        if (correctButtonIndex != chosenButtonIndex) {
            switch (chosenButtonIndex) {
                case 1:
                    wordButton1.setBackgroundColor(Color.parseColor("RED"));
                    break;
                case 2:
                    wordButton2.setBackgroundColor(Color.parseColor("RED"));
                    break;
                case 3:
                    wordButton3.setBackgroundColor(Color.parseColor("RED"));
                    break;
                case 4:
                    wordButton4.setBackgroundColor(Color.parseColor("RED"));
                    break;
            }
        } else {
            currentScore++;
        }
        maxScore++;
        switch (correctButtonIndex) {
            case 1:
                wordButton1.setBackgroundColor(Color.parseColor("GREEN"));
                break;
            case 2:
                wordButton2.setBackgroundColor(Color.parseColor("GREEN"));
                break;
            case 3:
                wordButton3.setBackgroundColor(Color.parseColor("GREEN"));
                break;
            case 4:
                wordButton4.setBackgroundColor(Color.parseColor("GREEN"));
                break;
        }
        finishedSentence = true;

    }
*/

}
