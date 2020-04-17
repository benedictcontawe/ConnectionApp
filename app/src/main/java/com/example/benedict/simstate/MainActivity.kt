package com.example.benedict.simstate

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val PERMISSION_STATE = 0
    }


    private var callPermissionSettings: Boolean? = null
    private var requestGranted: Boolean? = null
    private var neverAskAgain: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        requestPermissions()
    }

    private fun checkPermission(manifestPermissions : Array<String>) : Boolean {
        return manifestPermissions.filter { manifestPermission ->
            !isPermissionsGranted(manifestPermission)
        }.isNotEmpty()
        /*
        return if (requestGranted == null || neverAskAgain == null) {
            true
        } else if (!requestGranted!! && !neverAskAgain!!) {
            txtData.text = "All permission denied"
            true
        } else if (neverAskAgain as Boolean && !requestGranted!!) {
            txtData.text = "All permission denied and set to Don't Ask Again"
            false
        } else {
            txtData.text = "All permission allowed"
            false
        }
        */
    }

    private fun checkAppPermissionSettings() : Boolean {
        return if (!callPermissionSettings!! && neverAskAgain!! && !requestGranted!!) {
            callPermissionSettings = true
            true
        } else {
            false
        }
    }

    private fun requestPermissions() {
        Log.d("PermissionsResult", "requestPermissions()")
        val manifestPermissions: Array<String> =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                arrayOf(
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_CONTACTS,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            } else {
                arrayOf(
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_CONTACTS,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            }

        if (checkPermission(manifestPermissions)) {
            ActivityCompat.requestPermissions(
                this,
                manifestPermissions,
                PERMISSION_STATE
            )
        }
    }

    private fun isPermissionsGranted(permission : String) : Boolean {
        return ActivityCompat
            .checkSelfPermission(
                application,
                permission
            ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //region Logs for onRequestPermissionsResult parameters
        Log.d("PermissionsResult", "requestCode $requestCode")
        Log.d("PermissionsResult","permissions " + permissions.contentToString())
        Log.d("PermissionsResult","grantResults " + grantResults.contentToString())
        //endregion
        //region Code for requestCode and grantResults
        if (PERMISSION_STATE == requestCode) {
            requestGranted = grantResults.filter { grantResult ->
                grantResult == PackageManager.PERMISSION_DENIED
            }.isEmpty()
        }
        //endregion
        //region Code for permissions
        neverAskAgain = true
        for (permission in permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                Log.d("PermissionsResult", "permission Denied $permission")
                neverAskAgain = false
            } else {
                if (isPermissionsGranted(permission)) {
                    Log.d("PermissionsResult", "permission Allowed $permission")
                    neverAskAgain = false
                } else {
                    Log.d(
                        "PermissionsResult",
                        "permission set to never ask again $permission"
                    )
                    //neverAskAgain = true;
                }
            }
        }
        //endregion
        //region Code for showAppPermissionSettings
        callPermissionSettings = false
        if (checkAppPermissionSettings()) showAppPermissionSettings()
        //endregion
        //region Logs for Booleans of requestGranted, neverAskAgain, callPermissionSettings
        Log.d("PermissionsResult", "requestGranted $requestGranted")
        Log.d("PermissionsResult", "neverAskAgain $neverAskAgain")
        Log.d("PermissionsResult", "callPermissionSettings $callPermissionSettings")
        //endregion
    }

    private fun showAppPermissionSettings() {
        Log.d("PermissionsResult", "showAppPermissionSettings()")
        Toast.makeText(this, "Hardware Permissions Disabled", Toast.LENGTH_LONG).show()
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", packageName, null)
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}