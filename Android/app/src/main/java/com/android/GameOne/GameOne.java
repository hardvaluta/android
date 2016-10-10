package com.android.GameOne;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.android.Question;
import com.android.R;
import com.android.TextToSpeechEngine;
import com.android.VolleyCallback;

import java.util.ArrayList;
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
    private TextView leftSentence;
    private TextView rightSentence;
    private ImageButton nextSentenceButton;
    private ImageView qImage;
    private int currentSentenceIdx = 0;
    private ArrayList<AnswerButton> wordButtons;
    private TextToSpeechEngine ttsEngine = TextToSpeechEngine.getInstance(this);
    private DragZoneListener dropListen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_one);

        leftSentence= (TextView)findViewById(R.id.leftSentance);
        rightSentence= (TextView)findViewById(R.id.rightSentance);

        nextSentenceButton = ((ImageButton)findViewById(R.id.nextSentenceButton));
        qImage = (ImageView)findViewById(R.id.questionImage);
        wordButtons= new ArrayList<AnswerButton>();
        nextSentenceButton.setClickable(false);

        RelativeLayout sentancePond=(RelativeLayout) findViewById(R.id.sentancePond);
        RelativeLayout dropZone = (RelativeLayout) findViewById(R.id.dropZone);
        dropListen = new DragZoneListener(nextSentenceButton);
        MainDragListener mainListen = new MainDragListener(wordButtons);
        mainListen.addObserver(dropListen);
        dropZone.setOnDragListener(dropListen);
        sentancePond.setOnDragListener(mainListen);

        //Create the Answer-choice buttons
        for(int n=1;n<5;n++){
            if(n==1){
                wordButtons.add(new AnswerButton(this));
            }
            else{
                wordButtons.add(new AnswerButton(this));
            }
            wordButtons.get(n-1).setId(n);
            wordButtons.get(n-1).setText("Placeholder");

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            int width = this.getResources().getDisplayMetrics().widthPixels-400;
            Random random = new Random();
            width=random.nextInt(width);
            lp.leftMargin=width;
            if(n==1){
                wordButtons.get(n-1).setYPos(130);
                lp.topMargin+=130;
            }
            else{
                wordButtons.get(n-1).setYPos((150*n-1));
                lp.topMargin+=(150*n-1);
            }
            wordButtons.get(n-1).setXPos(width);
            sentancePond.addView(wordButtons.get(n-1), lp);
            wordButtons.get(n-1).setOnTouchListener(new DragList(wordButtons.get(n-1)));
        }

        //Fetch the question data and add it to the screen
        com.android.Client client = com.android.Client.getInstance(this.getApplicationContext());
        client.requestData(com.android.Client.QUESTION, 4, new VolleyCallback<ArrayList<Question>>() {

            public void onSuccessResponse(ArrayList<Question> qArray) {
                Question q=qArray.get(0);
                Collections.shuffle(wordButtons);
                wordButtons.get(0).setText(q.getA());
                wordButtons.get(0).setRightAnswer(true);
                wordButtons.get(1).setText(q.getB());
                wordButtons.get(2).setText(q.getC());
                wordButtons.get(3).setText(q.getD());
                String[] sentence=q.getText().split("\\*");
                leftSentence.setText(sentence[0]);
                rightSentence.setText(sentence[1]);
                qImage.setImageBitmap(q.getImg());
            }
        });

        //Text-To-Speech on "Question" click

        /*textView.setOnClickListener(new View.OnClickListener() {
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
        });*/

        nextSentenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextSentence();
            }
        });
    }

    private void wordButtonPressed(Button buttonPressed) {
        if (finishedSentence == false) {

            maxScore++;
            finishedSentence = true;
        }
    }

    private void nextSentence() {
        if (finishedSentence) {
            currentSentenceIdx = rand.nextInt(preString.length);
            
            RelativeLayout container = (RelativeLayout) findViewById(R.id.sentancePond);
            if(container.getChildCount()<5){
                for(AnswerButton b : wordButtons){
                    if(b.isRightAnswer()){
                        b.setRightAnswer(false);
                        View view = (View) b;
                        ViewGroup owner = (ViewGroup) view.getParent();
                        owner.removeView(view);

                        int x=0,y=0;
                        for(AnswerButton button : wordButtons){
                            if(!button.getTaken()){
                                x=button.getXPos();
                                y=button.getYPos();
                            }
                        }

                        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        lp.leftMargin+=x;
                        lp.topMargin+=y;
                        container.addView(view, lp);
                        b.setTaken(true);
                        view.setVisibility(View.VISIBLE);
                        dropListen.update(null, null);
                    }
                }
            }

            nextSentenceButton.setClickable(false);
            Collections.shuffle(wordButtons);

            leftSentence.setText(preString[currentSentenceIdx]);
            rightSentence.setText(postString[currentSentenceIdx]);
            wordButtons.get(0).setText(words[currentSentenceIdx][0]);
            wordButtons.get(0).setRightAnswer(true);
            wordButtons.get(1).setText(words[currentSentenceIdx][1]);
            wordButtons.get(2).setText(words[currentSentenceIdx][2]);
            wordButtons.get(3).setText(words[currentSentenceIdx][3]);
            wordButtons.get(0).setBackgroundColor(Color.LTGRAY);
            wordButtons.get(1).setBackgroundColor(Color.LTGRAY);
            wordButtons.get(2).setBackgroundColor(Color.LTGRAY);
            wordButtons.get(3).setBackgroundColor(Color.LTGRAY);
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
                if (finishedSentence == false) {

                    maxScore++;
                    finishedSentence = true;
                }
                ttsEngine.speak(button.getText().toString());
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
