package com.android;

import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import static android.R.attr.id;
import static android.R.attr.password;
import static android.R.attr.type;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by victor on 2016-09-22.
 */


public class Client{


    private String url;

    private static Client client;
    private Cache cache;
    private Network network;
    private RequestQueue queue;

    private SharedPreferences prefs;

    public static final int QUESTION = 0x01;
    public static final int IMAGE = 0x02;

    private ArrayList<Question> questionArray;
    private ArrayList<User> users;
    private ArrayList<GameInfo> currentGames;

    private JSONObject jsonQ;

    //frågor att hämta.
    private final int count = 4;

    private Client(Context context){

        cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024);
        network = new BasicNetwork(new HurlStack());

        queue = new RequestQueue(cache, network);
        queue.start();

        url = "http://philiplaine.com/";

        prefs = context.getSharedPreferences(MainMenu.PREF_FILE_NAME, Context.MODE_PRIVATE);
    }


    public static Client getInstance(Context context) throws Exception {
        if(isNetworkAvailable(context)){
            if(client == null)
                client = new Client(context);

            return client;
        } else {
            throw new Exception("Network not available");
        }
    }

    private void requestImage(int image_id, final VolleyCallback callback){
        String t_url = "data/" + image_id;

        ImageRequest getImageRequest = new ImageRequest(t_url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        callback.onSuccessResponse(response);
                    }
                },
                0, 0, ImageView.ScaleType.FIT_XY, Bitmap.Config.ARGB_8888,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) { }
                }
        );

        queue.add(getImageRequest);
    }

    public void requestRoundMemoryGame(final VolleyCallback callback){
        //TO DO
    }

    public void requestRoundSentenceGame(final VolleyCallback callback){
        String t_url = url + "type/1";
        JSONObject body = new JSONObject();

        try {
            body.put("count", count);
        } catch (JSONException e) { }

        JsonArrayRequest requestSentenceRound = new JsonArrayRequest(Request.Method.GET, t_url, body, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    questionArray = new ArrayList<Question>();

                    for(int i = 0; i < response.length(); i++){

                        jsonQ = response.getJSONObject(i);

                        requestImage(jsonQ.getInt("image"), new VolleyCallback() {

                            public void onSuccessResponse(Object o) {
                                try {

                                    questionArray.add(new Question(
                                            jsonQ.getString("answer_a"),
                                            jsonQ.getString("answer_b"),
                                            jsonQ.getString("answer_c"),
                                            jsonQ.getString("answer_d"),
                                            jsonQ.getString("text"),
                                            (Bitmap) o));

                                    if (questionArray.size() == count)
                                        callback.onSuccessResponse(questionArray);

                                } catch (JSONException e) {}
                            }
                        });
                    }

                } catch (JSONException e) {}

            }
        }, new Response.ErrorListener() { public void onErrorResponse(VolleyError error) {} });

        queue.add(requestSentenceRound);
    }


    public void createUser(String uname, String password, final VolleyCallback callback) {

        JSONObject body = new JSONObject();

        String t_url = url + "user/create";

        try {

            body.put("username", uname);
            body.put("password", password);

        } catch(JSONException e) { }

        JsonArrayRequest createUserRequest = new JsonArrayRequest(Request.Method.POST, t_url, body,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject jUser = response.getJSONObject(0);
                            callback.onSuccessResponse(new User(
                                    jUser.getString("username"),
                                    jUser.getInt("score"),
                                    jUser.getInt("id")));

                        } catch (JSONException e) {
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onSuccessResponse(null);
                    }
                }
        );

        queue.add(createUserRequest);
    }

    public void loginUser(String uname, String password, final VolleyCallback callback){
        String t_url = url + "user/authenticate";

        JSONObject body = new JSONObject();

        try {

            body.put("username", uname);
            body.put("password", password);

        } catch(JSONException e) { }
        
        JsonArrayRequest loginUserRequest = new JsonArrayRequest(Request.Method.POST, t_url, body,
                new Response.Listener<JSONArray>() {

                    public void onResponse(JSONArray response) {
                        try {

                            JSONObject jsonQ = response.getJSONObject(0);

                            callback.onSuccessResponse(new User(
                                    jsonQ.getString("username"),
                                    jsonQ.getInt("id"),
                                    jsonQ.getInt("score")));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                    new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError error) {
                            callback.onSuccessResponse(null);
                        }
                });

        queue.add(loginUserRequest);
    }

    public void getAllUsers(final VolleyCallback callback){
        String t_url = url + "user/all";

        users = new ArrayList<User>();

        JsonArrayRequest requestAllUSers = new JsonArrayRequest(Request.Method.GET, t_url, new Response.Listener<JSONArray>() {
            public void onResponse(JSONArray response) {

                for(int i = 0; i < response.length(); i++){
                    try {

                        JSONObject obj = response.getJSONObject(i);
                        users.add( new User( obj.getString("username"), obj.getInt("score"), obj.getInt("id")));
                    } catch (JSONException e) { }
                }

                callback.onSuccessResponse(users);
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                callback.onSuccessResponse(null);
                System.out.println("ERROR, "+error.toString());
            }
        });

        queue.add(requestAllUSers);

    }

    public void challengeUser(int id_to_challenge, final VolleyCallback callback){
        String t_url = url + "user/" + id_to_challenge + "/challenge";
        JSONObject body = new JSONObject();
        try {
            body.put("context_id", prefs.getInt("user_id", 0));
            //Hardcoded type 1 (Sentence game) for now.
            body.put("type", 1);
        } catch (JSONException e) {};

        JsonArrayRequest challenge = new JsonArrayRequest(Request.Method.POST, t_url, body, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                System.out.println("User challenged.");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(challenge);

    }

    public void declineChallenge(int game_id, final VolleyCallback callback){

        String t_url = url + "game/" + game_id + "/decline";
        JSONObject body = new JSONObject();
        try {
            body.put("context_id", prefs.getInt("user_id", 0));
        } catch (JSONException e) {}

        JsonArrayRequest decline = new JsonArrayRequest(Request.Method.POST, t_url, body, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                System.out.println("Challenge declined");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(decline);
    }

    public void acceptChallenge(int game_id, final VolleyCallback callback){
        String t_url = url + "game/" + game_id + "accept";
        JSONObject body = new JSONObject();
        try {
            body.put("context_id", prefs.getInt("user_id", 0));
        } catch (JSONException e) {}

        JsonArrayRequest accept = new JsonArrayRequest(Request.Method.POST, t_url, body, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                System.out.println("Challenge accepted");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(accept);
    }

    public void getCurrGames(final VolleyCallback callback){

        String t_url = url + "game/list";
        JSONObject body = new JSONObject();
        try {
            body.put("context_id", prefs.getInt("user_id", 0));
        } catch (JSONException e) { }


        JsonArrayRequest getGamesById = new JsonArrayRequest(Request.Method.GET, t_url, body, new Response.Listener<JSONArray>() {
            public void onResponse(JSONArray response) {
                for(int i = 0; i < response.length(); i++){
                    try {
                        JSONObject t = response.getJSONObject(i);
                        currentGames.add( new GameInfo (
                                t.getInt("id"),
                                t.getInt("player1"),
                                t.getInt("player2"),
                                t.getInt("state"),
                                t.getInt("type"),
                                client
                        ) );

                    } catch (JSONException e) { }
                }

                callback.onSuccessResponse(currentGames);
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(getGamesById);
    }

    public void reportProgress(int game_id, int player_id, int correct){
        String t_url = url + "game/" + game_id + "/progress";

        JSONObject body = new JSONObject();
        try {
            body.put("correct", correct);
            body.put("player", player_id);
        } catch(JSONException e) {}

        JsonArrayRequest reportProg = new JsonArrayRequest(Request.Method.POST, t_url, body, new Response.Listener<JSONArray>() {
            public void onResponse(JSONArray response) {
                System.out.println("Progress reported.");
            }
        }, new Response.ErrorListener() { public void onErrorResponse(VolleyError error) {} });

        queue.add(reportProg);
    }

    private static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}