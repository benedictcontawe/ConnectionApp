package com.example.nfcapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView mTextViewExplanation, mTextViewStatus; //ECP 2017-01-16
    private MainViewModel viewModel;
    private NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) { Log.d(TAG,"onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextViewExplanation = (TextView) findViewById(R.id.text_view_explanation);
        mTextViewStatus = (TextView) findViewById(R.id.text_view_status);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        nfcAdapter = NfcAdapter.getDefaultAdapter(getBaseContext());
        viewModel.observeNFCState().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String nfcStatus) {
                mTextViewStatus.setText(nfcStatus);
            }
        });
    }

    @Override
    protected void onResume() { Log.d(TAG,"onResume()");
        super.onResume();
        viewModel.checkNFCStatus();
        final PendingIntent pendingIntent = viewModel.createPendingIntent(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
        if (nfcAdapter != null) //enabling foreground dispatch for getting intent from NFC event:
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, viewModel.getIntentFilter(), viewModel.techList);
        if (getIntent() != null)
            onNewIntent(getIntent());
    }

    @Override
    protected void onPause() { Log.d(TAG,"onPause()");
        super.onPause();
        viewModel.setNFC(NFCStatus.NoOperation);
        if (nfcAdapter != null)
            nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) { Log.d(TAG,"onNewIntent()");
        super.onNewIntent(intent);
        Log.d(ContentValues.TAG,"intent.getAction() " + intent.getAction() + " intent.getExtras()" + intent.getExtras());
        if (viewModel.isTagDiscovered(intent.getAction())) { Log.d(ContentValues.TAG, "onNewIntent " + NfcAdapter.ACTION_TAG_DISCOVERED);
            mTextViewExplanation.setText(viewModel.getByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)));
            viewModel.setNFC(NFCStatus.Read);
            Parcelable tagN = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if (tagN != null) { Log.d(ContentValues.TAG, "Parcelable OK");
                NdefMessage[] msgs;
                byte[] empty = new byte[0];
                byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
                String data = viewModel.dumpTagData(tagN);
                byte[] payload = data.getBytes();
                mTextViewExplanation.setText(viewModel.getDateTimeNow(data));
                viewModel.setNFC(NFCStatus.Read);
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload);
                NdefMessage msg = new NdefMessage(new NdefRecord[] { record });
                msgs = new NdefMessage[] { msg };
                Log.d(ContentValues.TAG,"Parcelable " + Arrays.toString(msgs));
            } else {
                Log.d(ContentValues.TAG, "Parcelable NULL");
            }
            Parcelable[] messages1 = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (messages1 != null) {
                Log.d(ContentValues.TAG, "Found " + messages1.length + " NDEF messages");
            } else {
                Log.d(ContentValues.TAG, "Not EXTRA_NDEF_MESSAGES");
            }
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            Ndef ndef = Ndef.get(tag);
            if(ndef != null) { Log.d(ContentValues.TAG, "onNewIntent: NfcAdapter.EXTRA_TAG");
                Parcelable[] messages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
                if (messages != null) {
                    Log.d(ContentValues.TAG, "Found " + messages.length + " NDEF messages");
                }
            } else {
                Log.d(ContentValues.TAG, "Write to an unformatted tag not implemented");
            }
        }
    }
}