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


import com.android.Client;
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
    private TextView textView;
    private ImageButton nextSentenceButton;
    private ImageView qImage;
    private int currentSentenceIdx = 0;
    private ArrayList<answerButton> wordButtons;
    private answerButton rightAnswer;
    private TextToSpeechEngine ttsEngine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_one);

        textView = ((TextView)findViewById(R.id.textView));
        nextSentenceButton = ((ImageButton)findViewById(R.id.nextSentenceButton));
        qImage = (ImageView)findViewById(R.id.questionImage);
        wordButtons= new ArrayList<answerButton>();
        nextSentenceButton.setClickable(false);

        RelativeLayout sentancePond=(RelativeLayout) findViewById(R.id.sentancePond);
        RelativeLayout dropZone = (RelativeLayout) findViewById(R.id.dropZone);
        dropZone.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                int action = event.getAction();
                switch (event.getAction()) {
                    case DragEvent.ACTION_DROP:
                        RelativeLayout container = (RelativeLayout) v;
                        if(container.getChildCount()<1){
                            View view = (View) event.getLocalState();
                            answerButton b = (answerButton)view;
                            ViewGroup owner = (ViewGroup) view.getParent();

                            if (b.isRightAnswer()){
                                b.setBackgroundColor(Color.GREEN);
                                nextSentenceButton.setClickable(true);
                            }
                            else{
                                b.setBackgroundColor(Color.RED);
                            }

                            owner.removeView(view);
                            b.setTaken(false);

                            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            container.addView(view, lp);
                            view.setVisibility(View.VISIBLE);
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        sentancePond.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                int action = event.getAction();
                switch (event.getAction()) {
                    case DragEvent.ACTION_DROP:
                        RelativeLayout container = (RelativeLayout) v;
                        if(container.getChildCount()<5){
                            View view = (View) event.getLocalState();
                            answerButton b = (answerButton)view;
                            ViewGroup owner = (ViewGroup) view.getParent();
                            owner.removeView(view);

                            int x=0,y=0;
                            for(answerButton button : wordButtons){
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
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        for(int n=1;n<5;n++){
            if(n==1){
                wordButtons.add(new answerButton(this));
            }
            else{
                wordButtons.add(new answerButton(this));
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

        Client client = null;
        try {
            client = Client.getInstance(this.getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        client.requestRoundSentenceGame(new VolleyCallback<ArrayList<Question>>() {

            public void onSuccessResponse(ArrayList<Question> qArray) {
                Question q=qArray.get(0);
                Collections.shuffle(wordButtons);
                wordButtons.get(0).setText(q.getA());
                wordButtons.get(0).setRightAnswer(true);
                wordButtons.get(1).setText(q.getB());
                wordButtons.get(2).setText(q.getC());
                wordButtons.get(3).setText(q.getD());
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

            /*if (buttonPressed.equals(wordButton1)) {
                currentScore++;
            }*/
            maxScore++;
            //nextSentenceButton.setText(currentScore + "/" + maxScore);
            finishedSentence = true;
        }
        ttsEngine.speak(buttonPressed.getText().toString());
    }

    private void nextSentence() {
        if (finishedSentence) {
            currentSentenceIdx = rand.nextInt(preString.length);

            for(answerButton b : wordButtons){
                if(b.isRightAnswer()){
                    b.setRightAnswer(false);
                    RelativeLayout container = (RelativeLayout) findViewById(R.id.sentancePond);
                    View view = (View) b;
                    ViewGroup owner = (ViewGroup) view.getParent();
                    owner.removeView(view);

                    int x=0,y=0;
                    for(answerButton button : wordButtons){
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
                }
            }

            nextSentenceButton.setClickable(false);
            Collections.shuffle(wordButtons);

            textView.setText(preString[currentSentenceIdx] + "______" + postString[currentSentenceIdx]);
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
    private class answerButton extends Button{
        private int x;
        private int y;
        private boolean taken;
        private boolean rightAnswer;
        public answerButton(Context context){
            super(context);
            taken=true;
            rightAnswer=false;
        }
        public boolean getTaken(){return taken;}
        public void setTaken(boolean taken){this.taken=taken;}
        public int getXPos(){return x;}
        public int getYPos(){return y;}
        public void setXPos(int x){this.x=x;}
        public void setYPos(int y){this.y=y;}
        public boolean isRightAnswer(){return rightAnswer;}
        public void setRightAnswer(boolean rightAnswer){this.rightAnswer=rightAnswer;}
    }
}
