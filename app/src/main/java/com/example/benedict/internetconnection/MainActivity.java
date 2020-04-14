package com.example.benedict.internetconnection;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity {

    private TextView txtInternet, txtPing;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtInternet = (TextView) findViewById(R.id.txtInternet);
        txtPing = (TextView) findViewById(R.id.txtPing);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        observeData();
    }

    private void observeData() {
        viewModel.getLiveInternet().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean value) {
                if (value) {
                    txtInternet.setText("Network Connection is available");
                } else if (!value) {
                    txtInternet.setText("Network Connection is not available");
                }
            }
        });

        viewModel.getLivePing().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean value) {
                if (value) {
                    txtPing.setText("Google Successfuly Ping");
                } else {
                    txtPing.setText("Unreachable ping");
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewModel.unsetConnectivity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.setConnectivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
