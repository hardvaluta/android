package com.android1337;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private Button login_logoutButton;
    private TextView loginInfoTextView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefs = getSharedPreferences(MainMenu.PREF_FILE_NAME, MODE_PRIVATE);

        loginInfoTextView = (TextView)findViewById(R.id.loginInfoTextView);
        login_logoutButton = (Button)findViewById(R.id.login_logoutButton);

        if(prefs.getBoolean("active", false)){
            //ANVÄNDAREN ÄR INLOGGAD.
            String uname = prefs.getString("username", "");
            loginInfoTextView.setText("Hej " + uname + "! Du är inloggad.");
            login_logoutButton.setText("Logga ut");
            login_logoutButton.setOnClickListener(v -> {

                SharedPreferences.Editor editor = prefs.edit();
                editor.remove("username");
                editor.remove("active");
                editor.remove("user_id");
                editor.apply();

                Intent intent = getIntent();
                finish();
                startActivity(intent);

            });

        } else {
            //ANVÄNDAREN ÄR INTE INLOGGAD.
            loginInfoTextView.setText("Du är inte inloggad.");
            login_logoutButton.setText("Logga in");
            login_logoutButton.setOnClickListener(v -> startActivity(new Intent(SettingsActivity.this, LoginMenu.class)));
        }

    }
}
