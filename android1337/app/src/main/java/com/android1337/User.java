package com.android1337;

/**
 * Created by William on 2016-09-22.
 */

public class User{

    int id, games, score;
    String username;

    public User(String username, int games, int score){
        this.username=username;
        this.games=games;
        this.score=score;
    }

    public int getId()  {   return id;  }

    public int getGames()  {   return games;  }

    public int getScore()  {   return score;  }

    public String getUsername()  {   return username;  }
}
