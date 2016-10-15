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

import java.util.Observable;
import java.util.Observer;


/**
 * Created by William on 2016-10-10.
 */

public class DragZoneListener extends Observable implements View.OnDragListener, Observer{
    ImageButton nextSentenceButton;
    RelativeLayout container;
    View dragSpot;
    ViewGroup me;
    public DragZoneListener(ImageButton nextSentenceButton){
        this.nextSentenceButton=nextSentenceButton;
    }

    @Override
    public boolean onDrag(View v, DragEvent event){
        int action = event.getAction();
        switch (event.getAction()) {
            case DragEvent.ACTION_DROP:
                container = (RelativeLayout) v;
                if(dragSpot==null){
                    dragSpot = v.findViewById(R.id.dragSpot);
                }
                View view = (View) event.getLocalState();
                AnswerButton b = (AnswerButton)view;
                ViewGroup owner = (ViewGroup) view.getParent();
                me = (ViewGroup) v;
                ViewGroup mainView = (ViewGroup) owner.getParent();

                if(!owner.equals(v) && dragSpot.getParent()!=null){
                    System.out.println(view.toString() + "      :     "+owner.toString());
                    me.removeView(dragSpot);
                    owner.removeView(view);
                    me.addView(view);
                    if(b.isRightAnswer()){
                        setChanged();
                        notifyObservers();
                        b.setBackgroundColor(Color.GREEN);
                    }
                    else{
                        b.setBackgroundColor(Color.RED);
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
            me.addView(dragSpot);
        }
        else{
            me.addView(dragSpot);
        }
    }
}
