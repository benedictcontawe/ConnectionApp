package com.example.benedict.simstate;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.PhoneStateListener;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class MainViewModel extends AndroidViewModel {

    private static String TAG = MainViewModel.class.getSimpleName();
    private TelephonyManager telephonyManager;
    private SubscriptionManager subscriptionManager ;
    private SubscriptionInfo subscriptionInfo ;
    private SimChangedListener simChangedListener;
    private MutableLiveData<Boolean> liveSimState = new MutableLiveData<>();

    public MainViewModel(Application application) {
        super(application);
        Log.d(TAG,"Constructor");
        telephonyManager = (TelephonyManager) getApplication().getSystemService(Context.TELEPHONY_SERVICE);
        simChangedListener = new SimChangedListener(this);
        //setSubscription();
    }
    //region Subscription methods for LOLLIPOP_MR1
    private void setSubscription() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            subscriptionManager = (SubscriptionManager) getApplication().getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
            subscriptionInfo = subscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(0);
        } else {
            Log.d(TAG,"setSubscription() else");
        }
    }

    private boolean checkSubscriptionInfo() {
        if (subscriptionInfo != null && !subscriptionInfo.getCountryIso().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
    //endregion
    //region Register Unregister Sim State Detection Callback
    public void registerSimState() {
        telephonyManager.listen(simChangedListener,PhoneStateListener.LISTEN_SERVICE_STATE|PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }

    public void unregisterSimState() {
        telephonyManager.listen(simChangedListener,PhoneStateListener.LISTEN_NONE);
    }
    //endregion
    public void checkSimState() {
        liveSimState.postValue(isSimMounted());
    }
    private boolean isSimMounted() {
        Log.d(TAG,"checkSimState()");
        boolean isAvailable;
        int simState;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            simState = telephonyManager.getSimState(0);
        } else {
            simState = telephonyManager.getSimState();
        }
        switch (simState) {
            case TelephonyManager.SIM_STATE_READY:
                isAvailable = true;
                Log.d(TAG,"TelephonyManager.SIM_STATE_READY");
                break;
            case TelephonyManager.SIM_STATE_ABSENT: //SimState = “No Sim Found!”;
                isAvailable = false;
                Log.d(TAG,"TelephonyManager.SIM_STATE_ABSENT");
                break;
            case TelephonyManager.SIM_STATE_NETWORK_LOCKED: //SimState = “Network Locked!”;
                isAvailable = false;
                Log.d(TAG,"TelephonyManager.SIM_STATE_NETWORK_LOCKED");
                break;
            case TelephonyManager.SIM_STATE_PIN_REQUIRED: //SimState = “PIN Required to access SIM!”;
                isAvailable = false;
                Log.d(TAG,"TelephonyManager.SIM_STATE_PIN_REQUIRED");
                break;
            case TelephonyManager.SIM_STATE_PUK_REQUIRED: //SimState = “PUK Required to access SIM!”; // Personal Unblocking Code
                isAvailable = false;
                Log.d(TAG,"TelephonyManager.SIM_STATE_PUK_REQUIRED");
                break;
            case TelephonyManager.SIM_STATE_UNKNOWN: //SimState = “Unknown SIM State!”;
                isAvailable = false;
                Log.d(TAG,"TelephonyManager.SIM_STATE_UNKNOWN");
                break;
            default:
                isAvailable = false;
                Log.d(TAG,"TelephonyManager.default");
                break;
        }
        return isAvailable;
    }

    private int getPhoneType() {
        switch (telephonyManager.getPhoneType()) {
            case TelephonyManager.PHONE_TYPE_SIP:
                Log.d(TAG,"getPhoneType() : TelephonyManager.PHONE_TYPE_SIP");
                break;
            case TelephonyManager.PHONE_TYPE_CDMA:
                Log.d(TAG,"getPhoneType() : TelephonyManager.PHONE_TYPE_CDMA");
                break;
            case TelephonyManager.PHONE_TYPE_GSM:
                Log.d(TAG,"getPhoneType() : TelephonyManager.PHONE_TYPE_GSM");
                break;
            case TelephonyManager.PHONE_TYPE_NONE:
                Log.d(TAG,"getPhoneType() : TelephonyManager.PHONE_TYPE_NONE");
                break;
            default:
                Log.d(TAG,"getPhoneType() : TelephonyManager.PHONE_TYPE_NONE");
                break;
        }
        return telephonyManager.getPhoneType();
    }

    private boolean isNetworkRoaming() {
        return telephonyManager.isNetworkRoaming();
    }

    private String getSoftwareVersion() {
        if (telephonyManager.getDeviceSoftwareVersion() != null) {
            return telephonyManager.getDeviceSoftwareVersion();
        } else  {
            return "Nil";
        }
    }

    private String getVoiceMailNumber() {
        if (telephonyManager.getVoiceMailNumber() != null) {
            return telephonyManager.getVoiceMailNumber();
        } else  {
            return "Nil";
        }
    }

    private String getSimNumber() {
        return telephonyManager.getLine1Number();
        //return subscriptionInfo.getNumber();
    }
    //region LiveData Observers
    public LiveData<Boolean> getLiveSimState() {
        return liveSimState;
    }
    //endregion
    //region Manifest Permissions
    public boolean checkPermission() {
        boolean value = false;
        Log.d(TAG,"checkPermission()");
        if (isReadPhoneStatePermissionGranted()) {
            Log.d(TAG,"isReadPhoneStatePermissionGranted()");
            value = true;
        }
        return value;
    }

    private boolean isReadPhoneStatePermissionGranted() {
        return ActivityCompat
                .checkSelfPermission(
                        getApplication(),
                        Manifest.permission.READ_PHONE_STATE
                ) == PackageManager.PERMISSION_GRANTED;
    }
    //endregion
    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
