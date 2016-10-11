package com.android.GameOne;

import android.content.res.Resources;
import android.graphics.Color;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.R;

import java.util.Observable;
import java.util.Observer;


/**
 * Created by William on 2016-10-10.
 */

public class DragZoneListener implements View.OnDragListener, Observer {
    ImageButton nextSentenceButton;
    RelativeLayout container;
    View dragSpot;
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
                if(container.findViewById(R.id.dragSpot)!=null){
                    View view = (View) event.getLocalState();
                    AnswerButton b = (AnswerButton)view;
                    ViewGroup owner = (ViewGroup) view.getParent();
                    ViewGroup mainView = (ViewGroup) owner.getParent();

                    if (b.isRightAnswer()){
                        TextView score = (TextView)mainView.findViewById(R.id.scoreView);
                        String initialScore=score.getText().toString();
                        initialScore=initialScore.split(" ")[1];
                        int nScore = Integer.parseInt(initialScore);
                        nScore++;
                        String newScore = "Score: "+nScore;
                        score.setText(newScore);
                        b.setBackgroundColor(Color.GREEN);
                        nextSentenceButton.setClickable(true);
                    }
                    else{
                        b.setBackgroundColor(Color.RED);
                    }

                    owner.removeView(view);
                    b.setTaken(false);

                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    container.removeView(dragSpot);
                    container.addView(view, lp);
                    view.setVisibility(View.VISIBLE);
                }
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void update(Observable o, Object arg) {
        container.addView(dragSpot);
    }
}
