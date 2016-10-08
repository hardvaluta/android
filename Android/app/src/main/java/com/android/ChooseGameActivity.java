package com.android;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import java.util.Locale;

/**
 * Created by William on 2016-10-01.
 */

public class ChooseGameActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    TextToSpeech tts;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choosegame_menu);

        tts = new TextToSpeech(this, this);

        ImageButton mainMenu = (ImageButton) findViewById(R.id.homeButton);
        mainMenu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(ChooseGameActivity.this, MainMenu.class));
            }
        });

        ImageButton ttsOne = (ImageButton) findViewById(R.id.Game_One_TTS);
        ImageButton ttsTwo = (ImageButton) findViewById(R.id.Game_Two_TTS);
        ImageButton ttsThree = (ImageButton) findViewById(R.id.Game_Three_TTS);
        ImageButton ttsFour = (ImageButton) findViewById(R.id.Game_Four_TTS);
        ImageButton ttsFive = (ImageButton) findViewById(R.id.Game_Five_TTS);

        ttsOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wordButtonPressed(R.id.Game_One);
            }
        });
        ttsTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wordButtonPressed(R.id.Game_Two);
            }
        });
        ttsThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wordButtonPressed(R.id.Game_Three);
            }
        });
        ttsFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wordButtonPressed(R.id.Game_Four);
            }
        });
        ttsFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wordButtonPressed(R.id.Game_Five);
            }
        });

        Button gameOne = (Button) findViewById(R.id.Game_One);
        Button gameTwo = (Button) findViewById(R.id.Game_Two);
        Button gameThree = (Button) findViewById(R.id.Game_Three);
        Button gameFour = (Button) findViewById(R.id.Game_Four);
        Button gameFive = (Button) findViewById(R.id.Game_Five);

        gameOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                // startActivity(new Intent(ChooseGameActivity.this, GameOne.class));
            }
        });
        gameTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                 startActivity(new Intent(ChooseGameActivity.this, GameTwo.class));
            }
        });
        gameThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                // startActivity(new Intent(ChooseGameActivity.this, GameThree.class));
            }
        });
        gameFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                // startActivity(new Intent(ChooseGameActivity.this, GameFour.class));
            }
        });
        gameFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                // startActivity(new Intent(ChooseGameActivity.this, GameFive.class));
            }
        });

    }

    private void wordButtonPressed(int buttonIdPressed) {
        tts.speak(((Button)findViewById(buttonIdPressed)).getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS){
            //Denna raden sätter ibland till eng_USA i emulator i varje fall vilket förstör TTS
            //funktionaliteten
            Locale lang2   = tts.getLanguage();

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
}
