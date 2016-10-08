package com.android1337;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainMenu extends AppCompatActivity
{
    //public final static String EXTRA_MESSAGE = "com.android1337.MenuActivity";
    public static final String PREF_FILE_NAME = "PreferenceFile";
    private ImageButton singlePlayerButton;
    private ImageButton settingsButton;
    private SharedPreferences settings;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        settingsButton = (ImageButton) findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(v -> startActivity(new Intent(MainMenu.this, LoginMenu.class)));

        singlePlayerButton = (ImageButton) findViewById(R.id.sinlgePlayerButton);
        singlePlayerButton.setOnClickListener(v -> startActivity(new Intent(MainMenu.this, ChooseGameActivity.class)));

        settings = getSharedPreferences(PREF_FILE_NAME, 0);
        ((TextView)findViewById(R.id.totalScoreTextView)).setText("Total poäng: "+settings.getInt("totalScore", 0));

    }
}