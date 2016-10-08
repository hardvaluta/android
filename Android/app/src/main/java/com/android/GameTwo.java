package com.android;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

public class GameTwo extends AppCompatActivity implements TextToSpeech.OnInitListener{
    private Button[] cards = new Button[12];
    private Button listenButton;
    private Button nextButton;
    private int cardClickedId1 = -1;
    private int cardClickedId2 = -1;
    private int lastClickedId = -1;
    private Set<Integer> finishedCards;
    private Integer[] shuffles = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
    private String[] words = {"fotboll", "telefon", "träd", "bord", "stol", "lampa"};
    private int numberOfCardViews = 0;

    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_two);

        tts = new TextToSpeech(this, this);

        listenButton = ((Button)findViewById(R.id.listenButton));
        listenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lastClickedId >= 0) {
                    tts.speak(words[shuffles[lastClickedId] % 6].toString(), TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
        listenButton.setClickable(false);
        listenButton.setVisibility(View.INVISIBLE);

        nextButton = ((Button)findViewById(R.id.nextButton));
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts.shutdown();
                GameTwo.this.finish();
            }
        });
        nextButton.setText("Nästa");
        nextButton.setClickable(false);
        nextButton.setVisibility(View.INVISIBLE);

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

    }

    @Override
    public void onInit(int status){
        if (status == TextToSpeech.SUCCESS){

            //Denna raden sätter ibland till eng_USA i emulator i varje fall vilket förstör TTS
            //funktionaliteten
            //Locale lang2   = tts.getLanguage();

            //Bättre att sätta sv direkt; Har inte mobilen det så fungerar inte TTS och borde
            //stängas av imho. Thoughts?
            Locale lang = new Locale("sv");
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

    private void cardClicked(int cardId) {
        if (!finishedCards.contains(cardId)) { // Is this card already paired?
            if (cardClickedId2 < 0) { // No second card chosen
                if (cardClickedId1 < 0) { // No first card chosen
                    listenButton.setClickable(true);
                    listenButton.setVisibility(View.VISIBLE);
                    viewCard(cardId);
                    cardClickedId1 = cardId;
                    lastClickedId = cardId;
                    numberOfCardViews++;
                } else { // a first card is chosen
                    if (cardClickedId1 == cardId) {
                        return; // Do nothing, since the same card was clicked again
                    } else if (shuffles[cardClickedId1]%6==shuffles[cardId]%6) {// Found a pair?
                        cardClickedId2 = cardId;
                        viewCard(cardClickedId1);
                        viewCard(cardClickedId2);
                        finishedCards.add(cardClickedId1);
                        finishedCards.add(cardClickedId2);
                        cardClickedId1 = -1;
                        cardClickedId2 = -1;
                        lastClickedId = cardId;
                        numberOfCardViews++;
                        if (finishedCards.size() >= 12) {
                            // Save progress.
                            String string = "GAMETWO TIME:" + System.currentTimeMillis() + ", SCORE: " + numberOfCardViews + "\n";
                            try {
                                FileOutputStream fos = openFileOutput(MainMenu.SCORE_FILE_NAME, Context.MODE_APPEND);
                                fos.write(string.getBytes());
                                fos.close();
                            } catch (Exception e) { e.printStackTrace(); }
                            nextButton.setClickable(true);
                            nextButton.setVisibility(View.VISIBLE);
                        }
                    } else { // Didn't find a pair => wait until a next click to flip the cards
                        viewCard(cardId);
                        cardClickedId2 = cardId;
                        lastClickedId = cardId;
                        numberOfCardViews++;
                        listenButton.setClickable(true);
                        listenButton.setVisibility(View.VISIBLE);
                    }
                }
            } else { // a second card is already chosen => flip the cards!
                hideCard(cardClickedId1);
                hideCard(cardClickedId2);
                cardClickedId1 = -1;
                cardClickedId2 = -1;
                lastClickedId = -1;
                listenButton.setClickable(false);
                listenButton.setVisibility(View.INVISIBLE);
            }
        } else {
            lastClickedId = cardId;
            listenButton.setClickable(true);
            listenButton.setVisibility(View.VISIBLE);
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
        cards[cardId].setBackgroundColor(Color.LTGRAY);
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
                        tts.shutdown();
                        finish();
                    }
                });
        // Setting Positive "Yes" Button

        // Showing Alert Message
        alertDialog.show();
    }

}
