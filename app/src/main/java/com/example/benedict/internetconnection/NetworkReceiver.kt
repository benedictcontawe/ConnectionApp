package com.example.benedict.internetconnection

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log

class NetworkReceiver : BroadcastReceiver {

    companion object {
        private val TAG : String = NetworkReceiver::class.java.getSimpleName()
        private var networkReceiver : NetworkReceiver? = null
        fun newInstance(connectivityViewModel : MainViewModel) : NetworkReceiver {
            return networkReceiver?:NetworkReceiver(connectivityViewModel)
        }
    }

    private val mainViewModel : MainViewModel

    constructor(mainViewModel : MainViewModel) {
        this.mainViewModel = mainViewModel
    }

    override fun onReceive(context : Context?, intent : Intent?) {
        Log.d(TAG, "onReceive($context,$intent)")
        if (intent?.action == ConnectivityManager.CONNECTIVITY_ACTION) {
            mainViewModel.checkConnections()
        }
    }
}