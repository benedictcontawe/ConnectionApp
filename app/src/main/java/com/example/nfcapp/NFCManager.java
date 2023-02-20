package com.example.nfcapp;

import android.app.Activity;
import android.content.Context;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;

public class NFCManager {

    private static final String TAG = NFCManager.class.getSimpleName();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void enableReaderMode(Context context, Activity activity, NfcAdapter.ReaderCallback callback, int flags, Bundle extras) {
        try {
            NfcAdapter.getDefaultAdapter(context).enableReaderMode(activity, callback, flags, extras);
        } catch (UnsupportedOperationException ex) { Log.e(TAG,"UnsupportedOperationException " + ex.getMessage(), ex);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void disableReaderMode(Context context, Activity activity) {
        try {
            NfcAdapter.getDefaultAdapter(context).disableReaderMode(activity);
        } catch (UnsupportedOperationException ex) { Log.e(TAG,"UnsupportedOperationException " + ex.getMessage(), ex);

        }
    }

    public static Boolean isSupported(NfcAdapter nfcAdapter) {
        if (nfcAdapter == null) return false;
        else return true;
    }

    public static Boolean isNotSupported(NfcAdapter nfcAdapter) {
        if (nfcAdapter == null) return true;
        else return false;
    }

    public static Boolean isEnabled(NfcAdapter nfcAdapter) {
        if (nfcAdapter == null) return false;
        else return nfcAdapter.isEnabled();
    }

    public static Boolean isNotEnabled(NfcAdapter nfcAdapter) {
        if (nfcAdapter == null) return true;
        else return !nfcAdapter.isEnabled();
    }

    public static Boolean isSupportedAndEnabled(NfcAdapter nfcAdapter) {
        return isSupported(nfcAdapter) && isEnabled(nfcAdapter);
    }
}