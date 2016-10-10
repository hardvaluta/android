package com.android.GameOne;

import android.graphics.Color;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;


/**
 * Created by William on 2016-10-10.
 */

public class DragZoneListener implements View.OnDragListener{
    ImageButton nextSentenceButton;
    public DragZoneListener(ImageButton nextSentenceButton){
        this.nextSentenceButton=nextSentenceButton;
    }

    @Override
    public boolean onDrag(View v, DragEvent event){
        int action = event.getAction();
        switch (event.getAction()) {
            case DragEvent.ACTION_DROP:
                RelativeLayout container = (RelativeLayout) v;
                if(container.getChildCount()<1){
                    View view = (View) event.getLocalState();
                    AnswerButton b = (AnswerButton)view;
                    ViewGroup owner = (ViewGroup) view.getParent();

                    if (b.isRightAnswer()){
                        b.setBackgroundColor(Color.GREEN);
                        nextSentenceButton.setClickable(true);
                    }
                    else{
                        b.setBackgroundColor(Color.RED);
                    }

                    owner.removeView(view);
                    b.setTaken(false);

                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    container.addView(view, lp);
                    view.setVisibility(View.VISIBLE);
                }
                break;
            default:
                break;
        }
        return true;
    }
}
