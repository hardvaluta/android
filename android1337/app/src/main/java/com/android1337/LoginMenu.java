package com.android1337;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
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
        prefs = getSharedPreferences(MainMenu.PREF_FILE_NAME, MODE_PRIVATE);


        unameField = (EditText) findViewById(R.id.uNameEditText);
        pwdField = (EditText) findViewById(R.id.pwdEditText);

        loginButton = (Button) findViewById(R.id.loginButton);
        signupButton = (Button) findViewById(R.id.signupButton);

        loginButton.setOnClickListener( v -> {
            uname = unameField.getText().toString().trim();
            pwd = unameField.getText().toString().trim();

            //TO DO
            client.getUser(uname, pwd, o -> {
                if(o instanceof User){
                    User u = (User) o;
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("user_id", u.getId());
                    editor.putString("username", u.getUsername());
                    editor.putBoolean("active", true);
                    editor.commit();
                } else {
                    alert();
                }

            });

        });


        signupButton.setOnClickListener( v -> startActivity(new Intent(LoginMenu.this, SignupMenu.class)));


    }


    public void alert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginMenu.this);
        alertDialog.setTitle("Error");
        alertDialog.setMessage("Något gick fel.");
        alertDialog.setPositiveButton("Okej", (d, v) -> d.cancel());
        alertDialog.show();
    }
}