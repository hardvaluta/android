package com.android.GameOne;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by William on 2016-10-10.
 */

public class AnswerButton extends Button implements Observer{
    private int x;
    private int y;
    private boolean taken;
    private boolean rightAnswer;
    private boolean dragable;
    public AnswerButton(Context context){
        super(context);
        taken=true;
        rightAnswer=false;
        this.setAllCaps(false);
        dragable=true;
    }
    public AnswerButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        taken=true;
        rightAnswer=false;
        this.setAllCaps(false);
        dragable=true;
    }

    public AnswerButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        taken=true;
        rightAnswer=false;
        this.setAllCaps(false);
        dragable=true;
    }

    public boolean getTaken(){return taken;}
    public void setTaken(boolean taken){this.taken=taken;}
    public int getXPos(){return x;}
    public int getYPos(){return y;}
    public void setXPos(int x){this.x=x;}
    public void setYPos(int y){this.y=y;}
    public boolean isRightAnswer(){return rightAnswer;}
    public void setRightAnswer(boolean rightAnswer){this.rightAnswer=rightAnswer;}
    public boolean isDragable(){return dragable;}
    public void setDragable(boolean dragable){this.dragable=dragable;}

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("NOTIIIICE button dragable");
        dragable=false;
    }
}
