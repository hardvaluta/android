package com.android;

import android.graphics.Rect;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.Space;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ProfileActivity extends AppCompatActivity {
    public static final String SCORE_FILE_NAME = "scoreFile";
    public static final String SCORE_FILE_NAME3 = "scoreFile226786"; // Test
    public static final String SCORE_FILE_IDS = "scoreIdFile26314";
    private NestedScrollView scrollView;
    private TextView staticTextView;
    private float scrolledPercentage = 0f;
    private String[] gameStrings;
    private int gameEntriesToLoadEachTime = 75;
    private int lastGameEntryShowing = 0;
    private ArrayList<Button> buttons = new ArrayList<Button>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        scrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        staticTextView = (TextView)findViewById(R.id.staticTextView);
        staticTextView.setText("DET HÄR ÄR TOPPEN!\nDET HÄR ÄR TOPPEN!\nDET HÄR ÄR TOPPEN!\nDET HÄR ÄR TOPPEN!\nDET HÄR ÄR TOPPEN!\nDET HÄR ÄR TOPPEN!\nDET HÄR ÄR TOPPEN!\nDET HÄR ÄR TOPPEN!\nDET HÄR ÄR TOPPEN!\n");
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                scrolledPercentage = 100f*(float)scrollY/(float)(v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight());
                System.out.println(scrolledPercentage);

                if (scrolledPercentage > 50f) {
                    loadMoreGameEntries();
                }

            }
        });


        String string = "";
        int readNrOfBytes = gameEntriesToLoadEachTime*100;
        try {
            FileInputStream fis = openFileInput(ProfileActivity.SCORE_FILE_NAME3);
            byte[] buffer = new byte[gameEntriesToLoadEachTime * 100];
            while (readNrOfBytes==gameEntriesToLoadEachTime*100) {
                readNrOfBytes = fis.read(buffer, 0, gameEntriesToLoadEachTime * 100);
                if (readNrOfBytes < gameEntriesToLoadEachTime * 100) {
                    buffer = Arrays.copyOf(buffer, readNrOfBytes);
                }
                string += new String(buffer);
            }
        } catch (Exception e) { e.printStackTrace(); }

        gameStrings = string.split("\n");
        Collections.reverse(Arrays.asList(gameStrings));
        loadMoreGameEntries();


    }

    /**
     * The different csv formats:
     *
     */
    private void loadMoreGameEntries() {

            if (gameStrings != null) {
                LinearLayout linearLayout = (LinearLayout)scrollView.getChildAt(0);
                int i;
                View tempView = null;
                int gameEntriesToLoad = Math.min(gameStrings.length-lastGameEntryShowing, gameEntriesToLoadEachTime);
                for (i = lastGameEntryShowing; i < lastGameEntryShowing + gameEntriesToLoad; ++i) {
                    if ((tempView = parseGameEntry(gameStrings[i])) != null) {
                        linearLayout.addView(tempView);
                        Space tempSpace = new Space(this);
                        tempSpace.setMinimumHeight(20);
                        linearLayout.addView(tempSpace);
                    }
                }
                lastGameEntryShowing = i;
            }
    }

    /**
     * Game data from the csv strings
     *  GameOne Singleplayer:
     *  Score is based on how many words you tried before getting the right one. Max is 16. Min is 4.
     *  1,1,<(long) UNIQUE ID/SYSTEM TIME WHEN SAVED>,<(int) SCORE (more is more)>
     *  GameTwo Singleplayer:
     *  Score is number of cards you had to flip to find all pairs. Less flips is better. Min is 12. Max is infinite.
     *  1,2,<(long) UNIQUE ID/SYSTEM TIME WHEN SAVED>,<(int) SCORE (less is more)>
     *  GameOne Multiplayer:
     *  Score is based on how many words you tried before getting the right one. Max is 16. Min is 4.
     *  2,1,<DATABASE-WIDE UNIQUE ID>,<OWNER ID>,<OWNER USER NAME>,<OWNER SCORE(more is more)>,<SLAVE ID>,<SLAVE USER NAME>,<SLAVE SCORE(more is more)>
     *  GameTwo Multiplayer:
     *  Score is time. Fewer seconds is better.
     *  2,2,<DATABASE-WIDE UNIQUE ID>,<OWNER ID>,<OWNER USER NAME>,<OWNER SCORE(less is more)>,<SLAVE ID>,<SLAVE USER NAME>,<SLAVE SCORE(less is more)>
     */
    private View parseGameEntry(String gameEntryString) {
        RelativeLayout gameEntryLayout = new RelativeLayout(this);
        TextView gameEntryTextView = new TextView(this);
        gameEntryLayout.addView(gameEntryTextView);
        String[] gameDataStringArray = gameEntryString.split(",");
        switch (gameDataStringArray[0]) {
            case "1": // Single player
                switch (gameDataStringArray[1]) {
                    case "1": // GameOne
                        gameEntryTextView.setText("Singleplayer gameone: " + gameDataStringArray[3]);
                        break;
                    case "2": // GameTwo
                        gameEntryTextView.setText("Singleplayer gametwo: " + gameDataStringArray[3]);
                        break;
                    default:
                        return null;
                }
                break;
            case "2": // Multiplayer
                switch (gameDataStringArray[1]) {
                    case "1": // GameOne
                        gameEntryTextView.setText(
                                                    "Multiplayer gameone:\n"
                                                    + gameDataStringArray[4] + " got score "
                                                    + gameDataStringArray[5] + "\n"
                                                    + gameDataStringArray[7] + " got score "
                                                    + gameDataStringArray[8]);
                        break;
                    case "2": // GameTwo
                        gameEntryTextView.setText(
                                "Multiplayer gametwo:\n"
                                        + gameDataStringArray[4] + " got score "
                                        + gameDataStringArray[5] + "\n"
                                        + gameDataStringArray[7] + " got score "
                                        + gameDataStringArray[8]);
                        break;
                    default:
                        return null;
                }
                break;
            default:
                return null;
        }
        return gameEntryLayout;
    }

}
