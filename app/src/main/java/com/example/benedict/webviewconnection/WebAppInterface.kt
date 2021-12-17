package com.example.benedict.webviewconnection

import android.content.Context
import android.os.Build
import android.webkit.JavascriptInterface
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

public class WebAppInterface {

    companion object {
        public val TAG = WebAppInterface::class.java.getSimpleName()
    }

    private val context : Context
    private val liveText : MutableLiveData<String>

    constructor(context : Context) {
        this.context = context
        this.liveText = MutableLiveData<String>()
    }

    @JavascriptInterface // Show a toast from the web page
    public fun showToast(toast : String) {
        Toast.makeText(context, toast, Toast.LENGTH_SHORT).show()
    }

    @JavascriptInterface
    public fun getAndroidVersion() : Int {
        return Build.VERSION.SDK_INT
    }

    @JavascriptInterface
    public fun showToastAndroidVersion(versionName : String) {
        Toast.makeText(context, versionName, Toast.LENGTH_SHORT).show()
    }

    @JavascriptInterface
    public fun postLiveText(value : String) {
        liveText.postValue(value)
    }

    public fun observeLiveText() : LiveData<String> {
        return liveText
    }

    public fun getJavascriptAlert(alert : String) : String {
        return "javascript:alert(showWebAlert('$alert'))"
    }

    public fun getJavascriptAndroidVersion(paragraph : String) : String {
        return "javascript:alert(showAndroidVersion('$paragraph'))"
    }

    public fun getJavascriptParagraph(paragraph : String) : String {
        return "javascript:setParagraph(\"$paragraph\")"
    }
}