package com.example.benedict.internetconnection;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity {

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
                } else if (!value) {
                    txtSim.setText("Sim is not inserted");
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
