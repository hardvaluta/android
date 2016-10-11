package com.android.GameOne;

import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by William on 2016-10-10.
 */

public class MainDragListener extends Observable implements View.OnDragListener{
    ArrayList<AnswerButton> wordButtons;

    public MainDragListener(ArrayList<AnswerButton> wordButtons){
        this.wordButtons=wordButtons;
    }

    @Override
    public boolean onDrag(View v, DragEvent event){
        int action = event.getAction();
        switch (event.getAction()){
            case DragEvent.ACTION_DROP:
                float x=0,y=0;
                RelativeLayout container = (RelativeLayout) v;
                View view = (View) event.getLocalState();
                AnswerButton b = (AnswerButton)view;
                ViewGroup owner = (ViewGroup) view.getParent();
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                if(container.getChildCount()<4){
                    owner.removeView(view);
                    for(AnswerButton button : wordButtons){
                        if(!button.getTaken()){
                            x=button.getXPos();
                            y=button.getYPos();
                            lp.leftMargin+=x;
                            lp.topMargin+=y;
                        }
                    }
                    b.setTaken(true);
                    container.addView(view, lp);
                    view.setVisibility(View.VISIBLE);
                    setChanged();
                    notifyObservers();
                }
                else{
                    x=event.getX();y=event.getY();
                    lp.leftMargin+=x;
                    lp.topMargin+=y;
                    view.setLayoutParams(lp);
                }
                break;
            default:
                break;
        }
        return true;
    }
}
