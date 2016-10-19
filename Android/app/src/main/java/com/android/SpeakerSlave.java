package com.android;

import android.content.Context;

/**
 * Created by William on 2016-10-11.
 */

public class SpeakerSlave implements Runnable{
    private String s1;
    private String s2;
    private String s3;
    private TextToSpeechEngine ttsEngine;

    public SpeakerSlave(Context context, String s1, String s2, String s3){
        this.s1=s1;
        this.s2=s2;
        this.s3=s3;
        ttsEngine=TextToSpeechEngine.getInstance(context);
    }
    @Override
    public void run() {
        if(s2==null){
            ttsEngine.speak(s1);
            while(ttsEngine.isSpeaking()){}
            ttsEngine.playEarcon("silence");
            while(ttsEngine.isSpeaking()){}
            ttsEngine.speak(s3);
        }
        else{
            String sentence=s1+" "+s2+" "+s3;
            ttsEngine.speak(sentence);
        }
    }
}
