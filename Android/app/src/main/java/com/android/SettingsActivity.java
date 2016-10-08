package com.android;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;


public class SettingsActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    public static final String PREF_FILE_NAME = "PreferenceFile";
    private SeekBar speechRateBar;
    private TextToSpeechEngine ttsEngine;
    private SharedPreferences settings;
    private int currentProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

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
