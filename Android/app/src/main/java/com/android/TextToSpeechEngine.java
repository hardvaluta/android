package com.android;

import android.content.Context;
import android.media.MediaPlayer;
import android.speech.tts.TextToSpeech;

/**
 * Created by Fred on 2016-10-08.
 */

public class TextToSpeechEngine implements TextToSpeech.OnInitListener {
    public TextToSpeech tts;
    private float DEFAULTSPEECHRATE = 1f;
    private float MINSPEECHRATE = 0.2f;
    private float MAXSPEECHRATE = 1.8f;
    private static TextToSpeechEngine instance = null;
    private static MediaPlayer manager = null;
    private TextToSpeechEngine(Context context) {
        tts = new TextToSpeech(context, this);
    }

    public static TextToSpeechEngine getInstance(Context context) {
        if (instance == null) {
            instance = new TextToSpeechEngine(context);
            manager = MediaPlayer.create(context, R.raw.a_tone);
        }
        return instance;
    }

    @Override
    public void onInit(int status){

    }

    public void setSpeechRate(int speechRate) {
        tts.setSpeechRate((float)speechRate/50f);
    }

    public void speak(String string) {
        tts.speak(string, TextToSpeech.QUEUE_FLUSH, null);
    }

    public void silentSound(){
        System.out.println("NOTICCEEEEEEEEEEEEEEEEE");
        manager.start();
    }
    public boolean isPlaying(){
        return manager.isPlaying();
    }

    public boolean isSpeaking() {
        return tts.isSpeaking();
    }
}
