package com.hardvaluta.sbuilder;

import android.app.Application;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by victor on 2016-09-22.
 */


public class Client extends Application{

    private RequestQueue queue;
    private String url;
    private static Client client;
    private Question currentQuestion;

    private Client(){
        queue = Volley.newRequestQueue(this);
        url = "http://138.68.77.44/index.html";
        currentQuestion = null;
    }


    public static Client getInstance(){
        if(client == null)
            client = new Client();

        return client;
    }


    public void reqData(){

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            currentQuestion = null;
                            currentQuestion = new Question(response.getString("A"), response.getString("B"), response.getString("C"), response.getString("D"),
                                                            response.getString("correctAns"), response.getString("qText"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        );

        queue.add(getRequest);
    }

    public Question getCurrentQuestion(){
        return currentQuestion;
    }
}
