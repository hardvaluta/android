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
        switch (event.getAction()) {
            case DragEvent.ACTION_DROP:
                RelativeLayout container = (RelativeLayout) v;
                if(container.getChildCount()<5){
                    View view = (View) event.getLocalState();
                    AnswerButton b = (AnswerButton)view;
                    ViewGroup owner = (ViewGroup) view.getParent();
                    owner.removeView(view);

                    int x=0,y=0;
                    for(AnswerButton button : wordButtons){
                        if(!button.getTaken()){
                            x=button.getXPos();
                            y=button.getYPos();
                        }
                    }

                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    lp.leftMargin+=x;
                    lp.topMargin+=y;
                    container.addView(view, lp);
                    b.setTaken(true);
                    view.setVisibility(View.VISIBLE);
                    setChanged();
                    notifyObservers();
                }
                break;
            default:
                break;
        }
        return true;
    }
}
