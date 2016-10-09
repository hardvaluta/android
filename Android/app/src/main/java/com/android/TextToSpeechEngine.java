package com.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;

import static java.lang.Float.parseFloat;

/**
 * Created by Fred on 2016-10-08.
 */

public class TextToSpeechEngine implements TextToSpeech.OnInitListener {
    private TextToSpeech tts;
    private float DEFAULTSPEECHRATE = 1f;
    private float MINSPEECHRATE = 0.2f;
    private float MAXSPEECHRATE = 1.8f;
    private static TextToSpeechEngine instance = null;
    private AppCompatActivity context;
    private TextToSpeechEngine(Context context) {
        tts = new TextToSpeech(context, this);
        // set speech rate
        tts.setSpeechRate(1f);
    }

    public static TextToSpeechEngine getInstance(Context context) {
        if (instance == null) {
            instance = new TextToSpeechEngine(context);
        }
        return instance;
    }

    @Override
    public void onInit(int status) {

    }

    public void setSpeechRate(int newSpeechRate) {
        setSpeechRate(((float)newSpeechRate/(float)100)*(MAXSPEECHRATE-MINSPEECHRATE)+MINSPEECHRATE);
    }

    public void setSpeechRate(float newSpeechRate) {
        SharedPreferences settings = context.getSharedPreferences(MainMenu.PREF_FILE_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        float speechRate;
        if ((newSpeechRate - 0.01f) > MINSPEECHRATE && (newSpeechRate + 0.01f) < MAXSPEECHRATE) {
            speechRate = newSpeechRate;
        } else {
            speechRate = DEFAULTSPEECHRATE;
        }

        editor.putFloat("speechRate", speechRate);
        editor.commit();

        tts.setSpeechRate(speechRate);

    }

    public int getSpeechRateInt() {
        return (int)((getSpeechRate()-MINSPEECHRATE)/((MAXSPEECHRATE-MINSPEECHRATE)*100f));
    }

    public float getSpeechRate() {
        if (context != null) {
            SharedPreferences settings = context.getSharedPreferences(MainMenu.PREF_FILE_NAME, 0);
            float speechRate = settings.getFloat("speechRate", 1f);
            if ((speechRate - 0.01f) > MINSPEECHRATE && (speechRate + 0.01f) < MAXSPEECHRATE) {
                return speechRate;
            }
        }
        return DEFAULTSPEECHRATE;
    }

    public void speak(String string) {
        tts.speak(string, TextToSpeech.QUEUE_FLUSH, null);
    }

    public boolean isSpeaking() {
        return tts.isSpeaking();
    }
}
