package com.example.benedict.internetconnection;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class WebAppInterface {

    public static String TAG = WebAppInterface.class.getSimpleName();
    private Context context;
    private MutableLiveData<String> liveText;

    WebAppInterface(Context context) {
        this.context = context;
        this.liveText = new MutableLiveData<>();
    }

    @JavascriptInterface // Show a toast from the web page
    public void showToast(String toast) {
        Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public int getAndroidVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }

    @JavascriptInterface
    public void showToastAndroidVersion(String versionName) {
        Toast.makeText(context, versionName, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void postLiveText(String value) {
        liveText.postValue(value);
    }

    public LiveData<String> observeLiveText() {
        return liveText;
    }

    public String getJavascriptAlert(String alert) {
        return "javascript:alert(showWebAlert('" + alert + "'))";
    }

    public String getJavascriptAndroidVersion(String paragraph) {
        return "javascript:alert(showAndroidVersion('" + paragraph + "'))";
    }

    public String getJavascriptParagraph(String paragraph) {
        return "javascript:setParagraph(\"" + paragraph + "\")";
    }
}//javascript:setParagraph