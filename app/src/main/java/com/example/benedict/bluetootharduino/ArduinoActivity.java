package com.example.benedict.bluetootharduino;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class ArduinoActivity extends Activity implements OnClickListener{
    String MacAddress;
    BTManager BTM;
    ToggleButton toggle_connect,toggle_Key;
    TextView TextViewConnectionStatus,TextViewMacAddress;
    boolean isConnected = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arduino);

        GetBundleInfo();
        init();
    }

    private void init() {
        BTM = new BTManager();

        toggle_connect = (ToggleButton)findViewById(R.id.toggle_connect);
        toggle_Key = (ToggleButton)findViewById(R.id.toggle_Key);

        TextViewConnectionStatus = (TextView)findViewById(R.id.TextViewConnectionStatus) ;
        TextViewMacAddress = (TextView)findViewById(R.id.TextViewMacAddress);

        TextViewMacAddress.setText(MacAddress);

        toggle_connect.setOnClickListener(this);
        toggle_Key.setOnClickListener(this);
    }

    public void GetBundleInfo(){
        if(getIntent() != null) {
            MacAddress = getIntent().getExtras().getString("MACADDRESS");
            Log.e("CHECKING",MacAddress);
        }
        else
        {
            Log.e("NO DATA","CHECK CODES");
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.toggle_connect){
            if(toggle_connect.isChecked()) {
                try {
                    initializeConnection();
                    BTM.BTconnect(MacAddress);
                    TextViewConnectionStatus.setText("Bluetooth Connection Enabled");
                    isConnected = true;
                    BTM.write("C");
                }
                catch (Exception ex){
                    BTM.write("E");
                    Toast.makeText(getApplicationContext(), "CONNECTION ERROR: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                    toggle_connect.setChecked(false);
                }
            }
            else {
                BTM.write("D");
                BTM.BTdisabled();
                TextViewConnectionStatus.setText("Bluetooth Connection disabled");
                isConnected = false;
                onBackPressed();
            }
        }
        else if(v.getId() == R.id.toggle_Key){
            if(toggle_Key.isChecked()) {
                BTM.write("A");
                Toast.makeText(getApplicationContext(), "A", Toast.LENGTH_SHORT).show();
            }
            else {
                BTM.write("B");
                Toast.makeText(getApplicationContext(), "B", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initializeConnection() {
        BTM.BTInitialize();
        if(BTM.BTAvailable()) {
            BTM.BTenabled();
        }
    }
    protected void onPause() {
        super.onPause();
        Toast.makeText(getApplicationContext(), "onPause", Toast.LENGTH_SHORT).show();
        BTM.BTdisabled();
        initializeConnection();
    }
    protected void onStop() {
        super.onStop();
        Toast.makeText(getApplicationContext(), "onStop", Toast.LENGTH_SHORT).show();
        BTM.BTdisabled();
        initializeConnection();
    }
}
