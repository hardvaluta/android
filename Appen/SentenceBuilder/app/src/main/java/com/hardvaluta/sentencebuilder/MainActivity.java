package com.hardvaluta.sentencebuilder;

import android.app.ActionBar;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.Firebase;

public class MainActivity extends Activity {

    DBHandler db;

    private Button A, B, C, D;
    private TextView text;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);

        db = DBHandler.getInstance();

        text = (TextView)findViewById(R.id.qtext);
        A = (Button)findViewById(R.id.buttonA);
        B = (Button)findViewById(R.id.buttonB);
        C = (Button)findViewById(R.id.buttonC);
        D = (Button)findViewById(R.id.buttonD);

        db.readData();
        Question q = db.getCurrentQuestion();

        text.setText(q.getqText());
        A.setText(q.getA());
        B.setText(q.getB());
        C.setText(q.getC());
        D.setText(q.getD());
       // db.writeData("fittprogram");


    }
}
