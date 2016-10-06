package com.android1337;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class LoginMenu extends AppCompatActivity {

    private EditText unameField, pwdField;
    private Button loginButton, signinButton;
    private Client client;
    private String uname, pwd;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_menu);

        client = Client.getInstance(this);

        unameField = (EditText) findViewById(R.id.uNameEditText);
        pwdField = (EditText) findViewById(R.id.pwdEditText);

        loginButton = (Button) findViewById(R.id.loginButton);
        signinButton = (Button) findViewById(R.id.signupButton);

        loginButton.setOnClickListener( v -> {
            uname = unameField.getText().toString().trim();
            pwd = unameField.getText().toString().trim();

            //TO DO
            client.getUser(uname, pwd, o -> {
                User u = (User) o;

            });

        });


        signinButton.setOnClickListener( v -> new Intent(LoginMenu.this, SignupMenu.class));


    }
}
