package com.android.GameOne;

import android.content.res.Resources;
import android.graphics.Color;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.R;
import com.android.TextToSpeechEngine;

import org.w3c.dom.Text;

import java.util.Observable;
import java.util.Observer;


/**
 * Created by William on 2016-10-10.
 */

public class DragZoneListener extends Observable implements View.OnDragListener, Observer{
    ImageButton nextSentenceButton;
    View dragSpot;
    ViewGroup me;
    private int points=4;

    public DragZoneListener(ImageButton nextSentenceButton){
        this.nextSentenceButton=nextSentenceButton;
    }

    @Override
    public boolean onDrag(View v, DragEvent event){
        switch (event.getAction()) {
            case DragEvent.ACTION_DROP:
                if(dragSpot==null){
                    dragSpot = v.findViewById(R.id.dragSpot);
                }
                View view = (View) event.getLocalState();
                AnswerButton b = (AnswerButton)view;
                ViewGroup owner = (ViewGroup) view.getParent();
                me = (ViewGroup) v;

                if(!owner.equals(v) && dragSpot.getParent()!=null){
                    me.removeView(dragSpot);
                    owner.removeView(view);
                    me.addView(view);
                    if(b.isRightAnswer()){
                        setChanged();
                        notifyObservers(points);
                        b.setBackgroundColor(Color.GREEN);
                        points=4;
                    }
                    else{
                        b.setBackgroundColor(Color.RED);
                        points--;
                    }
                }
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof String){
            String message = (String)arg;
            if("Restore".equals(message.toString())){
                me.addView(dragSpot);
            }
        }
        else{
            me.addView(dragSpot);
        }
    }
}
