package com.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.android.GameOne.GameOne;


/**
 * Created by William on 2016-10-01.
 */

public class ChooseGameActivity extends AppCompatActivity {
    TextToSpeechEngine ttsEngine;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choosegame_menu);

        ttsEngine = TextToSpeechEngine.getInstance(this);

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
                startActivity(new Intent(ChooseGameActivity.this, GameOne.class));
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
        ttsEngine.speak(((Button)findViewById(buttonIdPressed)).getText().toString());
    }
}
