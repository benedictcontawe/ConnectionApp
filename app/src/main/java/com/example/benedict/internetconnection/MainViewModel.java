package com.example.benedict.internetconnection;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.webkit.WebView;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();
    private WebAppInterface webAppInterface;

    public MainViewModel(Application application) {
        super(application);
        webAppInterface = new WebAppInterface(application);
    }

    public WebAppInterface getWebAppInterface() {
        return webAppInterface;
    }

    public String getHTMLPath() {
        return "file:///android_asset/index.html";
    }

    public Boolean isGreaterThanEqualKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public Boolean isNotGreaterThanEqualKitKat() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT;
    }

    public Boolean isNotZeroFlags(ApplicationInfo applicationInfo) {
        return 0 != (applicationInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE);
    }

    public Boolean isWebContentsDebuggingEnabled(ApplicationInfo applicationInfo) {
        return isGreaterThanEqualKitKat() && isNotZeroFlags(applicationInfo);
    }

    public void setWebContentsDebuggingEnabled(ApplicationInfo applicationInfo) {
        if (isWebContentsDebuggingEnabled(applicationInfo)) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
    }

    public String getJavascriptAlert(String alert) {
        return webAppInterface.getJavascriptAlert(alert);
    }

    public String getJavascriptAndroidVersion(String alert) {
        return webAppInterface.getJavascriptAndroidVersion(alert);
    }

    public String getJavascriptParagraph(String alert) {
        return webAppInterface.getJavascriptParagraph(alert);
    }

    public Boolean isUndefined(String message) {
        return message.equals("undefined");
    }

    public LiveData<String> observeLiveText() {
        if (webAppInterface.observeLiveText() != null) return webAppInterface.observeLiveText();
        else return new MutableLiveData<String>("null");
    }

    @Override
    protected void onCleared() {
        webAppInterface = null;
        super.onCleared();
    }
}
//https://medium.com/mobile-app-development-publication/making-android-interacting-with-web-app-921be14f99d8
//https://www.codexpedia.com/android/javascript-interface-for-android-and-javascript-communication/