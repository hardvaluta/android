package com.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.ImageView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by victor on 2016-09-22.
 */


public class Client{


    private String url;

    private static Client client;
    private Cache cache;
    private Network network;
    private RequestQueue queue;

    private ArrayList<Question> questionArray;
    private JSONObject jsonQ;
    private int id;

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
        this.id = id;
        String t_url = url;

        switch(toRequest){

            case USER:
                t_url += "user/" + id;

                JsonArrayRequest getUserRequest = new JsonArrayRequest(Request.Method.GET, t_url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                try {

                                    JSONObject jsonQ = response.getJSONObject(0);

                                    callback.onSuccessResponse(new User(
                                            jsonQ.getString("username"),
                                            jsonQ.getInt("score"),
                                            jsonQ.getInt("games")));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
                );

                queue.add(getUserRequest);
                break;

            case QUESTION:

                t_url += "question/random";
                JSONObject jRequest = new JSONObject();
                JSONArray jArray=new JSONArray();

                try{

                    jRequest.put("difficulty", 1);
                    jRequest.put("count", id);
                    jArray.put(jRequest.get("difficulty"));
                    jArray.put(jRequest.get("count"));

                } catch(JSONException e) { }

                JsonArrayRequest getQuestionRequest = new JsonArrayRequest(Request.Method.GET, t_url, jArray,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                try {

                                    questionArray = new ArrayList<Question>();

                                    for (int n = 0; n < response.length(); n++) {

                                        System.out.println("n är: " + n);
                                        jsonQ = response.getJSONObject(n);

                                        Client.this.requestData(IMAGE, jsonQ.getInt("image"), (new VolleyCallback() {
                                            @Override
                                            public void onSuccessResponse(Object o) {

                                                try {

                                                    questionArray.add(new Question(
                                                            jsonQ.getString("answer_a"),
                                                            jsonQ.getString("answer_b"),
                                                            jsonQ.getString("answer_c"),
                                                            jsonQ.getString("answer_d"),
                                                            jsonQ.getString("text"),
                                                            (Bitmap) o));

                                                    if (questionArray.size() == Client.this.id)
                                                        callback.onSuccessResponse(questionArray);

                                                } catch (JSONException e) {
                                                }

                                            }
                                        }));

                                    }

                                } catch (JSONException e) {
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
                );

                queue.add(getQuestionRequest);
                break;


            case IMAGE:
                t_url += "image/" + id;

                ImageRequest getImageRequest = new ImageRequest(t_url,
                        new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap response) {
                                callback.onSuccessResponse(response);
                            }
                        },
                        0, 0, ImageView.ScaleType.FIT_XY, Bitmap.Config.ARGB_8888,
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        }
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

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}