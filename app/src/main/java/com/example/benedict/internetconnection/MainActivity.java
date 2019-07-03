package com.example.benedict.internetconnection;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity{
    private TextView txtData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtData = (TextView) findViewById(R.id.txtData);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (hasInternet()){
            txtData.setText("Network Connection is available");
        }
        else if (!hasInternet()){
            txtData.setText("Network Connection is not available");
        }
    }

    private boolean hasInternet(){
        boolean hasWifi = false;
        boolean hasMobileData = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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
}
