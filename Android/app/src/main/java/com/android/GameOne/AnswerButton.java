package com.android.GameOne;

import android.content.Context;
import android.widget.Button;

/**
 * Created by William on 2016-10-10.
 */

public class AnswerButton extends Button {
    private int x;
    private int y;
    private boolean taken;
    private boolean rightAnswer;
    public AnswerButton(Context context){
        super(context);
        taken=true;
        rightAnswer=false;
        this.setAllCaps(false);
    }

    public boolean getTaken(){return taken;}
    public void setTaken(boolean taken){this.taken=taken;}
    public int getXPos(){return x;}
    public int getYPos(){return y;}
    public void setXPos(int x){this.x=x;}
    public void setYPos(int y){this.y=y;}
    public boolean isRightAnswer(){return rightAnswer;}
    public void setRightAnswer(boolean rightAnswer){this.rightAnswer=rightAnswer;}
}
