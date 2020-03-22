package com.example.benedict.ConnectionApp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.lang.ref.WeakReference;

public class ManifestPermission {

    private static final int RECORD_AUDIO = 0;
    private static ManifestPermission manifestPermission;
    private static WeakReference<MainActivity> activityWeakReference;

    public static ManifestPermission newInstance(MainActivity activity) {
        Log.d("ManifestPermission","newInstance()");
        manifestPermission = new ManifestPermission(activity);
        return manifestPermission;
    }

    private ManifestPermission(MainActivity activity) {
        Log.d("ManifestPermission","Constructor");
        activityWeakReference = new WeakReference<MainActivity>(activity);
    }

    public static void check() {
        Log.d("ManifestPermission","check()");
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

        if(isExternalStorageMounted()) {
            String[] permissions = new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            requestPermissions(permissions);
        }
    }

    private static boolean hasMicrophone() {
        Log.d("ManifestPermission","hasMicrophone()");
        MainActivity activity = activityWeakReference.get();
        if (activity == null || activity.isFinishing()) {
            return false;
        }

        PackageManager packageManager = activity.getPackageManager();
        return packageManager.hasSystemFeature(
                PackageManager.FEATURE_MICROPHONE);
    }

    private static boolean isMicrophonePermissionGranted() {
        Log.d("ManifestPermission","isMicrophonePermissionGranted()");
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

    private static void requestPermissions(String[] permissions) {
        Log.d("ManifestPermission","requestPermissions()");
        MainActivity activity = activityWeakReference.get();
        if (activity == null || activity.isFinishing()) {
            return;
        }
        ActivityCompat.requestPermissions(
                activity,
                permissions,
                RECORD_AUDIO
        );
    }

    private static boolean isExternalStorageMounted() {
        Log.d("ManifestPermission","isExternalStorageMounted()");
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }
}
