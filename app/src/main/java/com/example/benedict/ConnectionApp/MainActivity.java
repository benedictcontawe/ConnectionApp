package com.example.benedict.ConnectionApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends Activity implements OnTouchListener,OnItemClickListener {
    AnimationClass AC;
    Button btnBluetoothFindPairedMacAddress,btnScan;
    ListView ListDeviceAndMacAddressesDevices;
    private Rect rect;
    private BluetoothAdapter myBluetooth = null;

    String plist[];
    String Device_MAC_Address[];

    List<String> DiscoveredDevice;
    List<String> MAC_ADDRESS;

    ArrayAdapter<String> adapter;
    private int DISCOVERY_REQUEST = 1;

    ProgressBar progress_bar;

    //ArrayList<BluetoothDevice> mDeviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        checkBluetooth();

        /*
        plist = new String[2];
        plist[0] = "Device Name - MAC Address";
        plist[1] = "Device Name - 00:12:06:13:27:40";
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, plist);
        ListDeviceAndMacAddressesDevices.setAdapter(adapter);
        */
    }

    private void checkBluetooth() {
        if(myBluetooth == null)
        {
            //Show a message. that the device has no bluetooth adapter
            Toast.makeText(getApplicationContext(), "Bluetooth Device Not Available", Toast.LENGTH_LONG).show();
            //finish apk
            finish();
        }
        else
        {
            if (myBluetooth.isEnabled()){
                Toast.makeText(getApplicationContext(),"Bluetooth Enabled" , Toast.LENGTH_LONG).show();
            }
            else{
                //Ask to the user turn the bluetooth on
                Intent turnBluetoothOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(turnBluetoothOn,DISCOVERY_REQUEST);
            }
        }
    }

    private void init() {
        AC = new AnimationClass();

        btnBluetoothFindPairedMacAddress = (Button)findViewById(R.id.btnBluetoothFindPairedMacAddress);
        btnScan = (Button)findViewById(R.id.btnScan);

        ListDeviceAndMacAddressesDevices = (ListView)findViewById(R.id.ListMacAddressesDevices);

        progress_bar = (ProgressBar)findViewById(R.id.progress_bar);

        btnBluetoothFindPairedMacAddress.setOnTouchListener(this);
        btnScan.setOnTouchListener(this);
        ListDeviceAndMacAddressesDevices.setOnItemClickListener(this);

        myBluetooth = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Write your code to perform an action on down
                rect = new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
                return true;
            case MotionEvent.ACTION_MOVE:
                // Write your code to perform an action on continuous touch move
                return true;
            case MotionEvent.ACTION_UP:
                // Write your code to perform an action on touch up
                if(rect.contains(view.getLeft() + (int) x, view.getTop() + (int) y)){
                    // User moved inside bounds
                    Toast.makeText(getApplicationContext(),"Scanning. . ." , Toast.LENGTH_SHORT).show();
                    if(view.getId() == R.id.btnBluetoothFindPairedMacAddress){
                        pairedDevicesList();
                        DisableButtonAndFadeOut();
                    }
                    else if(view.getId() == R.id.btnScan){
                        scanDeviceList();
                        DisableButtonAndFadeOut();
                    }
                }
                return true;
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(getApplicationContext(), "Position item index :" + position + "  ListItem item value : " + ListDeviceAndMacAddressesDevices.getItemAtPosition(position), Toast.LENGTH_LONG).show();
        //Toast.makeText(getApplicationContext(), String.valueOf(ListDeviceAndMacAddressesDevices.getItemAtPosition(position)).substring(String.valueOf(ListDeviceAndMacAddressesDevices.getItemAtPosition(position)).indexOf("-") + 1), Toast.LENGTH_SHORT).show();
        if(position != 0) { //Call Intent
            Intent i = new Intent(this, ArduinoActivity.class);
            i.putExtra("MACADDRESS", Device_MAC_Address[position]);
            //i.putExtra("MACADDRESS", String.valueOf(ListDeviceAndMacAddressesDevices.getItemAtPosition(position)).substring(String.valueOf(ListDeviceAndMacAddressesDevices.getItemAtPosition(position)).indexOf("-") +2));
            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
        else{
            //Toast.makeText(getApplicationContext(), "Cannot be selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void pairedDevicesList() {
        try {
            Set<BluetoothDevice> paired_devices = myBluetooth.getBondedDevices();

            if (paired_devices.size() > 0) {
                plist = new String[paired_devices.size()+1];
                Device_MAC_Address = new String[paired_devices.size()+1];
                plist[0] = "Device Name | MAC Address";
                Device_MAC_Address[0] = " ";
                int j = 1;
                for (BluetoothDevice device : paired_devices) {
                    plist[j] = device.getName().toString() + " - " + device.getAddress().toString();
                    Device_MAC_Address[j] = device.getAddress().toString();
                    j++;
                }
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, plist);
                ListDeviceAndMacAddressesDevices.setAdapter(adapter);
            } else {
                Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception ex){
            Toast.makeText(getApplicationContext(), "ERROR: "+ ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void scanDeviceList(){
        EnableDiscoverable();
        myBluetooth.startDiscovery();
        DiscoveredDevice = new ArrayList<String>();
        DiscoveredDevice.add("Device Name | MAC Address");

        MAC_ADDRESS = new ArrayList<String>();
        MAC_ADDRESS.add(" ");
    }

    private void EnableDiscoverable() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, filter);
    }

    public void getDescoveredDevice(){
        Toast.makeText(getApplicationContext(), "getDescoveredDevice", Toast.LENGTH_SHORT).show();
        Device_MAC_Address = new String[MAC_ADDRESS.size()+1];
        for(int count = 0;count < DiscoveredDevice.size();count++) {
            Device_MAC_Address[count] = MAC_ADDRESS.get(count);
        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, DiscoveredDevice);
        ListDeviceAndMacAddressesDevices.setAdapter(adapter);
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                if (state == BluetoothAdapter.STATE_ON) {
                    //showToast("ACTION_STATE_CHANGED: STATE_ON");
                }
            }

            else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                //mDeviceList = new ArrayList<>();
                //showToast("ACTION_DISCOVERY_STARTED");
                //mProgressDlg.show();
                progress_bar.setVisibility(View.VISIBLE);
            }

            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //mProgressDlg.dismiss();
                progress_bar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "ACTION_DISCOVERY_FINISHED", Toast.LENGTH_SHORT).show();
                getDescoveredDevice();
            }

            else if (BluetoothDevice.ACTION_FOUND.equals(action)) {// When discovery finds a device
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //mDeviceList.add(device);

                DiscoveredDevice.add(device.getName() + " | " + device.getAddress());
                MAC_ADDRESS.add(device.getAddress());
                Toast.makeText(getApplicationContext(), "Device found = " + device.getName(), Toast.LENGTH_SHORT).show();
            }
        }
    };

    public void DisableButtonAndFadeOut(){
        btnBluetoothFindPairedMacAddress.setEnabled(false);
        btnScan.setEnabled(false);

        AC.fadeOutAndHideButton(btnBluetoothFindPairedMacAddress, 750);
        AC.fadeOutAndHideButton(btnScan, 750);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setCancelable(false);
        dialog.setTitle("Leaving already?");
        dialog.setMessage("Are you sure you want to exit the game");
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //Action for "OK".
                EXIT();
            }
        })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Action for "Cancel".
                    }
                });

        final AlertDialog alert = dialog.create();
        alert.show();
    }

    private void EXIT() {
        finish();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        }
    }
}
