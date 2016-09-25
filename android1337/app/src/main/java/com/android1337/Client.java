package com.android1337;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Random;

/**
 * Created by victor on 2016-09-22.
 */


public class Client{

    private RequestQueue queue;
    private String url;
    private static Client client;

    private Client(Context context){
        queue = Volley.newRequestQueue(context);
        url = "http://philiplaine.com/";
    }


    public static Client getInstance(Context context){
        if(client == null)
            client = new Client(context);

        return client;
    }


    public void reqQuestion(int id, final VolleyCallback callback){
        String t_url = url;
        t_url += ("question/"+id);

        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, t_url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject jsonQ;
                            int nrObjects = response.length();
                            if(nrObjects==1){
                                jsonQ=response.getJSONObject(0);
                            }
                            else{
                                Random random = new Random();
                                jsonQ = response.getJSONObject(random.nextInt(nrObjects-1));
                            }

                            Question q = new Question(jsonQ.getString("answer_a"), jsonQ.getString("answer_b"), jsonQ.getString("answer_c"),
                                    jsonQ.getString("answer_d"), jsonQ.getString("text"));

                            callback.onSuccessResponse(q);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        );
        queue.add(getRequest);
    }

    public void reqUser(int id, final VolleyCallback callback){
        String t_url = url;
        t_url += ("user/all/list");
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, t_url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject jsonQ = response.getJSONObject(0);
                            User u = new User(jsonQ.getString("username"), jsonQ.getInt("score"), jsonQ.getInt("games"));

                            callback.onSuccessResponse(u);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        );
        queue.add(getRequest);
    }

    public void updateUser(int id, final VolleyCallback callback) {

    }
}
