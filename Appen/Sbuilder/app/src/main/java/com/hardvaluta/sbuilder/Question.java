package com.hardvaluta.sbuilder;

/**
 * Created by victor on 2016-09-22.
 */
public class Question {


    private String A, B, C, D, correctAns, qText;
    public Question(String A, String B, String C, String D, String correctAns, String qText){
        this.A = A; this.B = B; this.C = C; this.D = D; this.correctAns = correctAns; this.qText = qText;
    }

    public String getA() {
        return A;
    }

    public String getB() {
        return B;
    }

    public String getC() {
        return C;
    }

    public String getD() {
        return D;
    }

    public String getCorrectAns() {
        return correctAns;
    }

    public String getqText() {
        return qText;
    }
}
