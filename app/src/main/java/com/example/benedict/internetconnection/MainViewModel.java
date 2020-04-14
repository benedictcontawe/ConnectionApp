package com.example.benedict.internetconnection;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.io.IOException;

public class MainViewModel extends AndroidViewModel {

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

    public void setConnectivity() {
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

    public void unsetConnectivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            connectivityManager.unregisterNetworkCallback(networkCallback);
        } else {
            getApplication().unregisterReceiver(networkReceiver);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setNetworkCallback() {
        networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                super.onAvailable(network);
                Log.d("MainViewModel","onAvailable(" + network + ")");
                pingAll();
            }

            @Override
            public void onUnavailable() {
                super.onUnavailable();
                Log.d("MainViewModel","onUnavailable()");
                pingAll();
            }

            @Override
            public void onLost(Network network) {
                super.onLost(network);
                Log.d("MainViewModel","onLost(" + network + ")");
                pingAll();
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

    public LiveData<Boolean> getLiveInternet() {
        return liveInternet;
    }

    public LiveData<Boolean> getLivePing() {
        return livePing;
    }

    public void pingAll() {
        liveInternet.postValue(hasInternet());
        livePing.postValue(pingGoogle());
    }

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

    @Override
    protected void onCleared() {
        super.onCleared();
        connectivityManager = null;
    }
}
