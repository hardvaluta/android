package com.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class MultiplayerLandingPage extends AppCompatActivity {

    private Button newChallangeButton;
    private Client client;
    private SharedPreferences prefs;

    private ArrayList<GameInfo> allGames;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_landing_page);

        String[] but = {
                "Jimmie\tvs.\tFred",
                "Jimmie\tvs.\tVictor",
                "Jimmie\tvs.\tBertil",
                "Jimmie\tvs.\tStefan",
                "Jimmie\tvs.\tStefan",
                "Jimmie\tvs.\tStefan",
                "Jimmie\tvs.\tStefan",
                "Jimmie\tvs.\tStefan",
                "Jimmie\tvs.\tStefan",
                "Jimmie\tvs.\tStefan",
                "Jimmie\tvs.\tStefan",
                "Jimmie\tvs.\tStefan",
                "Jimmie\tvs.\tWilliam" };

        Button [] buttonArrayReady = new Button[but.length];

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout gameLayout = (LinearLayout) findViewById(R.id.gameLayout);

        for (int i = 0; i < but.length; i++)
        {
            buttonArrayReady[i] = new Button(this);
            buttonArrayReady[i].setLayoutParams(params);

            LinearLayout.LayoutParams Btnparams = (LinearLayout.LayoutParams) buttonArrayReady[i].getLayoutParams();
            buttonArrayReady[i].setText(but[i]);
            buttonArrayReady[i].setId(i+1); // Setting the ids
            gameLayout.addView(buttonArrayReady[i], Btnparams);
        }

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

        //GAMES WAITING TO BE ACCEPTED OR DECLINED BEFORE STARTED.
        ArrayList<GameInfo> toBeAcceptedGames = new ArrayList<GameInfo>();

        //GAMES IN WHICH THERE IS YOUR TURN TO PLAY.
        ArrayList<GameInfo> yourTurnGames = new ArrayList<GameInfo>();

        //GAMES WHERE IT IS THE OTHER USERS TURN TO PLAY.
        ArrayList<GameInfo> otherTurnGames = new ArrayList<GameInfo>();

        //FINISHED GAMES.
        ArrayList<GameInfo> finishedGames = new ArrayList<GameInfo>();


        for(GameInfo g : allGames){
            switch(g.getState()){

                //GAMES TO BE ACCEPTED.
                case 0:

                    if(prefs.getInt("user_id", 0) == g.getOwnerID())
                        System.out.println("User has not accepted game yet.");
                    else if(prefs.getInt("user_id", 0) == g.getChallengedUserID())
                        toBeAcceptedGames.add(g);

                    break;

                //PLAYER 1 TURN.
                case 1:

                    if(prefs.getInt("user_id", 0) == g.getOwnerID())
                        yourTurnGames.add(g);

                    else if (prefs.getInt("user_id", 0) == g.getChallengedUserID())
                        otherTurnGames.add(g);

                    break;

                //PLAYER 2 TURN.
                case 2:

                    if(prefs.getInt("user_id", 0) == g.getOwnerID())
                        otherTurnGames.add(g);

                    else if(prefs.getInt("userd_id", 0) == g.getChallengedUserID())
                        yourTurnGames.add(g);

                    break;

                // FINISHED GAMES.
                case 3: finishedGames.add(g);
                    break;

                default: // WHAT?
                    break;
            }
        }

        /*

        SHOW GAMES IN AN APPROPRIATE WAY AND ADD LISTENERS FOR THEM TO START GAMES.

         */

        newChallangeButton = (Button)findViewById(R.id.challengeNewUserButton);
        newChallangeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MultiplayerLandingPage.this, MultiPlayerChallengeUser.class));
            }
        });
    }
}
