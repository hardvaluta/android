package com.android1337;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by William on 2016-10-03.
 */

public class ConnectTheDots extends AppCompatActivity implements View.OnTouchListener{
    float x,y;
    boolean moving;
    ImageView drag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect_the_dots);

        drag = (ImageView) findViewById(R.id.dragImage);
        drag.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                moving=true;
            break;
            case MotionEvent.ACTION_MOVE:
                x=event.getRawX()-drag.getWidth()/2;
                y=event.getRawY()-drag.getHeight()*3/2;
                drag.setX(x);
                drag.setY(y);
            break;
            case MotionEvent.ACTION_UP:
                moving=false;
            break;
        }
        return true;
    }
}

