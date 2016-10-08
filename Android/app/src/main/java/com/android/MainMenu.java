package com.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainMenu extends AppCompatActivity
{
    //public final static String EXTRA_MESSAGE = "com.android.GameOne";
    public static final String PREF_FILE_NAME = "PreferenceFile";
    public static final String SCORE_FILE_NAME = "scoreFile";
    private ImageButton singlePlayerButton;
    private TextView totalScoreTextView;
    private SharedPreferences settings;
    private TextToSpeechEngine ttsEngine;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        singlePlayerButton = (ImageButton) findViewById(R.id.sinlgePlayerButton);
        singlePlayerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(MainMenu.this, com.android.ChooseGameActivity.class));
            }
        });

        totalScoreTextView = (TextView) findViewById(R.id.totalScoreTextView);
        totalScoreTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(MainMenu.this, com.android.ProfileActivity.class));
            }
        });

        settings = getSharedPreferences(PREF_FILE_NAME, 0);
        ((TextView)findViewById(R.id.totalScoreTextView)).setText("Total poäng: "+settings.getInt("totalScore", 0));
        ttsEngine = TextToSpeechEngine.getInstance(this);

    }
}