package com.android;

/**
 * Created by William on 2016-09-22.
 */

public class User{

    private int id, score;
    private String username;

    public User(String username, int score, int id){
        this.username=username;
        this.score=score;
    }

    public int getId()  {   return id;  }

    public int getScore()  {   return score;  }

    public String getUsername()  {   return username;  }
}
