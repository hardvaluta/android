package com.android;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;


public class GameOne extends AppCompatActivity {

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
    private Button[] wordButtons={wordButton1,wordButton2,wordButton3,wordButton4};
    private TextToSpeechEngine ttsEngine;

    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_one);
        textView = ((TextView)findViewById(R.id.textView));
        nextSentenceButton = ((Button)findViewById(R.id.nextSentenceButton));
        qImage = (ImageView)findViewById(R.id.questionImage);

        RelativeLayout sentancePond=(RelativeLayout) findViewById(R.id.sentancePond);
        RelativeLayout dropZone = (RelativeLayout) findViewById(R.id.dropZone);
        dropZone.setOnDragListener(new DragZoneListener());

        for(int n=1;n<5;n++){
            wordButtons[n-1] = new Button(this);
            wordButtons[n-1].setId(n);
            wordButtons[n-1].setText("Placeholder");
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            int width = this.getResources().getDisplayMetrics().widthPixels-400;
            Random random = new Random();
            width=random.nextInt(width);
            lp.leftMargin=width;
            if(n==1){
                lp.topMargin+=200;
            }
            else{
                lp.addRule(RelativeLayout.BELOW, n-1);
            }
            sentancePond.addView(wordButtons[n-1], lp);
            wordButtons[n-1].setOnTouchListener(new DragList(wordButtons[n-1]));
        }

        com.android.Client client = com.android.Client.getInstance(this.getApplicationContext());
        client.requestData(com.android.Client.QUESTION, 1, new VolleyCallback<ArrayList<Question>>() {

            public void onSuccessResponse(ArrayList<Question> qArray) {
                Question q=qArray.get(0);
                wordButtons[0].setText(q.getA());
                wordButtons[1].setText(q.getB());
                wordButtons[2].setText(q.getC());
                wordButtons[3].setText(q.getD());
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

        nextSentenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextSentence();
            }
        });

        ttsEngine = TextToSpeechEngine.getInstance(this);
    }

    private void wordButtonPressed(Button buttonPressed) {
        if (finishedSentence == false) {
            buttonPressed.setBackgroundColor(Color.GREEN);
            if (buttonPressed.equals(wordButton1)) {
                currentScore++;
            } else {
                buttonPressed.setBackgroundColor(Color.RED);
            }
            maxScore++;
            nextSentenceButton.setText(currentScore + "/" + maxScore);
            finishedSentence = true;
        }
        ttsEngine.speak(buttonPressed.getText().toString());
    }

    private void nextSentence() {
        if (finishedSentence) {
            currentSentenceIdx = rand.nextInt(preString.length);

            Collections.shuffle(Arrays.asList(wordButtons));

            textView.setText(preString[currentSentenceIdx] + "______" + postString[currentSentenceIdx]);
            wordButtons[0].setText(words[currentSentenceIdx][0]);
            wordButtons[1].setText(words[currentSentenceIdx][1]);
            wordButtons[2].setText(words[currentSentenceIdx][2]);
            wordButtons[3].setText(words[currentSentenceIdx][3]);
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

    private class DragList implements View.OnTouchListener{
        private final float SCROLL_THRESHOLD = 10;
        private float x,y;
        private boolean clicked;
        private Button button;

        public DragList(Button button){
            this.button=button;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                x = event.getX();
                y = event.getY();
                clicked=true;
                wordButtonPressed(button);
                return true;
            }
            if(event.getAction() == MotionEvent.ACTION_MOVE){
                if(clicked && (Math.abs(x - event.getX()) > SCROLL_THRESHOLD || Math.abs(y - event.getY()) > SCROLL_THRESHOLD)){
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                            v);
                    v.startDrag(data, shadowBuilder, v, 0);
                    return true;
                }
                return false;
            }
            else {
                return false;
            }
        }
    }

}
