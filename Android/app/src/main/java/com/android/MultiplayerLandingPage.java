package com.android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.GameOne.GameOne;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;


public class MultiplayerLandingPage extends AppCompatActivity {

    private Button newChallangeButton;
    private Client client;
    private SharedPreferences prefs;

    private ScrollView scrollView;
    private LinearLayout layout;

    private ArrayList<GameInfo> toBeAcceptedGames;
    private ArrayList<GameInfo> yourTurnGames;
    private ArrayList<GameInfo> otherTurnGames;
    private ArrayList<GameInfo> finishedGames;

    private ArrayList<GameInfo> allGames;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_landing_page);


        allGames = new ArrayList<>();
        try {
            client = Client.getInstance(this);
        } catch (Exception e) {
            System.out.println("Ingen internetanslutning");
        }

        prefs = getSharedPreferences(MainMenu.PREF_FILE_NAME, MODE_PRIVATE);

        client.getCurrGames(new VolleyCallback() {
            public void onSuccessResponse(Object o) {
                allGames.addAll((ArrayList<GameInfo>)o);
                init();
            }
        });
    }

    public void onBackPressed(){
        super.onBackPressed();
        startActivity(new Intent(this, MainMenu.class));
        finish();
    }

    public void init(){
        //GAMES WAITING TO BE ACCEPTED OR DECLINED BEFORE STARTED.
        toBeAcceptedGames = new ArrayList<GameInfo>();

        //GAMES IN WHICH THERE IS YOUR TURN TO PLAY.
        yourTurnGames = new ArrayList<GameInfo>();

        //GAMES WHERE IT IS THE OTHER USERS TURN TO PLAY.
        otherTurnGames = new ArrayList<GameInfo>();

        //FINISHED GAMES.
        finishedGames = new ArrayList<GameInfo>();

        TreeMap<Integer, GameInfo> finishedGameIds = new TreeMap<Integer, GameInfo>();

        if(!allGames.isEmpty()){
            for(GameInfo g : allGames){
                //finishedGameIds.put(g.getID(), g); // Test
                switch(g.getState()){

                    //GAMES TO BE ACCEPTED.
                    case 0:

                        if(prefs.getInt("user_id", 0) == g.getOwnerID())
                            System.out.println("User has not accepted game yet.");
                        else if(prefs.getInt("user_id", 0) == g.getSlaveID())
                            toBeAcceptedGames.add(g);

                        break;

                    //PLAYER 1 TURN.
                    case 1:

                        if(prefs.getInt("user_id", 0) == g.getOwnerID())
                            yourTurnGames.add(g);

                        else if (prefs.getInt("user_id", 0) == g.getSlaveID())
                            otherTurnGames.add(g);

                        break;

                    //PLAYER 2 TURN.
                    case 2:

                        if(prefs.getInt("user_id", 0) == g.getOwnerID())
                            otherTurnGames.add(g);

                        else if(prefs.getInt("userd_id", 0) == g.getSlaveID())
                            yourTurnGames.add(g);

                        break;

                    // FINISHED GAMES.
                    case 3:
                        finishedGames.add(g);
                        finishedGameIds.put(g.getID(), g);
                        break;

                    default: // WHAT?
                        break;
                }
            }

            try {

                Scanner scanner = new Scanner(openFileInput(ProfileActivity.SCORE_FILE_IDS));
                while(scanner.hasNextInt()) {
                    finishedGameIds.remove(scanner.nextInt());
                }
                scanner.close();
            } catch (Exception e) {  }
            try {
                if (!finishedGameIds.isEmpty()) {
                        PrintWriter printer = new PrintWriter(openFileOutput(ProfileActivity.SCORE_FILE_IDS, Context.MODE_APPEND));
                        for (GameInfo gameInfoElement: finishedGameIds.values()) {
                            //Add ID to ID list
                            printer.write(gameInfoElement.getID());
                            //Add game data to score list
                        }
                        printer.close();

                        FileOutputStream fosGameData = openFileOutput(ProfileActivity.SCORE_FILE_NAME3, Context.MODE_APPEND);
                        for (GameInfo gameInfoElement: finishedGameIds.values()) {
                            //Add ID to ID list
                            String gameDataString = 
                                        gameInfoElement.getType()               + ","
                                    +   gameInfoElement.getID()                 + ","
                                    +   gameInfoElement.getOwnerID()            + ","
                                    +   gameInfoElement.getSlaveID()   + "\n";
                            fosGameData.write(gameDataString.getBytes());
                            //Add game data to score list
                        }
                        fosGameData.close();
                }
            } catch (Exception e) { e.printStackTrace(); }
        }




        scrollView = (ScrollView) findViewById(R.id.scrollGameList);
        scrollView.removeAllViews();
        layout = new LinearLayout(scrollView.getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        if(!toBeAcceptedGames.isEmpty()){
            for(final GameInfo g : toBeAcceptedGames){


                LinearLayout tlayout = new LinearLayout(layout.getContext());
                tlayout.setOrientation(LinearLayout.HORIZONTAL);
                tlayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));

                String t = (g.getOwnerID() + " har utmanat dig!");
                TextView textView = new TextView(tlayout.getContext());
                textView.setText(t);

                Button accept = new Button(tlayout.getContext());
                Button decline = new Button(tlayout.getContext());

                accept.setText("Acceptera");
                decline.setText("Neka");

                accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        client.acceptChallenge(g.getID());
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                });

                decline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        client.declineChallenge(g.getID());
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                });



                tlayout.addView(textView);
                tlayout.addView(accept);
                tlayout.addView(decline);
                layout.addView(tlayout);

            }
        }


        if(!yourTurnGames.isEmpty()){
            for(final GameInfo g : yourTurnGames){

                final Button game = new Button(layout.getContext());
                game.setLayoutParams(
                        new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT)
                );

                int uid = prefs.getInt("user_id", 0) == g.getOwnerID() ? g.getSlaveID() : g.getOwnerID();

                client.getUser(uid, new VolleyCallback() {
                    @Override
                    public void onSuccessResponse(Object o) {

                        User u = (User)o;

                        game.setText("Spel mot " + u.getUsername() + ". Tryck för att spela.");
                        game.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                Intent intent = new Intent(MultiplayerLandingPage.this, GameOne.class);
                                intent.putExtra("GameInfo", g);
                                startActivity(intent);

                            }
                        });
                    }
                });


                layout.addView(game);
            }
        }

        if(!otherTurnGames.isEmpty()){
            for(GameInfo g : otherTurnGames){
                final Button game = new Button(layout.getContext());
                game.setLayoutParams(
                        new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT)
                );

                int uid = prefs.getInt("user_id", 0) == g.getOwnerID() ? g.getSlaveID() : g.getOwnerID();
                client.getUser(uid, new VolleyCallback() {
                    @Override
                    public void onSuccessResponse(Object o) {

                        User u = (User)o;
                        game.setText("Spel mot " + u.getUsername() + ". Det är inte din tur.");
                        game.setEnabled(false);
                    }
                });

                layout.addView(game);
            }
        }


        scrollView.addView(layout);


        newChallangeButton = (Button)findViewById(R.id.newChallengeButton);
        newChallangeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MultiplayerLandingPage.this, MultiPlayerChallengeUser.class));
            }
        });
    }
}
