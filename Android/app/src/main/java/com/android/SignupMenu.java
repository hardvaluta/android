package com.android;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
        prefs = getSharedPreferences(SettingsActivity.PREF_FILE_NAME, MODE_PRIVATE);

        signup = (Button) findViewById(R.id.registerNewUserButton);
        uname = (EditText) findViewById(R.id.signup_unameEditText);

        pwd1 = (EditText) findViewById(R.id.signup_password1);
        pwd2 = (EditText) findViewById(R.id.signup_password2);



        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pwd1.getText().toString().equals(pwd2.getText().toString())) {

                    client.createUser(uname.getText().toString(), pwd1.getText().toString(), new VolleyCallback() {
                        @Override
                        public void onSuccessResponse(Object o) {
                            if(o instanceof User){
                                User u = (User) o;
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putInt("user_id", u.getId());
                                editor.putString("username", u.getUsername());
                                editor.putBoolean("active", true);
                                editor.commit();
                                startActivity(new Intent(SignupMenu.this, MainMenu.class));
                            } else {
                                alert();
                            }
                        }
                    });

                } else {
                    System.out.println("Lösenorden matchar ej");
                }
            }
        });
    }

    public void alert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SignupMenu.this);
        alertDialog.setTitle("Error");
        alertDialog.setMessage("Något gick fel.");
        alertDialog.setPositiveButton("Okej", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }
}

