package com.example.nfcapp;

import android.app.Application;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Build;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class MainViewModel extends AndroidViewModel {

    private final static String TAG = MainViewModel.class.getSimpleName();
    private final static String prefix = "android.nfc.tech.";

    private final MutableLiveData<NFCStatus> liveNFCStatus;
    private final MutableLiveData<String> liveNFCState;

    public MainViewModel(final Application application) {
        super(application);
        Log.d(TAG, "constructor");
        liveNFCStatus = new MutableLiveData<NFCStatus>();
        liveNFCState = new MutableLiveData<String>();
    }
    //region NFC Status Methods
    public void checkNFCStatus() { Log.d(TAG, "checkNFCStatus()");
        if (NFCManager.isSupportedAndEnabled(getApplication())) {
            liveNFCStatus.setValue(NFCStatus.Tap);
            liveNFCState.setValue(getApplication().getString(R.string.please_tap_at_nfc_sensor));
        } else if (NFCManager.isNotEnabled(getApplication())) {
            liveNFCStatus.setValue(NFCStatus.NotEnabled);
            liveNFCState.setValue(getApplication().getString(R.string.please_enable_your_nfc));
        } else if (NFCManager.isNotSupported(getApplication())) {
            liveNFCStatus.setValue(NFCStatus.NotSupported);
            liveNFCState.setValue(getApplication().getString(R.string.nfc_not_supported));
        }
    }

    public void setNFC(NFCStatus status) {
        liveNFCStatus.setValue(status);
    }

    public LiveData<String> observeNFCState() {
        return liveNFCState;
    }
    //endregion
    //region NFC Process Methods, list of NFC technologies detected:
    public PendingIntent createPendingIntent(Context context, int requestCode, Intent intent) {
        final PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(context, requestCode, intent, 0);
        }
        return pendingIntent;
    }

    public final String[][] techList = new String[][] {
        new String[] {
            NfcA.class.getName(),
            NfcB.class.getName(),
            NfcF.class.getName(),
            NfcV.class.getName(),
            IsoDep.class.getName(),
            MifareClassic.class.getName(),
            MifareUltralight.class.getName(), Ndef.class.getName()
        }
    };

    public IntentFilter[] getIntentFilter() {
        //region creating intent receiver for NFC events:
        IntentFilter filter = new IntentFilter();
        filter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_TECH_DISCOVERED);
        //endregion
        return new IntentFilter[]{filter};
    }

    public String dumpTagData(final Parcelable parcelable) { Log.d(TAG,"dumpTagData(" + parcelable + ")");
        StringBuilder sb = new StringBuilder();
        Tag tag = (Tag) parcelable;
        byte[] id = tag.getId();
        sb.append("Tag ID (hex): ").append(getHex(id)).append("\n");
        sb.append("Tag ID (dec): ").append(getDec(id)).append("\n");
        sb.append("ID (reversed): ").append(getReversed(id)).append("\n");
        sb.append("Technologies: ");
        for (String tech : tag.getTechList()) {
            sb.append(tech.substring(prefix.length()));
            sb.append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        for (String tech : tag.getTechList()) {
            if (tech.equals(MifareClassic.class.getName())) {
                sb.append('\n');
                MifareClassic mifareTag = MifareClassic.get(tag);
                String type = "Unknown";
                switch (mifareTag.getType()) {
                    case MifareClassic.TYPE_CLASSIC:
                        type = "Classic";
                        break;
                    case MifareClassic.TYPE_PLUS:
                        type = "Plus";
                        break;
                    case MifareClassic.TYPE_PRO:
                        type = "Pro";
                        break;
                }
                sb.append("Mifare Classic type: ");
                sb.append(type);
                sb.append('\n');

                sb.append("Mifare size: ");
                sb.append(mifareTag.getSize() + " bytes");
                sb.append('\n');

                sb.append("Mifare sectors: ");
                sb.append(mifareTag.getSectorCount());
                sb.append('\n');

                sb.append("Mifare blocks: ");
                sb.append(mifareTag.getBlockCount());
            }

            if (tech.equals(MifareUltralight.class.getName())) {
                sb.append('\n');
                MifareUltralight mifareUlTag = MifareUltralight.get(tag);
                String type = "Unknown";
                switch (mifareUlTag.getType()) {
                    case MifareUltralight.TYPE_ULTRALIGHT:
                        type = "Ultralight";
                        break;
                    case MifareUltralight.TYPE_ULTRALIGHT_C:
                        type = "Ultralight C";
                        break;
                }
                sb.append("Mifare Ultralight type: ");
                sb.append(type);
            }
        }
        Log.d(TAG, "Datum: " + sb);
        Log.d(ContentValues.TAG, "dumpTagData Return \n" + sb);
        return sb.toString();
    }

    private String ByteArrayToHexString(final byte [] inarray) { Log.d(ContentValues.TAG, "ByteArrayToHexString " + Arrays.toString(inarray));
        int i, j, in;
        String [] hex = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
        String out = "";
        for(j = 0 ; j < inarray.length ; ++j) {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        //CE7AEED4
        //EE7BEED4
        Log.d(TAG, "ByteArrayToHexString" + String.format("%0" + (inarray.length * 2) + "X", new BigInteger(1,inarray)));
        return out;
    }

    public String getByteArrayToHexString(final byte [] inarray) { Log.d(TAG, "getByteArrayToHexString " + Arrays.toString(inarray));
        Log.d(ContentValues.TAG, "getByteArrayToHexString " + Arrays.toString(inarray));
        Log.d(ContentValues.TAG, "getByteArrayToHexString Return " + ByteArrayToHexString(inarray));
        return "NFC Tag\n" + ByteArrayToHexString(inarray);
    }

    public String getDateTimeNow(final String data) { Log.d(TAG,"getDateTime(" + data + ")");
        DateFormat TIME_FORMAT = SimpleDateFormat.getDateTimeInstance();
        Date now = new Date();
        Log.d(ContentValues.TAG,"getDateTimeNow() Return \n" + TIME_FORMAT.format(now) + '\n' + data);
        return TIME_FORMAT.format(now) + '\n' + data;
    }

    private String getHex(final byte[] bytes) { Log.d(TAG,"getHex()");
        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; --i) {
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
            if (i > 0) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    private long getDec(final byte[] bytes) { Log.d(TAG,"getDec()");
        long result = 0;
        long factor = 1;
        for (int i = 0; i < bytes.length; ++i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    private long getReversed(final byte[] bytes) { Log.d(TAG,"getReversed()");
        long result = 0;
        long factor = 1;
        for (int i = bytes.length - 1; i >= 0; --i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    public boolean isTagDiscovered(@Nullable final String action) {
        return action.equals(NfcAdapter.ACTION_TAG_DISCOVERED);
    }
    //endregion
}