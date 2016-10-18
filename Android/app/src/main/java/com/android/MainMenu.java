package com.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

import org.w3c.dom.Text;


public class MainMenu extends AppCompatActivity
{
    //public final static String EXTRA_MESSAGE = "com.android.GameOne.GameOne";
    public static final String PREF_FILE_NAME = "PreferenceFile";

    private ImageButton singlePlayerButton, multiPlayerButton, settingsButton, profileButton;
    private SharedPreferences prefs;
    private TextView totalScoreTextView;
    private TextToSpeechEngine ttsEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        prefs = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
        System.out.println(prefs.getAll());

        settingsButton = (ImageButton) findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenu.this, SettingsActivity.class));
            }
        });

        singlePlayerButton = (ImageButton) findViewById(R.id.sinlgePlayerButton);
        singlePlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenu.this, ChooseGameActivity.class));
            }
        });

        multiPlayerButton = (ImageButton) findViewById(R.id.multiPlayerButton);
        multiPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(prefs.getBoolean("active", false)){
                    startActivity(new Intent(MainMenu.this, MultiplayerLandingPage.class));
                } else {
                    startActivity(new Intent(MainMenu.this, LoginMenu.class));
                }
            }
        });

        profileButton = (ImageButton) findViewById(R.id.profileButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenu.this, ProfileActivity.class));
            }
        });

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