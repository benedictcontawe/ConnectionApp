package com.example.benedict.simstate;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Arrays;

public class ManifestPermission {

    static final private String TAG = ManifestPermission.class.getSimpleName();

    static final public int SETTINGS_PERMISSION_CODE = 1000;
    static final public int ALL_PERMISSION_CODE = 1001;
    static final public int TELEPHONY_PERMISSION_CODE = 1002;
    static final public int MICROPHONE_PERMISSION_CODE = 1003;
    static final public int CAMERA_PERMISSION_CODE = 1004;
    static final public int VIDEO_CALL_PERMISSION_CODE = 1005;
    static final public int GALLERY_PERMISSION_CODE = 1006;
    static final public int CONTACT_PERMISSION_CODE = 1007;

    static final public String[] getAllPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new String[]{
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_SMS,
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_CONTACTS,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_NUMBERS
            };
        } else {
            return new String[] {
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_SMS,
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_CONTACTS,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
        }
    }

    static final public String[] getTelephonyPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new String[]{
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_SMS,
                    Manifest.permission.READ_PHONE_NUMBERS
            };
        } else {
            return new String[] {
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_SMS
            };
        }
    };

    static final public String[] getMicrophonePermission() {
        return new String[] {
                Manifest.permission.RECORD_AUDIO
        };
    }

    static final public String[] getCameraPermission() {
        return new String[] {
                Manifest.permission.CAMERA
        };
    }

    static final public String[] getVideoCallPermission() {
        return new String[] {
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA
        };
    }

    static final public String[] getGalleryPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
        } else {
            return new String[] {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
        }
    }

    static final public String[] getContactPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new String[]{
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_CONTACTS,
                    Manifest.permission.READ_PHONE_NUMBERS
            };
        } else {
            return new String[] {
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_CONTACTS
            };
        }
    }

    static final public void checkSelfPermission(Context context, String permission) { //TODO(Not Completed)
        Log.d(TAG,"checkSelfPermission($context,${permissions.contentToString()},isGranted(),isDenied())");
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG,"allGranted()");
        }
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
            Log.d(TAG,"denied()");
        }
    }

    static final public void checkSelfPermission(Context context, String[] permissions) { //TODO(Not Completed)
        Log.d(TAG,"checkSelfPermission($context,${permissions.contentToString()},isGranted(),isDenied())");
        for(String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG,"allGranted()");
            }
            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
                Log.d(TAG,"denied()");
            }
        }
    }

    static final public void requestPermissions(Activity activity, String permission, int requestCode) {
        Log.d(TAG,"requestPermissions("+activity+","+permission+","+requestCode);
        ActivityCompat.requestPermissions(activity, new String[] { permission }, requestCode);
    }

    static final public void requestPermissions(Activity activity, String[] permissions, int requestCode) {
        Log.d(TAG,"requestPermissions("+activity+","+ Arrays.toString(permissions) +","+requestCode);
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    static final public void checkNeverAskAgain(Activity activity, String permission, int requestCode) { //TODO(Not Completed)
        Log.d(TAG,"hasPermissions("+activity+","+ permission +",isNeverAskAgain(),isNotNeverAskAgain())");
        if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission) && ActivityCompat.checkSelfPermission(activity,permission) == PackageManager.PERMISSION_DENIED) {
            Log.d(TAG,"isNeverAskAgain()");
        } else {
            Log.d(TAG,"isNotNeverAskAgain()");
        }
    }

    static final public void checkNeverAskAgain(Activity activity, String[] permissions, int requestCode) { //TODO(Not Completed)
        Log.d(TAG,"hasPermissions("+activity+","+ Arrays.toString(permissions) +",isNeverAskAgain(),isNotNeverAskAgain())");
        for(String permission : permissions) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission) && ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_DENIED) {
                Log.d(TAG,"isNeverAskAgain()");
            } else {
                Log.d(TAG,"isNotNeverAskAgain()");
            }
        }
    }

    static final public void checkPermissionsResult(Activity activity, String permission, int requestCode) { //TODO(Not Completed)
        if(ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            Log.d(TAG, "permission Denied " + permission);
        } else {
            if(ActivityCompat.checkSelfPermission(activity,permission) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "permission Allowed " + permission);
            } else{
                Log.d(TAG, "permission set to never ask again " + permission);
            }
        }
    }

    static final public void checkPermissionsResult(Activity activity, String[] permissions, int requestCode) { //TODO(Not Completed)

    }



    static final public void showRationalDialog(final Activity activity, String message) {
        Log.d(TAG,"showRationalDialog($activity,$message");
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Manifest Permissions");
        builder.setMessage(message);
        builder.setPositiveButton("SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showAppPermissionSettings(activity);
            }
        });
        builder.setNegativeButton("NOT NOW", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    static final private void showAppPermissionSettings(Activity activity) {
        Log.d("PermissionsResult", "showAppPermissionSettings()");
        activity.startActivityForResult(
                new Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", activity.getPackageName(), null)
                )
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                        .addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS),
                SETTINGS_PERMISSION_CODE
        );
    }
}
