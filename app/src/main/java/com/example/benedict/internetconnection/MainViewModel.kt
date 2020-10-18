package com.example.benedict.internetconnection

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.io.IOException

class MainViewModel : AndroidViewModel {

    companion object {
        private val TAG : String = MainViewModel::class.java.getSimpleName()
    }

    private val connectivityManager : ConnectivityManager
    private lateinit var networkCallback : ConnectivityManager.NetworkCallback
    private val networkReceiver : NetworkReceiver
    private val liveInternet : MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private val livePing  : MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private val liveMeter  : MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    constructor(application : Application) : super(application) {
        connectivityManager = getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        networkReceiver = NetworkReceiver(this@MainViewModel)
    }
    //region Register Unregister Internet Detection Callback
    public fun registerConnectivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            setNetworkCallback()
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setNetworkCallback()
            connectivityManager.registerNetworkCallback(setNetworkRequest(), networkCallback)
        } else {
            getApplication<Application>().registerReceiver(
                networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            )
        }
    }

    public fun unregisterConnectivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            connectivityManager?.unregisterNetworkCallback(networkCallback)
        } else {
            getApplication<Application>().unregisterReceiver(networkReceiver)
        }
    }
    //endregion
    //region ConnectivityManager initialization of NetworkCallback and NetworkRequest
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private fun setNetworkCallback() { Log.d(TAG, "setNetworkCallback()")
        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) { Log.d(TAG, "onAvailable($network)")
                super.onAvailable(network)
                checkConnections()
            }

            override fun onUnavailable() { Log.d(TAG, "onUnavailable()")
                super.onUnavailable()
                checkConnections()
            }

            override fun onLost(network : Network) { Log.d(TAG, "onLost($network)")
                super.onLost(network)
                checkConnections()
            }

            override fun onCapabilitiesChanged(network : Network?, networkCapabilities : NetworkCapabilities) {
                super.onCapabilitiesChanged(network, networkCapabilities)
                if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)) {
                    Log.d(TAG, "Serve Higher Quality Content")
                    liveMeter.postValue(true)
                } else {
                    Log.d(TAG, "Serve Lower Quality Content")
                    liveMeter.postValue(false)
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private fun setNetworkRequest() : NetworkRequest { Log.d(TAG, "setNetworkRequest()")
        return NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()
    }
    //endregion
    //region LiveData Observers
    open fun observeLiveInternet(): LiveData<Boolean> {
        return liveInternet
    }

    public fun observeLivePing(): LiveData<Boolean> {
        return livePing
    }

    public fun observeLiveMeter(): LiveData<Boolean> {
        return liveMeter
    }
    //endregion
    public fun checkConnections() {
        liveInternet.postValue(hasInternet())
        livePing.postValue(pingGoogle())
    }
    //region Ping methods for checking of internet and ping google
    private fun hasInternet() : Boolean { Log.d(TAG, "hasInternet()")
        var hasWifi = false
        var hasMobileData = false
        val networkInfos = connectivityManager.allNetworkInfo
        for (info in networkInfos) {
            if (info.typeName.equals("WIFI", ignoreCase = true))
                if (info.isConnected) hasWifi = true
            if (info.typeName.equals("MOBILE", ignoreCase = true))
                if (info.isConnected) hasMobileData = true
        }
        return hasMobileData || hasWifi
    }

    private fun pingGoogle() : Boolean { Log.d(TAG, "pingGoogle()")
        val command = "ping -c 1 google.com"
        return try {
            Runtime.getRuntime().exec(command).waitFor() == 0
        } catch (e: InterruptedException) { e.printStackTrace()
            false
        } catch (e: IOException) { e.printStackTrace()
            false
        }
    }
    //endregion
    //region Manifest Permissions
    public fun checkPermission() : Boolean {
        var value = false
        Log.d(TAG, "checkPermission()")
        if (isInternetPermissionGranted()) {
            Log.d(TAG, "isInternetPermissionGranted()")
            value = true
        }
        if (isAccessWifiStatePermissionGranted()) {
            Log.d(TAG, "isAccessWifiStatePermissionGranted()")
            value = true
        }
        if (isAccessNetworkStatePermissionGranted()) {
            Log.d(TAG, "isAccessNetworkStatePermissionGranted()")
            value = true
        }
        return value
    }

    private fun isInternetPermissionGranted() : Boolean {
        return ActivityCompat
            .checkSelfPermission(
                getApplication(), Manifest.permission.INTERNET
            ) == PackageManager.PERMISSION_GRANTED
    }

    private fun isAccessWifiStatePermissionGranted() : Boolean {
        return ActivityCompat
            .checkSelfPermission(
                getApplication(), Manifest.permission.ACCESS_WIFI_STATE
            ) == PackageManager.PERMISSION_GRANTED
    }

    private fun isAccessNetworkStatePermissionGranted() : Boolean {
        return ActivityCompat
            .checkSelfPermission(
                getApplication(), Manifest.permission.ACCESS_NETWORK_STATE
            ) == PackageManager.PERMISSION_GRANTED
    }

    //endregion
    override fun onCleared() {
        super.onCleared()
    }
}