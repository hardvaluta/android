package com.hardvaluta.sbuilder;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity {


    private Button buttonA, buttonB, buttonC, buttonD;
    private TextView textView;
    private Client client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        client = Client.getInstance(this);


        buttonA = (Button) findViewById(R.id.button);
        buttonB = (Button) findViewById(R.id.button2);
        buttonC = (Button) findViewById(R.id.button3);
        buttonD = (Button) findViewById(R.id.button4);
        textView = (TextView) findViewById(R.id.textView);

        client.reqQuestion(1, new VolleyCallback() {

            public void onSuccessResponse(Question q) {
                buttonA.setText(q.getA());
                buttonB.setText(q.getB());
                buttonC.setText(q.getC());
                buttonD.setText(q.getD());
                textView.setText(q.getText());
            }
        });




    }
}
