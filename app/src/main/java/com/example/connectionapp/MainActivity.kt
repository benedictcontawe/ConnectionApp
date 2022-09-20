package com.example.connectionapp

import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.connectionapp.databinding.MainBinder
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

public class MainActivity : AppCompatActivity, CompoundButton.OnCheckedChangeListener,NfcAdapter.ReaderCallback {

    companion object {
        private val TAG = MainActivity::class.java.getSimpleName()
    }

    private var binder : MainBinder? = null
    private val viewModel : MainViewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }

    constructor() {

    }

    override fun onCreate(savedInstanceState : Bundle?) {
        binder = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
        binder?.setViewModel(viewModel)
        binder?.setLifecycleOwner(this@MainActivity)
        super.onCreate(savedInstanceState)
        binder?.toggleButton?.setOnCheckedChangeListener(this@MainActivity)
        Coroutines.main(this@MainActivity, { scope ->
            scope.launch( block = { binder?.getViewModel()?.observeNFCStatus()?.collectLatest ( action = { status ->
                if (status == NFCStatus.NoOperation) NFCManager.disableReaderMode(this@MainActivity, this@MainActivity)
                else if (status == NFCStatus.Tap) NFCManager.enableReaderMode(this@MainActivity, this@MainActivity, this@MainActivity, NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK, Bundle())
            }) })
            scope.launch( block = { binder?.getViewModel()?.observeToast()?.collectLatest ( action = { message ->
                Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
            }) })
            scope.launch( block = { binder?.getViewModel()?.observeLiveData()?.collectLatest ( action = { tag ->
                binder?.textViewExplanation?.setText(tag)
            }) })
        })
    }

    override fun onCheckedChanged(buttonView : CompoundButton?, isChecked : Boolean) {
        if (buttonView == binder?.toggleButton)
            viewModel.onCheckNFC(isChecked)
    }

    override fun onTagDiscovered(tag : Tag?) {
        binder?.getViewModel()?.readTag(tag)
    }
}