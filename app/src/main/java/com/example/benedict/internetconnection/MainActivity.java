package com.example.benedict.internetconnection;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.TextView;
import java.io.IOException;

public class MainActivity extends Activity {
    private TextView txtInternet, txtPing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtInternet = (TextView) findViewById(R.id.txtInternet);
        txtPing = (TextView) findViewById(R.id.txtPing);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (hasInternet()){
            txtInternet.setText("Network Connection is available");
        }
        else if (!hasInternet()){
            txtInternet.setText("Network Connection is not available");
        }

        if (pingGoogle()) {
            txtPing.setText("Google Successfuly Ping");
        } else {
            txtPing.setText("Unreachable ping");
        }
    }

    private boolean hasInternet() {
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
}
