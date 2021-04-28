package com.example.ufoodlibrary.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.ufoodlibrary.utilities.G3Application;

/**
 * Created by Alfonso on 22-May-16.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {

        if(checkInternet(context))
        {
            G3Application.setNetworkAvailable(true);
        }
        else{
            G3Application.setNetworkAvailable(false);
        }

    }


    boolean checkInternet(Context context) {

        if (isNetworkAvailable(context)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

}
