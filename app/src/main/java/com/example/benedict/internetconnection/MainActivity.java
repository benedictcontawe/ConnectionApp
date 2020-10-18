package com.example.benedict.internetconnection;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity {

    private static final int INTERNET_STATE = 0;
    private TextView txtInternet, txtPing, txtMetered;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtInternet = (TextView) findViewById(R.id.txtInternet);
        txtPing = (TextView) findViewById(R.id.txtPing);
        txtMetered = (TextView) findViewById(R.id.txtMetered);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        observeData();
    }

    private void observeData() {
        viewModel.observeLiveInternet().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean value) {
                requestPermissions(viewModel.checkPermission());
                if (value) {
                    txtInternet.setText("Network Connection is available");
                } else {
                    txtInternet.setText("Network Connection is not available");
                }
            }
        });

        viewModel.observeLivePing().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean value) {
                requestPermissions(viewModel.checkPermission());
                if (value) {
                    txtPing.setText("Google Successfuly Ping");
                    txtMetered.setVisibility(View.VISIBLE);
                } else {
                    txtPing.setText("Google Unreachable ping");
                    txtMetered.setVisibility(View.INVISIBLE);
                }
            }
        });

        viewModel.observeLiveMeter().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean value) {
                if (value) {
                    txtMetered.setText("Network Connection is Metered");
                } else {
                    txtMetered.setText("Network Connection is not Metered");
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            viewModel.unregisterConnectivity();
        } catch (Exception ex) {
            showAppPermissionSettings();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestPermissions(viewModel.checkPermission());
        try {
            viewModel.registerConnectivity();
            viewModel.checkConnections();
        } catch (Exception ex) {
            showAppPermissionSettings();
        }
    }

    private void requestPermissions(boolean permissionGranted) {
        if (!permissionGranted) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.INTERNET,
                            Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.ACCESS_NETWORK_STATE,
                    },
                    INTERNET_STATE
            );
        }
    }

    private void showAppPermissionSettings() {
        Toast.makeText(this,"Internet Permissions Disabled",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", getPackageName(), null));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}