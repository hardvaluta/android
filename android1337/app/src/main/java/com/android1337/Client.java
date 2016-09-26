package com.android1337;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageRequest;
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

/**
 * Created by victor on 2016-09-22.
 */


public class Client{


    private String url;

    private static Client client;
    private Cache cache;
    private Network network;
    private RequestQueue queue;

    public static final int USER = 0x01;
    public static final int QUESTION = 0x02;
    public static final int IMAGE = 0x03;

    private Client(Context context){

        cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024);
        network = new BasicNetwork(new HurlStack());

        queue = new RequestQueue(cache, network);
        queue.start();

        url = "http://philiplaine.com/";
    }


    public static Client getInstance(Context context){
        if(client == null)
            client = new Client(context);

        return client;
    }


    public void requestData(int toRequest, int id, final VolleyCallback callback){
        String t_url;

        switch(toRequest){

            case USER:
                t_url = url + "user/" + id;

                JsonArrayRequest getUserRequest = new JsonArrayRequest(Request.Method.GET, t_url, null,
                        (response) ->  {
                                try {
                                    JSONObject jsonQ = response.getJSONObject(0);
                                    User u = new User(jsonQ.getString("username"), jsonQ.getInt("score"), jsonQ.getInt("games"));

                                    callback.onSuccessResponse(u);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                        }, (error) -> {}
                );

                queue.add(getUserRequest);
                break;

            case QUESTION:
                t_url = url + "question/" + id;

                JsonArrayRequest getQuestionRequest = new JsonArrayRequest(Request.Method.GET, t_url, null,
                        (response) -> {
                            try {

                                JSONObject jsonQ = response.getJSONObject(0);
                                Question q = new Question(jsonQ.getString("answer_a"), jsonQ.getString("answer_b"), jsonQ.getString("answer_c"),
                                        jsonQ.getString("answer_d"), jsonQ.getString("text"));

                                callback.onSuccessResponse(q);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }, (error) -> {}
                );

                queue.add(getQuestionRequest);
                break;


            case IMAGE:
                t_url = url + "image/" + id;

                ImageRequest getImageRequest = new ImageRequest(t_url,
                        (bitmap) -> callback.onSuccessResponse(bitmap),
                        0, 0, ImageView.ScaleType.CENTER_INSIDE, null,
                        (error) -> {}
                );

                queue.add(getImageRequest);
                break;


            default:
                break;
        }

    }

    public void updateUser(int id, final VolleyCallback callback) {
        /*TO DO*/
    }

}
