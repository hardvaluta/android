package com.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;

import java.util.ArrayList;

public class MultiPlayerChallengeUser extends AppCompatActivity {

    private Button challengeButton;
    private EditText searchField;
    private ScrollView scrollView;
    private ArrayList<User> allUsers;
    private ArrayList<User> searchedUsers;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_user_page);

        challengeButton = (Button)findViewById(R.id.challengeButton);
        searchField = (EditText) findViewById(R.id.search_edittext);
        scrollView = (ScrollView)findViewById(R.id.scrollview_searchp);
        searchedUsers = new ArrayList<User>();

        Client.getInstance(this).getAllUsers(new VolleyCallback() {
            public void onSuccessResponse(Object o) {
                allUsers = (ArrayList<User>) o;
            }
        });

        challengeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                for(int i = 0; i < scrollView.getChildCount(); i++){
                    View v = scrollView.getChildAt(i);
                    if(v instanceof CheckBox){
                        if(((CheckBox) v).isChecked()){
                            //Challange user here
                        }
                    }
                }
            }
        });

        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String currText = searchField.getText().toString().trim();
                if(currText.length() > 2 && allUsers != null){
                    for(User u : allUsers){
                        if(u.getUsername().toLowerCase().contains(currText.toLowerCase()))
                            searchedUsers.add(u);

                    }
                } else {
                    System.out.println("Inga användare hittade");
                }


                scrollView.removeAllViews();
                //Update ScrollView.

                for(int i = 0; i < searchedUsers.size(); i++){
                    CheckBox checkBox = new CheckBox(scrollView.getContext());
                    checkBox.setId(i);
                    checkBox.setText(searchedUsers.get(i).getUsername());
                    scrollView.addView(checkBox);
                }
            }
        });

    }
}
