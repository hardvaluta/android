package com.android1337;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class SignupMenu extends AppCompatActivity {


    private Button signup;
    private EditText uname, pwd1, pwd2;

    private Client client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_menu);

        client = Client.getInstance(this);

        signup = (Button) findViewById(R.id.registerNewUserButton);
        uname = (EditText) findViewById(R.id.signup_unameEditText);

        pwd1 = (EditText) findViewById(R.id.signup_password1);
        pwd2 = (EditText) findViewById(R.id.signup_password2);


        signup.setOnClickListener(v -> {
            if( ! pwd1.equals(pwd2)) {
                // LÖSENORDEN MATCHER EJ GÖR NÅGOT
            } else {
                client.createUser(uname.getText().toString(), pwd1.getText().toString(),
                        o -> {
                            User u = (User) o;
                            // GÖR NÅGOT.

                        });

            }
        });
    }
}
