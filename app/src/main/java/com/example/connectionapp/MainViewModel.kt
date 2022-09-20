package com.example.connectionapp

import android.app.Application
import android.content.ContentValues
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.MifareClassic
import android.nfc.tech.MifareUltralight
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.experimental.and

public class MainViewModel : AndroidViewModel {

    companion object {
        private val TAG = MainViewModel::class.java.getSimpleName()
        private const val prefix = "android.nfc.tech."
    }

    private val liveNFC : MutableStateFlow<NFCStatus?>
    private val liveToast : MutableSharedFlow<String?>
    private val liveTag : MutableStateFlow<String?>

    constructor(application : Application) : super(application) { Log.d(TAG, "constructor")
        liveNFC = MutableStateFlow(null)
        liveToast = MutableSharedFlow()
        liveTag = MutableStateFlow(null)
    }
    //region Toast Methods
    private fun updateToast(message : String) { Coroutines.io(this@MainViewModel, {
        liveToast.emit(message)
    } ) }

    private suspend fun postToast(message : String) {
        liveToast.emit(message)
    }

    public fun observeToast() : SharedFlow<String?> {
        return liveToast.asSharedFlow()
    }
    //endregion
    public fun getNFCFlags() : Int {
        return NfcAdapter.FLAG_READER_NFC_A or
        NfcAdapter.FLAG_READER_NFC_B or
        NfcAdapter.FLAG_READER_NFC_F or
        NfcAdapter.FLAG_READER_NFC_V or
        NfcAdapter.FLAG_READER_NFC_BARCODE //or NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK
    }
    //region NFC Methods
    public fun onCheckNFC(isChecked : Boolean) { Coroutines.io(this@MainViewModel, { Log.d(TAG, "onCheckNFC(${isChecked})")
        if (isChecked) {
            postNFCStatus(NFCStatus.Tap)
            liveTag.emit("Please Tap Now")
        } else {
            postNFCStatus(NFCStatus.NoOperation)
            liveTag.emit(null)
        }
    } ) }

    public fun readTag(tag : Tag?) { Coroutines.default(this@MainViewModel, { Log.d(TAG, "readTag(${tag} ${tag?.getTechList()})")
        postNFCStatus(NFCStatus.Process)
        val stringBuilder : StringBuilder = StringBuilder()
        val id : ByteArray? = tag?.getId()
        stringBuilder.append("Tag ID (hex): ${getHex(id!!)} \n")
        stringBuilder.append("Tag ID (dec): ${getDec(id)} \n")
        stringBuilder.append("Tag ID (reversed): ${getReversed(id)} \n")
        stringBuilder.append("Technologies: ")
        tag.getTechList().forEach { tech ->
            stringBuilder.append(tech.substring(prefix.length))
            stringBuilder.append(", ")
        }
        stringBuilder.delete(stringBuilder.length - 2, stringBuilder.length)
        tag.getTechList().forEach { tech ->
            if (tech.equals(MifareClassic::class.java.getName())) {
                stringBuilder.append('\n')
                val mifareTag : MifareClassic = MifareClassic.get(tag)
                val type : String
                if (mifareTag.getType() == MifareClassic.TYPE_CLASSIC) type = "Classic"
                else if (mifareTag.getType() == MifareClassic.TYPE_PLUS) type = "Plus"
                else if (mifareTag.getType() == MifareClassic.TYPE_PRO) type = "Pro"
                else type = "Unknown"
                stringBuilder.append("Mifare Classic type: $type \n")
                stringBuilder.append("Mifare size: ${mifareTag.getSize()} bytes \n")
                stringBuilder.append("Mifare sectors: ${mifareTag.getSectorCount()} \n")
                stringBuilder.append("Mifare blocks: ${mifareTag.getBlockCount()}")
            }
            if (tech.equals(MifareUltralight::class.java.getName())) {
                stringBuilder.append('\n');
                val mifareUlTag : MifareUltralight = MifareUltralight.get(tag);
                val type : String
                if (mifareUlTag.getType() == MifareUltralight.TYPE_ULTRALIGHT) type = "Ultralight"
                else if (mifareUlTag.getType() == MifareUltralight.TYPE_ULTRALIGHT_C) type = "Ultralight C"
                else type = "Unkown"
                stringBuilder.append("Mifare Ultralight type: ");
                stringBuilder.append(type)
            }
        }
        Log.d(TAG, "Datum: $stringBuilder")
        Log.d(ContentValues.TAG, "dumpTagData Return \n $stringBuilder")
        postNFCStatus(NFCStatus.Read)
        liveTag.emit("${getDateTimeNow()} \n ${stringBuilder}")
    } ) }

    public fun updateNFCStatus(status : NFCStatus) { Coroutines.io(this@MainViewModel, {
        postNFCStatus(status)
    } ) }

    private suspend fun postNFCStatus(status : NFCStatus) { Log.d(TAG, "postNFCStatus(${status})")
        if (NFCManager.isSupportedAndEnabled(getApplication())) liveNFC.emit(status)
        else if (NFCManager.isNotEnabled(getApplication())) { liveNFC.emit(NFCStatus.NotEnabled)
            postToast("Please Enable your NFC!")
        } else if (NFCManager.isNotSupported(getApplication())) { liveNFC.emit(NFCStatus.NotSupported)
            postToast("NFC Not Supported!")
        }
    }

    public fun observeNFCStatus() : StateFlow<NFCStatus?> {
        return liveNFC.asStateFlow()
    }
    //endregion
    //region Tags Information Methods
    private fun getDateTimeNow() : String { Log.d(TAG, "getDateTimeNow()")
        val TIME_FORMAT : DateFormat = SimpleDateFormat.getDateTimeInstance()
        val now : Date = Date()
        Log.d(ContentValues.TAG,"getDateTimeNow() Return ${TIME_FORMAT.format(now)}")
        return TIME_FORMAT.format(now)
    }

    private fun getHex(bytes : ByteArray) : String {
        val sb = StringBuilder()
        for (i in bytes.indices.reversed()) {
            val b : Int = bytes[i].and(0xff.toByte()).toInt()
            if (b < 0x10) sb.append('0')
            sb.append(Integer.toHexString(b))
            if (i > 0)
                sb.append(" ")
        }
        return sb.toString()
    }

    private fun getDec(bytes : ByteArray) : Long {
        Log.d(TAG, "getDec()")
        var result : Long = 0
        var factor : Long = 1
        for (i in bytes.indices) {
            val value : Long = bytes[i].and(0xffL.toByte()).toLong()
            result += value * factor
            factor *= 256L
        }
        return result
    }

    private fun getReversed(bytes : ByteArray) : Long {
        Log.d(TAG, "getReversed()")
        var result : Long = 0
        var factor : Long = 1
        for (i in bytes.indices.reversed()) {
            val value = bytes[i].and(0xffL.toByte()).toLong()
            result += value * factor
            factor *= 256L
        }
        return result
    }

    public fun observeLiveData() : StateFlow<String?> {
        return liveTag.asStateFlow()
    }
    //endregion
}