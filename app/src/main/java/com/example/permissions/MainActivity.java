package com.example.permissions;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    static final private String TAG = MainActivity.class.getSimpleName();
    private static final int PERMISSION_STATE = 0;
    private Boolean callPermissionSettings,requestGranted, neverAskAgain;
    private TextView txtData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtData = (TextView) findViewById(R.id.txtData);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkPermission()) requestPermissions();
    }

    private boolean checkPermission() {
        if (requestGranted == null || neverAskAgain == null) {
            return true;
        }
        else if (!requestGranted && !neverAskAgain) {
            txtData.setText("All permission denied");
            return true;
        }
        else if (neverAskAgain && !requestGranted) {
            txtData.setText("All permission denied and set to Don't Ask Again");
            return false;
        }
        else {
            txtData.setText("All permission allowed");
            return false;
        }
    }

    private boolean checkAppPermissionSettings() {
        if (!callPermissionSettings && neverAskAgain && !requestGranted) {
            callPermissionSettings = true;
            return true;
        } else {
            return false;
        }
    }

    private void requestPermissions() {
        Log.d("PermissionsResult", "requestPermissions()");
        String[] manifestPermissions;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            manifestPermissions = new String[]{
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_CONTACTS,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
        } else {
            manifestPermissions = new String[]{
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_CONTACTS,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
        }

        ActivityCompat.requestPermissions(
                this,
                manifestPermissions,
                PERMISSION_STATE
        );
    }

    private boolean isPermissionsGranted(String permission) {
        return ActivityCompat
                .checkSelfPermission(
                        getApplication(),
                        permission
                ) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //region Logs for onRequestPermissionsResult parameters
        Log.d("PermissionsResult", "requestCode " + requestCode);
        Log.d("PermissionsResult", "permissions " + Arrays.toString(permissions));
        Log.d("PermissionsResult", "grantResults " + Arrays.toString(grantResults));
        //endregion
        //region Code for requestCode and grantResults
        requestGranted = true;
        if (this.PERMISSION_STATE == requestCode) {
            for (int grantResult : grantResults) {
                switch (grantResult) {
                    case PackageManager.PERMISSION_GRANTED:
                        Log.d("PermissionsResult", "grantResult Allowed " + grantResult);
                        //requestGranted = true;
                        break;
                    case PackageManager.PERMISSION_DENIED:
                        Log.d("PermissionsResult", "grantResult Denied " + grantResult);
                        requestGranted = false;
                        break;
                    default:
                        requestGranted = false;
                        break;
                }
            }
        }
        //endregion
        //region Code for permissions
        neverAskAgain = true;
        for(String permission : permissions) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                Log.d("PermissionsResult", "permission Denied " + permission);
                neverAskAgain = false;
            } else {
                if(isPermissionsGranted(permission)) {
                    Log.d("PermissionsResult", "permission Allowed " + permission);
                    neverAskAgain = false;
                } else{
                    Log.d("PermissionsResult", "permission set to never ask again " + permission);
                    //neverAskAgain = true;
                }
            }
        }
        //endregion
        //region Code for showAppPermissionSettings
        callPermissionSettings = false;
        if (checkAppPermissionSettings()) showAppPermissionSettings();
        //endregion
        //region Logs for Booleans of requestGranted, neverAskAgain, callPermissionSettings
        Log.d("PermissionsResult", "requestGranted " + requestGranted);
        Log.d("PermissionsResult", "neverAskAgain " + neverAskAgain);
        Log.d("PermissionsResult", "callPermissionSettings " + callPermissionSettings);
        //endregion
    }

    private void showAppPermissionSettings() {
        Log.d("PermissionsResult", "showAppPermissionSettings()");
        Toast.makeText(this, "Hardware Permissions Disabled", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", getPackageName(), null));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        startActivity(intent);
    }

    private void  onActivityResult() { Log.d(TAG,"onActivityResult()");
        ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult (
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult activityResult) {
                    if (activityResult.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = activityResult.getData();   // Handle the Intent
                    }
                    if (activityResult.getResultCode() == ManifestPermission.SETTINGS_PERMISSION_CODE)
                        Toast.makeText(getBaseContext(), "PERMISSION_SETTINGS_CODE",Toast.LENGTH_SHORT).show();
                }
            }
        );
        //mStartForResult.launch();

        ActivityResultLauncher<String> mGetContent = registerForActivityResult (
            new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    // Handle the returned Uri
                }
            }
        );
        //mGetContent.launch();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG,"onActivityResult(" + requestCode + ", " + resultCode + ", " + data );
        if (requestCode == ManifestPermission.SETTINGS_PERMISSION_CODE)
            Toast.makeText(this,"PERMISSION_SETTINGS_CODE",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}