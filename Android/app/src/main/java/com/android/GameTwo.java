package com.android;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.DialogFragment;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.GameOne.GameOne;

import org.w3c.dom.Text;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;

public class GameTwo extends AppCompatActivity {
    public static final int gameId = 2;
    public int nrRounds = 1;
    private boolean isSingleGame = true;
    private TextView timePassedView;
    private Button[] cards = new Button[12];
    private Button listenButton;
    private Button nextButton;
    private int cardClickedId1 = -1;
    private int cardClickedId2 = -1;
    private int lastClickedId = -1;
    private Set<Integer> flippedCards;
    private Set<Integer> finishedCards;
    private Integer[] shuffles = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
    private String[] words = {"fotboll", "telefon", "träd", "bord", "stol", "lampa"};
    private int numberOfCardViews = 0;
    private int timePassed = -1;
    private Handler handler;
    private Runnable runnable;
    private Runnable runnablePenalty;

    private TextToSpeechEngine ttsEngine;

    private GameInfo multiplayerInfo=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game_two);
        
        if(getIntent().getExtras()!=null){
            Bundle extra = getIntent().getExtras();
            multiplayerInfo = (GameInfo) extra.get("GameInfo");
            isSingleGame = false;
        }

        timePassedView = (TextView)findViewById(R.id.timePassedView);
        if (isSingleGame) {
            timePassedView.setText("Antal kort vända: " + numberOfCardViews);
        }

        listenButton = ((Button)findViewById(R.id.listenButton));
        listenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lastClickedId >= 0) {
                    ttsEngine.speak(words[shuffles[lastClickedId] % 6].toString());
                }
            }
        });
        listenButton.setClickable(false);
        listenButton.setAlpha(0.4f);

        nextButton = ((Button)findViewById(R.id.nextButton));
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nrRounds--;
                ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar_Game2);
                progressBar.setProgress(progressBar.getProgress()+1);
                if(nrRounds==0){
                    ImageView image = new ImageView(GameTwo.this);
                    try{
                        InputStream ims = getAssets().open("good_job_smaller.jpg");
                        image.setImageDrawable(Drawable.createFromStream(ims, null));
                        ims.close();
                    }catch(IOException e){
                        System.out.println("Image not found");
                    }
                    String message = "";
                    if (isSingleGame) {
                        message = "Du vände på " + numberOfCardViews + " kort\nBra jobbat!";
                    } else {
                        message = "Det tog " + timePassed + " sekunder\nBra jobbat!";
                    }
                    ttsEngine.speak("Bra jobbat!");
                    AlertDialog gameFinished = new AlertDialog.Builder(GameTwo.this).
                            setMessage(message).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    }).
                            setTitle("Omgång färdig!").
                            setView(image).show();
                    gameFinished.setCancelable(false);
                    gameFinished.setCanceledOnTouchOutside(false);
                    TextView textView = (TextView) gameFinished.findViewById(android.R.id.message);
                    /*Typeface face=Typeface.createFromAsset(getAssets(), "Bubblegum.ttf");
                    textView.setTypeface(face);*/
                    textView.setTextSize(50);
                    textView.setAllCaps(false);

                    /*gameFinished.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                    gameFinished.show();*/
                }
                else{
                    nextButton.setClickable(false);
                    nextButton.setAlpha(0.4f);
                    //Setup the memory board with new cards!
                }
            }
        });

        nextButton.setText("Nästa");
        nextButton.setClickable(false);
        nextButton.setAlpha(0.4f);

        cards[0] = ((Button)findViewById(R.id.card11));
        cards[1] = ((Button)findViewById(R.id.card12));
        cards[2] = ((Button)findViewById(R.id.card13));

        cards[3] = ((Button)findViewById(R.id.card21));
        cards[4] = ((Button)findViewById(R.id.card22));
        cards[5] = ((Button)findViewById(R.id.card23));

        cards[6] = ((Button)findViewById(R.id.card31));
        cards[7] = ((Button)findViewById(R.id.card32));
        cards[8] = ((Button)findViewById(R.id.card33));

        cards[9] = ((Button)findViewById(R.id.card41));
        cards[10] = ((Button)findViewById(R.id.card42));
        cards[11] = ((Button)findViewById(R.id.card43));

        cards[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameTwo.this.cardClicked(0);
            }
        });
        cards[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameTwo.this.cardClicked(1);
            }
        });
        cards[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameTwo.this.cardClicked(2);
            }
        });

        cards[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameTwo.this.cardClicked(3);
            }
        });
        cards[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameTwo.this.cardClicked(4);
            }
        });
        cards[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameTwo.this.cardClicked(5);
            }
        });

        cards[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameTwo.this.cardClicked(6);
            }
        });
        cards[7].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameTwo.this.cardClicked(7);
            }
        });
        cards[8].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameTwo.this.cardClicked(8);
            }
        });

        cards[9].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameTwo.this.cardClicked(9);
            }
        });
        cards[10].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameTwo.this.cardClicked(10);
            }
        });
        cards[11].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameTwo.this.cardClicked(11);
            }
        });

        Collections.shuffle(Arrays.asList(shuffles));


        for (int i = 0; i < 12; i++) {
            cards[i].setText("");
        }

        finishedCards = new TreeSet<Integer>();
        flippedCards = new TreeSet<Integer>();

        if (!isSingleGame) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    timePassedView.setText("Tid: " + ++timePassed);
                    handler.postDelayed(this, 1000);
                }
            };
            handler = new Handler();
            handler.post(runnable);

            runnablePenalty = new Runnable() {
                @Override
                public void run() {
                    timePassedView.setBackgroundColor(Color.TRANSPARENT);
                }
            };
        }

    }

    private void cardClicked(int cardId) {
        if (!finishedCards.contains(cardId)) { // Is this card already paired?
            if (cardClickedId2 < 0) { // No second card chosen
                if (cardClickedId1 < 0) { // No first card chosen
                    listenButton.setClickable(true);
                    listenButton.setAlpha(1f);
                    viewCard(cardId);
                    cardClickedId1 = cardId;
                    lastClickedId = cardId;
                    numberOfCardViews++;
                    if (isSingleGame) {
                        timePassedView.setText("Antal kort vända: " + numberOfCardViews);
                    }
                } else { // a first card is chosen
                    if (cardClickedId1 == cardId) {
                        return; // Do nothing, since the same card was clicked again
                    } else if (shuffles[cardClickedId1]%6==shuffles[cardId]%6) {// Found a pair?
                        cardClickedId2 = cardId;
                        viewCard(cardClickedId1);
                        viewCard(cardClickedId2);
                        finishedCards.add(cardClickedId1);
                        finishedCards.add(cardClickedId2);
                        cards[cardClickedId1].setAlpha(0.4f);
                        cards[cardClickedId2].setAlpha(0.4f);
                        cardClickedId1 = -1;
                        cardClickedId2 = -1;
                        lastClickedId = cardId;
                        numberOfCardViews++;
                        if (isSingleGame) {
                            timePassedView.setText("Antal kort vända: " + numberOfCardViews);
                        }
                        if (finishedCards.size() >= 12) {
                            if (isSingleGame) {
                                // Save progress.
                                String string =
                                        "1"                             + ","
                                    +   gameId                          + ","
                                    +   System.currentTimeMillis()      + ","
                                    +   numberOfCardViews               + "\n";
                                try {
                                    FileOutputStream fos = openFileOutput(ProfileActivity.SCORE_FILE_NAME3, Context.MODE_APPEND);
                                    fos.write(string.getBytes(Charset.defaultCharset()));
                                    fos.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                handler.removeCallbacks(runnable);
                            }

                            nextButton.setClickable(true);
                            nextButton.setAlpha(1f);
                        }
                    } else { // Didn't find a pair => wait until a next click to flip the cards
                        cardClickedId2 = cardId;
                        lastClickedId = cardId;
                        numberOfCardViews++;
                        if (isSingleGame) {
                            timePassedView.setText("Antal kort vända: " + numberOfCardViews);
                        } else {
                            if (!flippedCards.add(cardClickedId1) || !flippedCards.add(cardClickedId2)) {
                                timePassed += 4;
                                timePassedView.setText("Tid: " + timePassed);
                                timePassedView.setBackgroundColor(Color.RED);
                                handler.postDelayed(runnablePenalty, 2500);
                            }
                        }
                        viewCard(cardId);
                        listenButton.setClickable(true);
                        listenButton.setAlpha(1f);
                    }
                }
            } else { // a second card is already chosen => flip the cards!
                hideCard(cardClickedId1);
                hideCard(cardClickedId2);
                cardClickedId1 = -1;
                cardClickedId2 = -1;
                lastClickedId = -1;
                listenButton.setClickable(false);
                listenButton.setAlpha(0.4f);
            }
        } else {
            lastClickedId = cardId;
            listenButton.setClickable(true);
            listenButton.setAlpha(1f);
        }
    }

    private void viewCard(int cardId) {
        if (shuffles[cardId] < 6) {
            cards[cardId].setText(words[shuffles[cardId]]);
        } else {
            switch (shuffles[cardId]%6) {
                case 0:
                    cards[cardId].setBackgroundResource(R.mipmap.memory0);
                    break;
                case 1:
                    cards[cardId].setBackgroundResource(R.mipmap.memory1);
                    break;
                case 2:
                    cards[cardId].setBackgroundResource(R.mipmap.memory2);
                    break;
                case 3:
                    cards[cardId].setBackgroundResource(R.mipmap.memory3);
                    break;
                case 4:
                    cards[cardId].setBackgroundResource(R.mipmap.memory4);
                    break;
                case 5:
                    cards[cardId].setBackgroundResource(R.mipmap.memory5);
                    break;
                default:
                    break;
            }
        }
    }

    private void hideCard(int cardId) {
        cards[cardId].setText("");
        cards[cardId].setBackgroundResource(R.mipmap.quarterwidthbutton);
    }


    @Override
    public void onBackPressed() {
        //Display alert message when back button has been pressed
        backButtonHandler();
        return;
    }

    public void backButtonHandler() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                GameTwo.this);
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
