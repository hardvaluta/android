package com.android1337;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class SignupMenu extends AppCompatActivity {


    private Button signup;
    private EditText uname, pwd1, pwd2;

    private Client client;
    private SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_menu);

        client = Client.getInstance(this);

        signup = (Button) findViewById(R.id.registerNewUserButton);
        uname = (EditText) findViewById(R.id.signup_unameEditText);

        pwd1 = (EditText) findViewById(R.id.signup_password1);
        pwd2 = (EditText) findViewById(R.id.signup_password2);

        prefs = getSharedPreferences(MainMenu.PREF_FILE_NAME, 0);

        signup.setOnClickListener(v -> {
            if(pwd1.getText().toString().equals(pwd2.getText().toString())) {

                client.createUser(uname.getText().toString(), pwd1.getText().toString(), o -> {
                    User u = (User) o;
                    // GÖR NÅGOT.
                    System.out.println("Du är inloggad som: " + u.getUsername());
                    startActivity(new Intent(SignupMenu.this, MainMenu.class));
                });

            } else {
                System.out.println("Lösenorden matchar ej");
            }
        });
    }
}
