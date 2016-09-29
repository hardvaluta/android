package com.android1337;

import android.graphics.Bitmap;

/**
 * Created by victor on 2016-09-22.
 */
public class Question {

    private String A, B, C, D, text;
    private Bitmap img;

    public Question(String A, String B, String C, String D, String text, Bitmap img){
        this.A = A; this.B = B; this.C = C; this.D = D; this.text = text; this.img = img;
    }

    public String getA() { return A; }

    public String getB() { return B; }

    public String getC() { return C; }

    public String getD() { return D; }

    public String getText() { return text; }

    public Bitmap getImg() { return img; }

}