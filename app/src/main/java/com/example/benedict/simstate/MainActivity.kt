package com.example.benedict.simstate

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        private val TAG : String = MainActivity::class.java.getSimpleName()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //region Check Permission On Click Listener
        allCheckPermissions.setOnClickListener(this)
        telephonyCheckPermissions.setOnClickListener(this)
        microphoneCheckPermissions.setOnClickListener(this)
        cameraCheckPermissions.setOnClickListener(this)
        videoCallCheckPermissions.setOnClickListener(this)
        galleryCheckPermissions.setOnClickListener(this)
        //endregion
        //region Request Permission On Click Listener
        allRequestPermissions.setOnClickListener(this)
        telephonyRequestPermissions.setOnClickListener(this)
        microphoneRequestPermissions.setOnClickListener(this)
        cameraRequestPermissions.setOnClickListener(this)
        videoCallRequestPermissions.setOnClickListener(this)
        galleryRequestPermissions.setOnClickListener(this)
        //endregion
        showRationalDialog.setOnClickListener(this)
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onClick(view : View) {
        when(view) {
            allCheckPermissions -> {
                ManifestPermission.checkSelfPermission(this,
                    ManifestPermission.allPermissions,
                    isGranted = { setTextData("All Permissions Granted!") },
                    isDenied = { setTextData("All Permissions Denied!") }
                )
            }
            telephonyCheckPermissions -> {
                ManifestPermission.checkSelfPermission(this,
                    ManifestPermission.telephonyPermissions,
                    isGranted = { setTextData("Telephony Permissions Granted!") },
                    isDenied = { setTextData("Telephony Permissions Denied!") }
                )
            }
            microphoneCheckPermissions -> {
                ManifestPermission.checkSelfPermission(this,
                    ManifestPermission.microphonePermission,
                    isGranted = { setTextData("Microphone Permissions Granted!") },
                    isDenied = { setTextData("Microphone Permissions Denied!") }
                )
            }
            cameraCheckPermissions -> {
                ManifestPermission.checkSelfPermission(this,
                    ManifestPermission.cameraPermission,
                    isGranted = { setTextData("Camera Permissions Granted!") },
                    isDenied = { setTextData("Camera Permissions Denied!") }
                )
            }
            videoCallCheckPermissions -> {
                ManifestPermission.checkSelfPermission(this,
                    ManifestPermission.videoCallPermission,
                    isGranted = { setTextData("Video Call Permissions Granted!") },
                    isDenied = { setTextData("Video Call Permissions Denied!") }
                )
            }
            galleryCheckPermissions -> {
                ManifestPermission.checkSelfPermission(this,
                    ManifestPermission.galleryPermissions,
                    isGranted = { setTextData("Gallery Permissions Granted!") },
                    isDenied = { setTextData("Gallery Permissions Denied!") }
                )
            }
            allRequestPermissions -> {
                ManifestPermission.requestPermissions(this,
                    ManifestPermission.allPermissions,
                    ManifestPermission.ALL_PERMISSION_CODE
                )
            }
            telephonyRequestPermissions -> {
                ManifestPermission.requestPermissions(this,
                    ManifestPermission.telephonyPermissions,
                    ManifestPermission.TELEPHONY_PERMISSION_CODE
                )
            }
            microphoneRequestPermissions -> {
                ManifestPermission.requestPermissions(this,
                    ManifestPermission.microphonePermission,
                    ManifestPermission.MICROPHONE_PERMISSION_CODE
                )
            }
            cameraRequestPermissions -> {
                ManifestPermission.requestPermissions(this,
                    ManifestPermission.cameraPermission,
                    ManifestPermission.CAMERA_PERMISSION_CODE
                )
            }
            videoCallRequestPermissions -> {
                ManifestPermission.requestPermissions(this,
                    ManifestPermission.videoCallPermission,
                    ManifestPermission.VIDEO_CALL_PERMISSION_CODE
                )
            }
            galleryRequestPermissions -> {
                ManifestPermission.requestPermissions(this,
                    ManifestPermission.galleryPermissions,
                    ManifestPermission.GALLARY_PERMISSION_CODE
                )
            }
            showRationalDialog -> {
                ManifestPermission.showRationalDialog(this,"Go to App Permission Settings?")
            }
        }
    }

    private fun setTextData(text : String) {
        txtData.setText(text)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d("PermissionsResult", "requestCode $requestCode")
        Log.d("PermissionsResult","permissions " + permissions.contentToString())
        Log.d("PermissionsResult","grantResults " + grantResults.contentToString())

        permissions.map {permission ->
            ManifestPermission.checkNeverAskAgain(this,permission,
                isNeverAskAgain = {
                    Log.d("PermissionsResult", "checkNeverAskAgain isNeverAskAgain single $permission")
                },
                isNotNeverAskAgain = {
                    Log.d("PermissionsResult", "checkNeverAskAgain isNotNeverAskAgain single $permission")
                }
            )

            ManifestPermission.checkPermissionsResult(this,permission,
                isNeverAskAgain = {
                    Log.d("PermissionsResult", "checkPermissionsResult isNeverAskAgain single $permission")
                }, isDenied = {
                    Log.d("PermissionsResult","checkPermissionsResult isDenied single $permission")
                }, isGranted = {
                    Log.d("PermissionsResult","checkPermissionsResult isGranted single $permission")
                }
            )
        }

        ManifestPermission.checkNeverAskAgain(this,permissions,
            isNeverAskAgain = {
                Log.d("PermissionsResult", "checkNeverAskAgain isNeverAskAgain")
            },
            isNotNeverAskAgain = {
                Log.d("PermissionsResult", "checkNeverAskAgain isNotNeverAskAgain")
            }
        )

        ManifestPermission.checkPermissionsResult(this, permissions, grantResults,
            isNeverAskAgain =  {
                Log.d("PermissionsResult", "checkPermissionsResult isNeverAskAgain")
            }, isDenied = {
                Log.d("PermissionsResult","checkPermissionsResult isDenied")
            }, isGranted = {
                Log.d("PermissionsResult","checkPermissionsResult isGranted")
            }
        )

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}