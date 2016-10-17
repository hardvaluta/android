package com.android;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by victor on 2016-10-15.
 */

public class GameInfo implements Serializable{
    private int game_id, p1, p2, state, type;
    private transient Client client;

    public GameInfo(int game_id, int p1, int p2, int state, int type, Client client){
        this.p1 = p1; this.p2 = p2;
        this.game_id = game_id; this.state = state;
        this.type = type; this.client = client;
    }

    public int getID(){
        return game_id;
    }

    public int getOwnerID(){
        return p1;
    }

    public int getChallengedUserID(){
        return p2;
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

    public void getRounds(final VolleyCallback callback){
        switch(type){
            case 1:
                client.requestRoundSentenceGame(callback);
                break;

            case 2: //DO NOT USE
                client.requestRoundMemoryGame(callback);
                break;

            default:
                System.out.println("Not supported");
        }
    }

    public void reportProgress(int correct){
        client.reportProgress(game_id, correct);
    }


}
