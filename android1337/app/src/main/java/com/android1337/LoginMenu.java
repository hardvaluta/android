package com.android1337;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class LoginMenu extends AppCompatActivity {

    private EditText unameField, pwdField;
    private Button loginButton, signupButton;
    private Client client;
    private String uname, pwd;
    private SharedPreferences prefs;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_menu);

        client = Client.getInstance(this);
        prefs = getSharedPreferences(MainMenu.PREF_FILE_NAME, 0);

        unameField = (EditText) findViewById(R.id.uNameEditText);
        pwdField = (EditText) findViewById(R.id.pwdEditText);

        loginButton = (Button) findViewById(R.id.loginButton);
        signupButton = (Button) findViewById(R.id.signupButton);

        loginButton.setOnClickListener( v -> {
            uname = unameField.getText().toString().trim();
            pwd = unameField.getText().toString().trim();

            //TO DO
            client.getUser(uname, pwd, o -> {
                User u = (User) o;
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("username", u.getUsername());
                editor.putBoolean("active", true);
                editor.commit();
            });

        });


        signupButton.setOnClickListener( v -> startActivity(new Intent(LoginMenu.this, SignupMenu.class)));


    }
}
