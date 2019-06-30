package com.example.benedict.bluetootharduino;

/**
 * Created by Benedict on 22/03/2017.
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class BTManager {
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice remoteDevice;
    private BluetoothSocket socket;
    public OutputStream outStream;
    public InputStream inStream;
    private String TAG = "tBlue";

    public void BTInitialize() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public boolean BTAvailable() {
        if (mBluetoothAdapter == null) {
// Device does not support Bluetooth
            return false;
        }
        return true;
    }

    public boolean BTenabled() {
        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
            return true;
        }
        return false;
    }

    public void BTdisabled() {
        mBluetoothAdapter.disable();
    }

    public void write(String packetCharacter)
    {
        Log.i(TAG, "Sending \""+packetCharacter+"\"... ");
        byte[] outBuffer= packetCharacter.getBytes();
        try {
            outStream.write(outBuffer);
        } catch (IOException e) {
            Log.e(TAG, "Write failed.", e);
        }
    }

    public boolean BTconnect(String MacAddress) {
        MacAddress = MacAddress.toUpperCase();
        remoteDevice = mBluetoothAdapter.getRemoteDevice(MacAddress);
        Log.i(TAG, "Creating RFCOMM socket...");
        try {
            Method m = remoteDevice.getClass().getMethod("createRfcommSocket",
                    new Class[] { int.class });
            socket = (BluetoothSocket) m.invoke(remoteDevice, 1);
            Log.i(TAG, "RFCOMM socket created.");
        } catch (NoSuchMethodException e) {
            Log.i(TAG, "Could not invoke createRfcommSocket.");
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            Log.i(TAG, "Bad argument with createRfcommSocket.");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            Log.i(TAG, "Illegal access with createRfcommSocket.");
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            Log.i(TAG, "Invocation target exception: createRfcommSocket.");
            e.printStackTrace();
        }
        Log.i(TAG, "Got socket for device " + socket.getRemoteDevice());
        mBluetoothAdapter.cancelDiscovery();
        Log.i(TAG, "Connecting socket...");
        try {
            socket.connect();
            Log.i(TAG, "Socket connected.");
        } catch (IOException e) {
            try {
                Log.e(TAG, "Failed to connect socket. ", e);
                socket.close();
                Log.e(TAG, "Socket closed because of an error. ", e);
            } catch (IOException eb) {
                Log.e(TAG, "Also failed to close socket. ", eb);
            }
            return false;
        }
        try {
            outStream = socket.getOutputStream();
            Log.i(TAG, "Output stream open.");
            inStream = socket.getInputStream();
            Log.i(TAG, "Input stream open.");
        } catch (IOException e) {
            Log.e(TAG, "Failed to create output stream.", e);
        }
        return true;
    }
}
