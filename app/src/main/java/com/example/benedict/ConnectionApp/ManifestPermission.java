package com.example.benedict.ConnectionApp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.lang.ref.WeakReference;

public class ManifestPermission {

    private static String TAG = ManifestPermission.class.getSimpleName();
    public static final int RECORD_AUDIO = 0;
    private boolean requestGranted,neverAskAgain;
    private static ManifestPermission manifestPermission;
    private static WeakReference<MainActivity> activityWeakReference;

    public static ManifestPermission newInstance(MainActivity activity) {
        Log.d(TAG,"newInstance()");
        manifestPermission = new ManifestPermission(activity);
        return manifestPermission;
    }

    private ManifestPermission(MainActivity activity) {
        Log.d(TAG,"Constructor");
        activityWeakReference = new WeakReference<MainActivity>(activity);
    }

    public void setRequestGranted(boolean requestGranted) {
        Log.d(TAG,"setRequestGranted(" + requestGranted + ")");
        this.requestGranted = requestGranted;
    }

    public void setNeverAskAgain(boolean neverAskAgain) {
        Log.d(TAG,"setNeverAskAgain(" + neverAskAgain + ")");
        this.neverAskAgain = neverAskAgain;
    }

    public boolean getRequestGranted() {
        Log.d(TAG,"getRequestGranted()");
        return requestGranted;
    }

    public boolean isNeverAskAgain() {
        Log.d(TAG,"getRequestGranted()");
        return neverAskAgain;
    }

    public void check() {
        Log.d(TAG,"check()");
        MainActivity activity = activityWeakReference.get();
        if (activity == null || activity.isFinishing()) {
            return;
        }

        if (!hasMicrophone()) {
            activity.disableRecordView();
            return;
        }

        if (!isMicrophonePermissionGranted()) {
            String[] permissions = new String[] {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            requestPermissions(permissions);
        }

        if(!isWriteExternalStorageGranted()) {
            String[] permissions = new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            requestPermissions(permissions);
        }
        /*
        if(!isExternalStorageMounted()) {
            String[] permissions = new String[] {Manifest.permission_group.STORAGE};
            requestPermissions(permissions);
        }
        */
    }

    private boolean hasMicrophone() {
        Log.d(TAG,"hasMicrophone()");
        MainActivity activity = activityWeakReference.get();
        if (activity == null || activity.isFinishing()) {
            return false;
        }

        PackageManager packageManager = activity.getPackageManager();
        return packageManager.hasSystemFeature(
                PackageManager.FEATURE_MICROPHONE);
    }

    private boolean isMicrophonePermissionGranted() {
        Log.d(TAG,"isMicrophonePermissionGranted()");
        MainActivity activity = activityWeakReference.get();
        if (activity == null || activity.isFinishing()) {
            return false;
        }

        return ActivityCompat
                .checkSelfPermission(
                        activity,
                        Manifest.permission.RECORD_AUDIO
                ) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isWriteExternalStorageGranted() {
        Log.d(TAG,"isExternalStorageMounted()");
        MainActivity activity = activityWeakReference.get();
        if (activity == null || activity.isFinishing()) {
            return false;
        }

        return ActivityCompat
                .checkSelfPermission(
                        activity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isExternalStorageMounted() {
        MainActivity activity = activityWeakReference.get();
        if (activity == null || activity.isFinishing()) {
            return false;
        }
        //return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
        return ActivityCompat
                .checkSelfPermission(
                        activity,
                        Manifest.permission_group.STORAGE
                ) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions(String[] permissions) {
        Log.d(TAG,"requestPermissions()");
        MainActivity activity = activityWeakReference.get();
        if (activity == null || activity.isFinishing()) {
            return;
        }
        if (!requestGranted) {
            ActivityCompat.requestPermissions(
                    activity,
                    permissions,
                    RECORD_AUDIO
            );
        }
    }
}
