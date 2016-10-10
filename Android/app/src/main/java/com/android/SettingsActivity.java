package com.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;

public class SettingsActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private SharedPreferences prefs;
    private Button login_logoutButton;
    private TextView loginInfoTextView;

    public static final String PREF_FILE_NAME = "PreferenceFile";
    private SeekBar speechRateBar;
    private TextToSpeechEngine ttsEngine;
    private SharedPreferences settings;
    private int currentProgress;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefs = getSharedPreferences(SettingsActivity.PREF_FILE_NAME, MODE_PRIVATE);

        loginInfoTextView = (TextView)findViewById(R.id.loginInfoTextView);
        login_logoutButton = (Button)findViewById(R.id.login_logoutButton);

        if(prefs.getBoolean("active", false)){
            //ANVÄNDAREN ÄR INLOGGAD.
            String uname = prefs.getString("username", "");
            loginInfoTextView.setText("Hej " + uname + "! Du är inloggad.");
            login_logoutButton.setText("Logga ut");
            login_logoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.remove("username");
                    editor.remove("active");
                    editor.remove("user_id");
                    editor.apply();

                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            });

        } else {
            //ANVÄNDAREN ÄR INTE INLOGGAD.
            loginInfoTextView.setText("Du är inte inloggad.");
            login_logoutButton.setText("Logga in");
            login_logoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(SettingsActivity.this, LoginMenu.class));
                }
            });
        }

        ttsEngine = TextToSpeechEngine.getInstance(this);

        speechRateBar = (SeekBar)findViewById(R.id.speechRateBar);
        settings = getSharedPreferences(SettingsActivity.PREF_FILE_NAME, 0);
        currentProgress = settings.getInt("speechRate", 50);
        speechRateBar.setProgress(currentProgress);
        speechRateBar.setOnSeekBarChangeListener(this);

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        currentProgress = progress;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("speechRate", currentProgress);
        editor.commit();
        Float tempFloat = ((float)currentProgress/50f);
        ttsEngine.tts.setSpeechRate(tempFloat);
        ttsEngine.speak("Detta är ett test");
    }
}
