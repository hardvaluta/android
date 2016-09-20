package hardvaluta.unnamed;

import android.content.Context;
import android.net.ConnectivityManager;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by William on 2016-09-20.
 */
public class DatabaseHandler {
    Context activity;
    Firebase myFirebaseRef;

    public DatabaseHandler(Context activity){
        this.activity=activity;
        Firebase.setAndroidContext(activity);
        myFirebaseRef = new Firebase("https://<YOUR-FIREBASE-APP>.firebaseio.com/");
    }

    private Boolean isConnected(){
        ConnectivityManager connectivityManager =(ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getActiveNetworkInfo().getState()!=null){
            return true;
        }
        return false;
    }

    public void createUser(final String eMail, final String password){
        if(isConnected()){
            myFirebaseRef.child("users").createUser(eMail, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
                @Override
                public void onSuccess(Map<String, Object> result) {
                    //User successfully created
                    //System.out.println("Successfully created user account with id: " + result.get("uid"));
                    logInUser(eMail, password);

                }
                @Override
                public void onError(FirebaseError firebaseError) {
                    // Error on creation
                }
            });
        }
        else{
            //Inform about no internet connectivity!
        }
    }

    public void logInUser(String eMail, String password){
        if(isConnected()){
            myFirebaseRef.child("users").authWithPassword(eMail,password, new Firebase.AuthResultHandler() {
                @Override
                public void onAuthenticated(AuthData authData) {
                    // Successfully logged in
                }

                @Override
                public void onAuthenticationError(FirebaseError error) {
                    // Failed to log in
                }
            });
        }
        else{
            //Inform about no internet connectivity
        }
    }

    public void addPuzzle(String phrase, String[] solutions){
        if(isConnected()) {
            //Lär krävas loopning och tillklistrande av alla solutions i slutet då de teoretiskt sätt kan variera i antal?
            String sPuzzle = "{\"phrase\":\"HÄR SKA PHRASE IN\",[\"solutions\":\"HÄR SKA SOLUTIONS IN\"]}";
            try{
                JSONObject jPuzzle = new JSONObject(sPuzzle);
                myFirebaseRef.child("puzzles").setValue(sPuzzle);
            }catch(JSONException e){}
        }
        else {
            //Inform about no internet connectivity!
        }
    }

    public JSONObject getPuzzle(){
        //Implement
        return null;
    }

    public void updateDifficulty(String name, int modification){
        //Implement
    }
}
