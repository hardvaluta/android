package com.android;

import android.graphics.Rect;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.Space;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
    public static final String SCORE_FILE_NAME3 = "scoreFile276"; // Test
    public static final String SCORE_FILE_IDS = "scoreIdFile34";
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

    private void loadMoreGameEntries() {

            if (gameStrings != null) {
                LinearLayout linearLayout = (LinearLayout)scrollView.getChildAt(0);
                int i;
                int gameEntriesToLoad = Math.min(gameStrings.length-lastGameEntryShowing, gameEntriesToLoadEachTime);
                for (i = lastGameEntryShowing; i < lastGameEntryShowing + gameEntriesToLoad; ++i) {
                    Button tempButton = new Button(this);
                    buttons.add(i, tempButton);
                    tempButton.setText(i + ": " + gameStrings[i] + "\n\n");
                    linearLayout.addView(tempButton);

                    Space tempSpace = new Space(this);
                    tempSpace.setMinimumHeight(20);
                    linearLayout.addView(tempSpace);
                }
                lastGameEntryShowing = i;
            }
    }
}
