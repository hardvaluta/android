package com.android;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

        try {
            client = Client.getInstance(this);
        } catch (Exception e) {
            alert("Error", "Ingen internetanslutning.");
        }
        prefs = getSharedPreferences(MainMenu.PREF_FILE_NAME, MODE_PRIVATE);


        unameField = (EditText) findViewById(R.id.uNameEditText);
        pwdField = (EditText) findViewById(R.id.pwdEditText);

        loginButton = (Button) findViewById(R.id.loginButton);
        signupButton = (Button) findViewById(R.id.signupButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uname = unameField.getText().toString().trim();
                pwd = pwdField.getText().toString().trim();

                client.loginUser(uname, pwd, new VolleyCallback() {
                    @Override
                    public void onSuccessResponse(Object o) {
                        if(o instanceof User){
                            User u = (User) o;
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putInt("user_id", u.getId());
                            editor.putString("username", u.getUsername());
                            editor.putBoolean("active", true);
                            editor.commit();

                            startActivity(new Intent(LoginMenu.this, SettingsActivity.class));
                        } else {
                            alert("Error", "Något gick fel");
                        }
                    }
                });
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginMenu.this, SignupMenu.class));
            }
        });
    }


    public void alert(String title, String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginMenu.this);
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton("Okej", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }
}
