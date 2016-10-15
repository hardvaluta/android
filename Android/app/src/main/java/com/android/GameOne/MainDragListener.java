package com.android.GameOne;

import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

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
                View view = (View) event.getLocalState();
                AnswerButton b = (AnswerButton)view;
                if(b.isDragable())
                {
                    RelativeLayout container = (RelativeLayout) v;
                    ViewGroup me = (ViewGroup) v;
                    ViewGroup owner = (ViewGroup) view.getParent();

                    if(!view.getParent().equals(v)){
                        setChanged();
                        notifyObservers();
                        owner.removeView(view);
                        me.addView(view);
                    }
                    /*else{
                        System.out.println("Hände" + "      :     "+owner.toString());
                        x=event.getX();y=event.getY();
                        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        lp.leftMargin+=x;
                        lp.topMargin+=y;
                        view.setLayoutParams(lp);
                    }*/
                }
                break;
            default:
                break;
        }
        return true;
    }
}
