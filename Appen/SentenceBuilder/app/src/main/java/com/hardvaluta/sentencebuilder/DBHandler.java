package com.hardvaluta.sentencebuilder;

import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Ref;

/**
 * Created by victor on 2016-09-21.
 */
public class DBHandler {

    private DatabaseReference mRef;
    private static DBHandler db;
    private Question currentQuestion;

    private DBHandler(){
        mRef = FirebaseDatabase.getInstance().getReference();
        currentQuestion = new Question();

    }


    public static DBHandler getInstance(){
        if(db == null){
            db = new DBHandler();
        } return db;
    }


    public void readData(){
        mRef.child("questions").child("0").addListenerForSingleValueEvent(new ValueEventListener() {

            public void onDataChange(DataSnapshot dataSnapshot) {
;               currentQuestion = dataSnapshot.getValue(Question.class);
               // System.out.println(dataSnapshot.getValue());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    public Question getCurrentQuestion(){
        return currentQuestion;
    }

    public void writeData(String s){
        mRef.child("questions").child("0").child("qText").setValue(s);
    }
}
