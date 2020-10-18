package com.example.benedict.internetconnection;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.io.IOException;

public class MainViewModel extends AndroidViewModel {

    private static String TAG = MainViewModel.class.getSimpleName();
    private ConnectivityManager connectivityManager;
    private ConnectivityManager.NetworkCallback networkCallback;
    private NetworkReceiver networkReceiver;
    private MutableLiveData<Boolean> liveInternet = new MutableLiveData<>();
    private MutableLiveData<Boolean> livePing = new MutableLiveData<>();

    public MainViewModel(Application application) {
        super(application);
        connectivityManager = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkReceiver = new NetworkReceiver(this);
    }
    //region Register Unregister Internet Detection Callback
    public void registerConnectivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            setNetworkCallback();
            connectivityManager.registerDefaultNetworkCallback(networkCallback);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setNetworkCallback();
            connectivityManager.registerNetworkCallback(setNetworkRequest(),networkCallback);
        } else {
            getApplication().registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    public void unregisterConnectivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            connectivityManager.unregisterNetworkCallback(networkCallback);
        } else {
            getApplication().unregisterReceiver(networkReceiver);
        }
    }
    //endregion
    //region ConnectivityManager initialization of NetworkCallback and NetworkRequest
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setNetworkCallback() {
        networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                super.onAvailable(network);
                Log.d(TAG,"onAvailable(" + network + ")");
                pingAll();
            }

            @Override
            public void onUnavailable() {
                super.onUnavailable();
                Log.d(TAG,"onUnavailable()");
                pingAll();
            }

            @Override
            public void onLost(Network network) {
                super.onLost(network);
                Log.d(TAG,"onLost(" + network + ")");
                pingAll();
            }

            @Override
            public void onCapabilitiesChanged(Network network,NetworkCapabilities networkCapabilities) {
                super.onCapabilitiesChanged(network, networkCapabilities);
                if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)) {
                    Log.d(TAG,"Serve Higher Quality Content");
                } else  {
                    Log.d(TAG,"Serve Lower Quality Content");
                }
            }
        };
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private NetworkRequest setNetworkRequest() {
        return new NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .build();
    }
    //endregion
    //region LiveData Observers
    public LiveData<Boolean> getLiveInternet() {
        return liveInternet;
    }

    public LiveData<Boolean> getLivePing() {
        return livePing;
    }
    //endregion
    public void pingAll() {
        liveInternet.postValue(hasInternet());
        livePing.postValue(pingGoogle());
    }
    //region Ping methods for checking of internet and ping google
    private boolean hasInternet() {
        boolean hasWifi = false;
        boolean hasMobileData = false;
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
        for (NetworkInfo info : networkInfos){
            if (info.getTypeName().equalsIgnoreCase("WIFI"))
                if (info.isConnected())
                    hasWifi = true;
            if (info.getTypeName().equalsIgnoreCase("MOBILE"))
                if (info.isConnected())
                    hasMobileData = true;
        }
        Log.d(TAG,"hasInternet() : " + String.valueOf(hasMobileData || hasWifi));
        return hasMobileData || hasWifi;
    }

    private boolean pingGoogle() {
        String command = "ping -c 1 google.com";
        try {
            return Runtime.getRuntime().exec(command).waitFor() == 0;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    //endregion
    //region Manifest Permissions
    public boolean checkPermission() {
        boolean value = false;
        Log.d(TAG,"checkPermission()");
        if (isInternetPermissionGranted()) {
            Log.d(TAG,"isInternetPermissionGranted()");
            value = true;
        }
        if (isAccessWifiStatePermissionGranted()) {
            Log.d(TAG,"isAccessWifiStatePermissionGranted()");
            value = true;
        }
        if (isAccessNetworkStatePermissionGranted()) {
            Log.d(TAG,"isAccessNetworkStatePermissionGranted()");
            value = true;
        }
        return value;
    }

    private boolean isInternetPermissionGranted() {
        return ActivityCompat
                .checkSelfPermission(
                        getApplication(),
                        Manifest.permission.INTERNET
                ) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isAccessWifiStatePermissionGranted() {
        return ActivityCompat
                .checkSelfPermission(
                        getApplication(),
                        Manifest.permission.ACCESS_WIFI_STATE
                ) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isAccessNetworkStatePermissionGranted() {
        return ActivityCompat
                .checkSelfPermission(
                        getApplication(),
                        Manifest.permission.ACCESS_NETWORK_STATE
                ) == PackageManager.PERMISSION_GRANTED;
    }
    //endregion
    @Override
    protected void onCleared() {
        super.onCleared();
        connectivityManager = null;
    }
}
