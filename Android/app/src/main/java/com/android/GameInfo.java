package com.android;

import android.os.Parcelable;

import java.io.Serializable;

import static android.R.attr.type;

/**
 * Created by victor on 2016-10-15.
 */

public class GameInfo implements Serializable{
    private int game_id, p1, p2, p1_score, p2_score, state, type;
    private String ownerUsername, slaveUsername;

    public GameInfo(int game_id, int p1, int p2, int p1_score, int p2_score, int state, int type, Client client){
        this.p1 = p1; this.p2 = p2;
        this.p1_score = p1_score;
        this.p2_score = p2_score;
        this.game_id = game_id;
        this.state = state;
        this.type = type;

        client.getUser(p1, new VolleyCallback() {
            @Override
            public void onSuccessResponse(Object o) {
                ownerUsername = (String)o;
            }
        });

        client.getUser(p2, new VolleyCallback() {
            @Override
            public void onSuccessResponse(Object o) {
                slaveUsername = (String)o;
            }
        });
    }

    public int getID(){
        return game_id;
    }

    public int getOwnerID(){
        return p1;
    }

    public int getSlaveID(){
        return p2;
    }

    public int getOwnerScore() {
        return p1_score;
    }

    public int getSlaveScore(){
        return p2_score;
    }

    public String getOwnerUserName() {
        return ownerUsername;
    }

    public String getSlaveUserName(){
        return slaveUsername;
    }

    /*
    state = 0, the game has not started, ie waiting for user to accept challenge.
    state = 1, player1's turn.
    state = 2, player2's turn.
    state = 3, the game has ended.
     */
    public int getState() {
        return state;
    }

    //type denotes which type of game. Only supports type = 1, which is sentence game.
    public int getType(){
        return type;
    }

    public void getRounds(Client c, final VolleyCallback callback){
        switch(type){
            case 1:
                c.requestRoundSentenceGame(true, game_id, callback);
                break;

            case 2:
                c.requestRoundMemoryGame(callback);
                break;

            default:
                System.out.println("Not supported");
        }
    }

    public void reportProgress(Client c, int correct){
        c.reportProgress(game_id, correct);
    }


}
