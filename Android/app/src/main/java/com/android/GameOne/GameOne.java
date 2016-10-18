package com.android.GameOne;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.android.Client;
import com.android.GameInfo;
import com.android.GameTwo;
import com.android.Question;
import com.android.R;
import com.android.SpeakerSlave;
import com.android.TextToSpeechEngine;
import com.android.VolleyCallback;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class GameOne extends AppCompatActivity implements Observer{
    private final int nrQuestions = 4;
    public static final int gameId = 1;
    private boolean isSingleGame = true;
    private String[] preString={"Barnet", "Han", "Hon", "Dem", "Dem spelar"};
    private String[] postString = {"i träd", "i bassängen", "vattenmelon", "tillsammans", ""};
    private String[][] words = {{"klättrar", "dansar", "sjunger", "kör"},{"simmar", "skjuter", "pratar", "trollar"},{"äter", "sitter", "skriker", "gråter"},{"dansar", "tittar", "äter", "hoppar"},{"fotboll", "bowling", "datorspel", "tennis"}};
    private int[] images = {R.mipmap.klattrar, R.mipmap.simmar, R.mipmap.ater, R.mipmap.dansar, R.mipmap.fotboll};

    private Random rand = new Random();
    private int currentScore = 0;
    private int maxScore = 0;
    private TextView leftSentence;
    private TextView rightSentence;
    private ImageButton nextSentenceButton;
    private ImageView qImage;
    private int currentSentenceIdx = 0;
    private ArrayList<AnswerButton> wordButtons;
    private TextToSpeechEngine ttsEngine;
    private DragZoneListener dropListen;

    private static GameInfo multiplayerInfo=null;
    private ArrayList<Question> questionArray;
    private Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_one);

        if(getIntent().getExtras()!=null){
            Bundle extra = getIntent().getExtras();
            multiplayerInfo = (GameInfo) extra.get("GameInfo");
        }

        leftSentence= (TextView)findViewById(R.id.leftSentance);
        rightSentence= (TextView)findViewById(R.id.rightSentance);
        nextSentenceButton = ((ImageButton)findViewById(R.id.nextSentenceButton));
        qImage = (ImageView)findViewById(R.id.questionImage);
        wordButtons= new ArrayList<AnswerButton>();
        nextSentenceButton.setClickable(false);
        nextSentenceButton.setVisibility(Button.INVISIBLE);

        questionArray= new ArrayList<Question>();

        RelativeLayout sentancePond=(RelativeLayout) findViewById(R.id.sentancePond);
        final RelativeLayout dropZone = (RelativeLayout) findViewById(R.id.dropZone);
        LinearLayout dropLayout = (LinearLayout) findViewById(R.id.dropLayout);

        dropLayout.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                View zone = (View) dropZone;
                dropListen.onDrag(zone, event);
                return false;
            }
        });

        dropListen = new DragZoneListener(nextSentenceButton);
        dropListen.addObserver(this);
        MainDragListener mainListen = new MainDragListener(wordButtons);
        mainListen.addObserver(dropListen);
        dropZone.setOnDragListener(dropListen);
        sentancePond.setOnDragListener(mainListen);

        AnswerButton choise1 = (AnswerButton) findViewById(R.id.choise1);
        AnswerButton choise2 = (AnswerButton) findViewById(R.id.choise2);
        AnswerButton choise3 = (AnswerButton) findViewById(R.id.choise3);
        AnswerButton choise4 = (AnswerButton) findViewById(R.id.choise4);
        wordButtons.add(choise1);wordButtons.add(choise2);wordButtons.add(choise3);wordButtons.add(choise4);
        dropListen.addObserver(choise1);
        dropListen.addObserver(choise2);
        dropListen.addObserver(choise3);
        dropListen.addObserver(choise4);
        choise1.setOnTouchListener(new DragList(choise1));
        choise2.setOnTouchListener(new DragList(choise2));
        choise3.setOnTouchListener(new DragList(choise3));
        choise4.setOnTouchListener(new DragList(choise4));

        ImageButton ttsButton=(ImageButton) findViewById(R.id.TTS);
        ttsButton.setOnClickListener(new SentenceListener(this));

        try{
            client = com.android.Client.getInstance(this.getApplicationContext());
        }catch(java.lang.Exception e){}
        if(multiplayerInfo==null){
            client.requestRoundSentenceGame(new VolleyCallback<ArrayList<Question>>(){

                @Override
                public void onSuccessResponse(ArrayList<Question> qArray) {
                    for(int n=0;n<4;n++){
                        questionArray.add(qArray.get(n));
                    }
                    nextQuestion();
                }
            });
        }
        else{
            multiplayerInfo.getRounds(client, new VolleyCallback<ArrayList<Question>>(){
                @Override
                public void onSuccessResponse(ArrayList<Question> qArray) {
                    Question q = qArray.get(0);
                    for(int n=0;n<4;n++){
                        questionArray.add(qArray.get(n));
                    }
                    nextQuestion();
                }
            });
        }

        //nextSentence();
        nextSentenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextQuestion();
            }
        });
    }

    private void nextQuestion(){
        currentSentenceIdx = rand.nextInt(questionArray.size());
        Question q = questionArray.get(currentSentenceIdx);
        RelativeLayout container = (RelativeLayout) findViewById(R.id.sentancePond);
        if(container.getChildCount()<8){
            for(AnswerButton b : wordButtons){
                if(b.isRightAnswer()){
                    b.setRightAnswer(false);
                    View view = (View) b;
                    ViewGroup owner = (ViewGroup) view.getParent();
                    owner.removeView(view);
                    container.addView(view);
                    b.setTaken(true);
                    view.setVisibility(View.VISIBLE);
                    dropListen.update(null, "Restore");
                }
            }
        }

        for(AnswerButton b : wordButtons){
            b.setDragable(true);
        }

        nextSentenceButton.setClickable(false);
        nextSentenceButton.setVisibility(AnswerButton.INVISIBLE);
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
        wordButtons.get(0).setBackgroundColor(Color.LTGRAY);
        wordButtons.get(1).setBackgroundColor(Color.LTGRAY);
        wordButtons.get(2).setBackgroundColor(Color.LTGRAY);
        wordButtons.get(3).setBackgroundColor(Color.LTGRAY);

        final View view = (View) findViewById(R.id.sentanceGame);
        final Context context = this;

        Timer time = new Timer();
        time.schedule(new TimerTask() {
            @Override
            public void run() {
                new Thread(new SpeakerSlave(context, leftSentence.getText().toString(), null, rightSentence.getText().toString())).start();
            }
        }, 1000);
    }

    private void nextSentence() {
        currentSentenceIdx = rand.nextInt(preString.length);

        RelativeLayout container = (RelativeLayout) findViewById(R.id.sentancePond);
        if(container.getChildCount()<8){
            for(AnswerButton b : wordButtons){
                if(b.isRightAnswer()){
                    b.setRightAnswer(false);
                    View view = (View) b;
                    ViewGroup owner = (ViewGroup) view.getParent();
                    owner.removeView(view);
                    container.addView(view);
                    b.setTaken(true);
                    view.setVisibility(View.VISIBLE);
                    dropListen.update(null, "Restore");
                }
            }
        }

        for(AnswerButton b : wordButtons){
            b.setDragable(true);
        }

        nextSentenceButton.setClickable(false);
        nextSentenceButton.setVisibility(AnswerButton.INVISIBLE);
        Collections.shuffle(wordButtons);

        leftSentence.setText(preString[currentSentenceIdx]);
        rightSentence.setText(postString[currentSentenceIdx]);
        wordButtons.get(0).setText(words[currentSentenceIdx][0]);
        wordButtons.get(0).setRightAnswer(true);
        wordButtons.get(1).setText(words[currentSentenceIdx][1]);
        wordButtons.get(2).setText(words[currentSentenceIdx][2]);
        wordButtons.get(3).setText(words[currentSentenceIdx][3]);
        qImage.setImageResource(images[currentSentenceIdx]);
        wordButtons.get(0).setBackgroundColor(Color.LTGRAY);
        wordButtons.get(1).setBackgroundColor(Color.LTGRAY);
        wordButtons.get(2).setBackgroundColor(Color.LTGRAY);
        wordButtons.get(3).setBackgroundColor(Color.LTGRAY);

        final View view = (View) findViewById(R.id.sentanceGame);
        final Context context = this;
        Timer time = new Timer();
        time.schedule(new TimerTask() {
            @Override
            public void run() {
                new Thread(new SpeakerSlave(context, leftSentence.getText().toString(), null, rightSentence.getText().toString())).start();
            }
        }, 1000);
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
        alertDialog.setMessage("Är du sker på att du vill lämna spelet?");
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

    @Override
    public void update(Observable o, Object arg) {
        ProgressBar progressBar=(ProgressBar) findViewById(R.id.progressBar);
        TextView progressText = (TextView) findViewById(R.id.progressText);
        progressBar.setProgress(progressBar.getProgress()+1);
        progressText.setText(progressBar.getProgress()+"/"+nrQuestions+" frågor");

        if(progressBar.getProgress()<nrQuestions){
            nextSentenceButton.setClickable(true);
            nextSentenceButton.setVisibility(AnswerButton.VISIBLE);
        }
        else{   // Here is what happens when the game is finished!

            if(multiplayerInfo!=null){
                multiplayerInfo.reportProgress(client, progressBar.getProgress());
            }

            ImageView image = new ImageView(GameOne.this);
            try{
                InputStream ims = getAssets().open("good_job_smaller.jpg");
                image.setImageDrawable(Drawable.createFromStream(ims, null));
                ims.close();
            }catch(IOException e){}
            String message = "Bra jobbat!";
            ttsEngine.speak("Bra Jobbat!");
            AlertDialog gameFinished = new AlertDialog.Builder(GameOne.this).
                    setMessage(message).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    finish();
                }
            }).
                    setTitle("Omgång färdig!").
                    setView(image).show();
            TextView textView = (TextView) gameFinished.findViewById(android.R.id.message);
                    /*Typeface face=Typeface.createFromAsset(getAssets(), "Bubblegum.ttf");
                    textView.setTypeface(face);*/
            textView.setTextSize(50);
            textView.setAllCaps(false);
        }
    }

    private class DragList implements View.OnTouchListener{
        private final float SCROLL_THRESHOLD = 10;
        private float x,y;
        private boolean clicked;
        private AnswerButton button;
        private long timeOfClick;

        public DragList(AnswerButton button){
            this.button=button;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                timeOfClick=event.getEventTime();
                x = event.getX();
                y = event.getY();
                clicked=true;
                return true;
            }
            if (event.getAction() == MotionEvent.ACTION_UP){
                if(event.getEventTime()-timeOfClick<350 && (event.getX()-x<50&&event.getY()-y<50)){
                    ttsEngine.speak(button.getText().toString());
                }
            }
            if(event.getAction() == MotionEvent.ACTION_MOVE && button.isDragable()){
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
    private class SentenceListener implements View.OnClickListener{
        private Context context;
        public SentenceListener(Context context){
            this.context=context;
        }
        @Override
        public void onClick(View v) {
            boolean finishedSpeaking=false;
            TextView left = (TextView) findViewById(R.id.leftSentance);
            TextView right = (TextView) findViewById(R.id.rightSentance);
            RelativeLayout dropLayout = (RelativeLayout) findViewById(R.id.dropZone);

            String sLeft=left.getText().toString();
            String sChoise=null;
            String sRight=right.getText().toString();

            if(dropLayout.findViewById(R.id.dragSpot)==null){
                for(int n=0 ; n<dropLayout.getChildCount();n++){
                    if(dropLayout.getChildAt(n) instanceof AnswerButton){
                        AnswerButton choise=(AnswerButton)dropLayout.getChildAt(n);
                        sChoise=choise.getText().toString();
                    }
                }
            }

            new Thread(new SpeakerSlave(context, sLeft, sChoise, sRight)).start();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ttsEngine = TextToSpeechEngine.getInstance(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ttsEngine.shutdown();
    }

}
