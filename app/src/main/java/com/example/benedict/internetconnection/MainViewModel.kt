package com.example.benedict.internetconnection

import android.app.Application
import android.content.pm.ApplicationInfo
import android.os.Build
import android.webkit.WebView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

public class MainViewModel : AndroidViewModel{

    private val webAppInterface: WebAppInterface

    companion object {
        public val TAG = MainViewModel::class.java.getSimpleName()
    }

    constructor(application : Application) : super(application) {
        webAppInterface = WebAppInterface(application)
    }

    public fun getWebAppInterface() : WebAppInterface {
        return webAppInterface
    }

    public fun getHTMLPath() : String {
        return "file:///android_asset/index.html"
    }

    public fun isGreaterThanEqualKitKat(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
    }

    public fun isNotGreaterThanEqualKitKat() : Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT
    }

    public fun isNotZeroFlags(applicationInfo : ApplicationInfo) : Boolean {
        return 0 != applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
    }

    public fun isWebContentsDebuggingEnabled(applicationInfo : ApplicationInfo) : Boolean {
        return isGreaterThanEqualKitKat() && isNotZeroFlags(applicationInfo)
    }

    public fun setWebContentsDebuggingEnabled(applicationInfo : ApplicationInfo) {
        if (isWebContentsDebuggingEnabled(applicationInfo)) {
            WebView.setWebContentsDebuggingEnabled(true)
        }
    }

    public fun getJavascriptAlert(alert : String) : String {
        return webAppInterface.getJavascriptAlert(alert)
    }

    public fun getJavascriptAndroidVersion(alert : String) : String {
        return webAppInterface.getJavascriptAndroidVersion(alert)
    }

    public fun getJavascriptParagraph(alert : String) : String {
        return webAppInterface.getJavascriptParagraph(alert)
    }

    public fun isUndefined(message : String) : Boolean {
        return message.equals("undefined")
    }

    public fun observeLiveText() : LiveData<String> {
        return if (webAppInterface.observeLiveText() != null) webAppInterface.observeLiveText()
        else MutableLiveData<String>("null")
    }

    override fun onCleared() {
        //webAppInterface = null
        super.onCleared()
    }
}