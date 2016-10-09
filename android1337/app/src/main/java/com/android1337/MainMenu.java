package com.android1337;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static com.android1337.R.id.multiPlayerButton;
import static com.android1337.R.id.settingsButton;
import static com.android1337.R.id.start;

public class MainMenu extends AppCompatActivity
{
    //public final static String EXTRA_MESSAGE = "com.android1337.MenuActivity";
    public static final String PREF_FILE_NAME = "PreferenceFile";
    private ImageButton singlePlayerButton, multiPlayerButton, settingsButton;
    private SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        prefs = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);

        settingsButton = (ImageButton) findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(v -> startActivity(new Intent(MainMenu.this, SettingsActivity.class)));

        singlePlayerButton = (ImageButton) findViewById(R.id.sinlgePlayerButton);
        singlePlayerButton.setOnClickListener(v -> startActivity(new Intent(MainMenu.this, ChooseGameActivity.class)));

        multiPlayerButton = (ImageButton) findViewById(R.id.multiPlayerButton);
        multiPlayerButton.setOnClickListener(v -> {
            if(prefs.getBoolean("active", false)){
                //startActivity(;new Intent(MainMenu.this, MultiPlayerEnter.class));
            } else {
                startActivity(new Intent(MainMenu.this, SettingsActivity.class));
            }

        });


        ((TextView)findViewById(R.id.totalScoreTextView)).setText("Total poäng: "+prefs.getInt("totalScore", 0));

    }
}