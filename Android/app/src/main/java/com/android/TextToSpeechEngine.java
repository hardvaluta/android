package com.android;

import android.content.Context;
import android.media.MediaPlayer;
import android.speech.tts.TextToSpeech;

/**
 * Created by Fred on 2016-10-08.
 */

public class TextToSpeechEngine implements TextToSpeech.OnInitListener {
    public TextToSpeech tts;

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

    private static void kill(){
        instance = null;
    }

    @Override
    public void onInit(int status){
        System.out.println("Add earcon: "+tts.addEarcon("silence", "com.android", R.raw.a_tone));

    }

    public void setSpeechRate(int speechRate) {
        tts.setSpeechRate((float)speechRate/50f);
    }

    public void speak(String string){
        tts.stop();
        tts.speak(string, TextToSpeech.QUEUE_FLUSH, null);
    }

    public void speakCombo(String string){
        tts.speak(string, TextToSpeech.QUEUE_ADD, null);
    }

    public void playEarcon(String string){
        tts.playEarcon(string, TextToSpeech.QUEUE_ADD, null);
    }

    public void silentSound(){
        manager.start();
    }
    public boolean isPlaying(){
        return manager.isPlaying();
    }
    public boolean isSpeaking() {
        return tts.isSpeaking();
    }


    public void shutdown() {
        kill();
        tts.shutdown();
    }
}
