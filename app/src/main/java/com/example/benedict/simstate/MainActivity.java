package com.example.benedict.simstate;

import android.Manifest;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity {

    private static final int PHONE_STATE = 0;
    private TextView txtSim;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtSim = (TextView) findViewById(R.id.txtSim);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        observeData();
    }

    private void observeData() {
        viewModel.getLiveSimState().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean value) {
                if (value) {
                    txtSim.setText("Sim is inserted");
                } else {
                    txtSim.setText("Sim is not inserted");
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewModel.unregisterSimState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestPermissions(viewModel.checkPermission());
        viewModel.registerSimState();
        viewModel.checkSimState();
    }

    private void requestPermissions(boolean permissionGranted) {
        if (!permissionGranted) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.READ_PHONE_STATE
                    },
                    PHONE_STATE
            );
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
