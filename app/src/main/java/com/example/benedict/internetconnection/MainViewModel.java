package com.example.benedict.internetconnection;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class MainViewModel extends AndroidViewModel {

    private MutableLiveData<Boolean> liveSimState = new MutableLiveData<>();

    public MainViewModel(Application application) {
        super(application);
        Log.e("MainViewModel","isReadPhoneStatePermissionGranted() " + isReadPhoneStatePermissionGranted());
        Log.e("MainViewModel","isSimAvailable() " + isSimAvailable());
    }

    public boolean isSimAvailable() {
        boolean isAvailable = false;
        TelephonyManager telMgr = (TelephonyManager) getApplication().getSystemService(Context.TELEPHONY_SERVICE);
        int simState = telMgr.getSimState();
        switch (simState) {
            case TelephonyManager.SIM_STATE_ABSENT: //SimState = “No Sim Found!”;
                break;
            case TelephonyManager.SIM_STATE_NETWORK_LOCKED: //SimState = “Network Locked!”;
                break;
            case TelephonyManager.SIM_STATE_PIN_REQUIRED: //SimState = “PIN Required to access SIM!”;
                break;
            case TelephonyManager.SIM_STATE_PUK_REQUIRED: //SimState = “PUK Required to access SIM!”; // Personal Unblocking Code
                break;
            case TelephonyManager.SIM_STATE_READY:
                isAvailable = true;
                break;
            case TelephonyManager.SIM_STATE_UNKNOWN: //SimState = “Unknown SIM State!”;
                break;
        }
        return isAvailable;
    }

    private boolean isReadPhoneStatePermissionGranted() {
        return ActivityCompat
                .checkSelfPermission(
                        getApplication(),
                        Manifest.permission.READ_PHONE_STATE
                ) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions(String[] permissions) {
        Log.d("ManifestPermission","requestPermissions()");

        ActivityCompat.requestPermissions(
                null,
                permissions,
                0
        );
    }

    public LiveData<Boolean> getLiveSimState() {
        return liveSimState;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
    /*
    https://readyandroid.wordpress.com/how-to-check-sim-card-is-available-or-not-in-android/
    https://sites.google.com/site/androidhowto/how-to-1/check-if-sim-card-exists-in-the-phone
     */
}
