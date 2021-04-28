package com.example.ufoodlibrary.utilities;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.ufoodlibrary.firebase.FirebaseInterface;
import com.example.ufoodlibrary.firebase.FirebaseManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Alfonso on 02-Apr-16.
 */
public class G3Application extends Application {

    private static Context appContext;
    private static boolean networkAvailable;
    public static FirebaseInterface fManager;
    public static final String SHARED_PREFERENCE = "uFood_shared";
    public static final String UNREAD_NOTIFICATION = "unread_notification";
    public static final String LAST_TIMESTAMP = "last_timestamp";
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    public void onCreate() {
        super.onCreate();

        appContext = getApplicationContext();

        networkAvailable = checkNetwork();

        fManager = FirebaseManager.newInstance();

        setAuthListeners();

    }

    private boolean checkNetwork() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        else{
            return false;
        }
    }

    private void setAuthListeners() {
        FirebaseAuth mAuth;

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user;
                if (firebaseAuth != null) {
                    user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        // User is signed in
                        fManager.setAccountId(user.getUid());

                        Log.d("ApplicationCtx", "onAuthStateChanged:signed_in:" + user.getUid());

                    } else {
                        // User is signed out
                        fManager.setAccountId(null);
                        Log.d("ApplicationCtx", "onAuthStateChanged:signed_out");

                    }
                }

            }
        };

        mAuth.addAuthStateListener(mAuthListener);
    }

    public static boolean isNetworkAvailable() {
        return networkAvailable;
    }

    public static void setNetworkAvailable(boolean networkAvailable) {
        G3Application.networkAvailable = networkAvailable;
    }

    public static Context getAppContext() {
        return appContext;
    }

}
