package com.example.benedict.internetconnection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

class NetworkReceiver extends BroadcastReceiver {

    private static final String TAG = NetworkReceiver.class.getSimpleName();
    public MainViewModel mainViewModel;

    public NetworkReceiver(MainViewModel mainViewModel) {
        Log.d(TAG,"constructor(" + mainViewModel + ")");
        this.mainViewModel = mainViewModel;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"onReceive(" + context + "," + intent + ")");
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            mainViewModel.checkConnections();
        }
    }
}