package hardvaluta.unnamed;

import android.content.Context;
import android.net.ConnectivityManager;

import com.firebase.client.Firebase;

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
    
}
