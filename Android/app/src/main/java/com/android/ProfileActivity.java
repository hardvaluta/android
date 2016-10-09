package com.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.FileInputStream;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        String string = "";
        try {
            FileInputStream fis = openFileInput(MainMenu.SCORE_FILE_NAME);
            byte[] buffer = new byte[500];
            fis.read(buffer, 0, 500);
            string = new String(buffer);
        } catch (Exception e) { e.printStackTrace(); }

        ((TextView)findViewById(R.id.textView2)).setText(string);

    }
}
