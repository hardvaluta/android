package com.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MultiplayerLandingPage extends AppCompatActivity {

    private Button newChallangeButton;
    private Client client;
    private SharedPreferences prefs;

    private ArrayList<GameInfo> allGames;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_landing_page);

        try {
            client = Client.getInstance(this);
        } catch (Exception e) {
            System.out.println("Ingen internetanslutning");
        }

        prefs = getSharedPreferences(MainMenu.PREF_FILE_NAME, MODE_PRIVATE);

        client.getCurrGames(new VolleyCallback() {
            public void onSuccessResponse(Object o) {
                allGames.addAll((ArrayList<GameInfo>)o);
            }
        });

        ArrayList<GameInfo> wasChallengedGames = new ArrayList<GameInfo>();
        ArrayList<GameInfo> youChallengedGames = new ArrayList<GameInfo>();
        ArrayList<GameInfo> currentGames = new ArrayList<GameInfo>();
        ArrayList<GameInfo> finishedGames = new ArrayList<GameInfo>();


        for(GameInfo g : allGames){
            switch(g.getState()){
                case 0:

                    if(prefs.getInt("user_id", 0) == g.getOwnerID())
                        youChallengedGames.add(g);
                    else
                        wasChallengedGames.add(g);
                    
                    break;

                case 1: currentGames.add(g);
                    break;

                case 2: finishedGames.add(g);
                    break;

                default: // WHAT?
                    break;
            }
        }

        newChallangeButton = (Button)findViewById(R.id.challengeNewUserButton);
        newChallangeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MultiplayerLandingPage.this, MultiPlayerChallengeUser.class));
            }
        });
    }
}
