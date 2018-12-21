package com.melmo.androidchat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class ConnectivityChangeReceiver extends BroadcastReceiver {
    private static final String TAG = "ConnChangeRcv";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        Log.d(TAG, "onReceive: Connectivity change");
        Toast.makeText(context, "", Toast.LENGTH_SHORT).show();

        if(!isNetworkAvailable(context)) {
            Toast.makeText(context, "Pas de connexion! Veuillez vous connecter", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
