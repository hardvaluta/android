package com.android;

import android.graphics.Bitmap;
import android.util.Pair;

import java.util.ArrayList;

/**
 * Created by victor on 2016-10-18.
 */

public class Memory {
    ArrayList<Pair<String, Bitmap>> memArr;

    public Memory(){
        memArr = new ArrayList<Pair<String, Bitmap>>();
    }

    public ArrayList<Pair<String, Bitmap>> getMemoryArrayList(){
        return memArr;
    }

    public void add(Pair<String, Bitmap> p){
        memArr.add(p);
    }

    public int size(){
        return memArr.size();
    }

    public Pair<String, Bitmap> get(int i){
        return memArr.get(i);
    }
}
