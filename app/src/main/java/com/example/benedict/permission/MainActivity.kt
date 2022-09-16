package com.example.benedict.permission

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        private val TAG : String = MainActivity::class.java.getSimpleName()
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //region Check Permission On Click Listener
        allCheckPermissions.setOnClickListener(this@MainActivity)
        telephonyCheckPermissions.setOnClickListener(this@MainActivity)
        microphoneCheckPermissions.setOnClickListener(this@MainActivity)
        cameraCheckPermissions.setOnClickListener(this@MainActivity)
        videoCallCheckPermissions.setOnClickListener(this@MainActivity)
        galleryCheckPermissions.setOnClickListener(this@MainActivity)
        contactCheckPermissions.setOnClickListener(this@MainActivity)
        //endregion
        //region Request Permission On Click Listener
        allRequestPermissions.setOnClickListener(this@MainActivity)
        telephonyRequestPermissions.setOnClickListener(this@MainActivity)
        microphoneRequestPermissions.setOnClickListener(this@MainActivity)
        cameraRequestPermissions.setOnClickListener(this@MainActivity)
        videoCallRequestPermissions.setOnClickListener(this@MainActivity)
        galleryRequestPermissions.setOnClickListener(this@MainActivity)
        contactRequestPermissions.setOnClickListener(this@MainActivity)
        //endregion
        showRationalDialog.setOnClickListener(this@MainActivity)
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
                ManifestPermission.checkSelfPermission(this@MainActivity,
                    ManifestPermission.allPermissions,
                    isGranted = { setTextData("All Permissions Granted!") },
                    isDenied = { setTextData("All Permissions Denied!") }
                )
            }
            telephonyCheckPermissions -> {
                ManifestPermission.checkSelfPermission(this@MainActivity,
                    ManifestPermission.telephonyPermissions,
                    isGranted = { setTextData("Telephony Permissions Granted!") },
                    isDenied = { setTextData("Telephony Permissions Denied!") }
                )
            }
            microphoneCheckPermissions -> {
                ManifestPermission.checkSelfPermission(this@MainActivity,
                    ManifestPermission.microphonePermission,
                    isGranted = { setTextData("Microphone Permissions Granted!") },
                    isDenied = { setTextData("Microphone Permissions Denied!") }
                )
            }
            cameraCheckPermissions -> {
                ManifestPermission.checkSelfPermission(this@MainActivity,
                    ManifestPermission.cameraPermission,
                    isGranted = { setTextData("Camera Permissions Granted!") },
                    isDenied = { setTextData("Camera Permissions Denied!") }
                )
            }
            videoCallCheckPermissions -> {
                ManifestPermission.checkSelfPermission(this@MainActivity,
                    ManifestPermission.videoCallPermission,
                    isGranted = { setTextData("Video Call Permissions Granted!") },
                    isDenied = { setTextData("Video Call Permissions Denied!") }
                )
            }
            galleryCheckPermissions -> {
                ManifestPermission.checkSelfPermission(this@MainActivity,
                    ManifestPermission.galleryPermissions,
                    isGranted = { setTextData("Gallery Permissions Granted!") },
                    isDenied = { setTextData("Gallery Permissions Denied!") }
                )
            }
            contactCheckPermissions -> {
                ManifestPermission.checkSelfPermission(this@MainActivity,
                    ManifestPermission.contactPermission,
                    isGranted = { setTextData("Contact Permissions Granted!") },
                    isDenied = { setTextData("Contact Permissions Denied!") }
                )
            }
            allRequestPermissions -> {
                ManifestPermission.requestPermissions(this@MainActivity,
                    ManifestPermission.allPermissions,
                    ManifestPermission.ALL_PERMISSION_CODE
                )
            }
            telephonyRequestPermissions -> {
                ManifestPermission.requestPermissions(this@MainActivity,
                    ManifestPermission.telephonyPermissions,
                    ManifestPermission.TELEPHONY_PERMISSION_CODE
                )
            }
            microphoneRequestPermissions -> {
                ManifestPermission.requestPermissions(this@MainActivity,
                    ManifestPermission.microphonePermission,
                    ManifestPermission.MICROPHONE_PERMISSION_CODE
                )
            }
            cameraRequestPermissions -> {
                ManifestPermission.requestPermissions(this@MainActivity,
                    ManifestPermission.cameraPermission,
                    ManifestPermission.CAMERA_PERMISSION_CODE
                )
            }
            videoCallRequestPermissions -> {
                ManifestPermission.requestPermissions(this@MainActivity,
                    ManifestPermission.videoCallPermission,
                    ManifestPermission.VIDEO_CALL_PERMISSION_CODE
                )
            }
            galleryRequestPermissions -> {
                ManifestPermission.requestPermissions(this@MainActivity,
                    ManifestPermission.galleryPermissions,
                    ManifestPermission.GALLERY_PERMISSION_CODE
                )
            }
            contactRequestPermissions -> {
                ManifestPermission.requestPermissions(this@MainActivity,
                    ManifestPermission.contactPermission,
                    ManifestPermission.CONTACT_PERMISSION_CODE
                )
            }
            showRationalDialog -> {
                ManifestPermission.showRationalDialog(this@MainActivity,"Go to App Permission Settings?")
            }
        }
    }

    private fun setTextData(text : String) {
        txtData.setText(text)
    }

    override fun onRequestPermissionsResult(requestCode : Int, permissions : Array<String>, grantResults : IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d("PermissionsResult", "requestCode $requestCode")
        Log.d("PermissionsResult","permissions " + permissions.contentToString())
        Log.d("PermissionsResult","grantResults " + grantResults.contentToString())

        permissions.map { permission ->
            ManifestPermission.checkNeverAskAgain(this@MainActivity,permission,
                isNeverAskAgain = {
                    Log.d("PermissionsResult", "checkNeverAskAgain isNeverAskAgain single $permission")
                },
                isNotNeverAskAgain = {
                    Log.d("PermissionsResult", "checkNeverAskAgain isNotNeverAskAgain single $permission")
                }
            )

            ManifestPermission.checkPermissionsResult(this@MainActivity,permission,
                isNeverAskAgain = {
                    Log.d("PermissionsResult", "checkPermissionsResult isNeverAskAgain single $permission")
                }, isDenied = {
                    Log.d("PermissionsResult","checkPermissionsResult isDenied single $permission")
                }, isGranted = {
                    Log.d("PermissionsResult","checkPermissionsResult isGranted single $permission")
                }
            )
        }

        ManifestPermission.checkNeverAskAgain(this@MainActivity, permissions,
            isNeverAskAgain = {
                Log.d("PermissionsResult", "checkNeverAskAgain isNeverAskAgain")
            },
            isNotNeverAskAgain = {
                Log.d("PermissionsResult", "checkNeverAskAgain isNotNeverAskAgain")
            }
        )

        ManifestPermission.checkPermissionsResult(this@MainActivity, permissions, grantResults,
            isNeverAskAgain =  {
                Log.d("PermissionsResult", "checkPermissionsResult isNeverAskAgain")
                when(requestCode) {
                    ManifestPermission.ALL_PERMISSION_CODE -> { setTextData("All Permissions is Never Ask Again!") }
                    ManifestPermission.TELEPHONY_PERMISSION_CODE -> { setTextData("Telephony Permissions is Never Ask Again!") }
                    ManifestPermission.MICROPHONE_PERMISSION_CODE -> { setTextData("Microphone Permissions is Never Ask Again!") }
                    ManifestPermission.CAMERA_PERMISSION_CODE -> { setTextData("Camera Permissions is Never Ask Again!") }
                    ManifestPermission.VIDEO_CALL_PERMISSION_CODE -> { setTextData("Video Call Permissions is Never Ask Again!") }
                    ManifestPermission.GALLERY_PERMISSION_CODE -> { setTextData("Gallery Permissions is Never Ask Again!") }
                    ManifestPermission.CONTACT_PERMISSION_CODE -> { setTextData("Contact Permissions is Never Ask Again!") }
                }
            }, isDenied = {
                Log.d("PermissionsResult","checkPermissionsResult isDenied")
                when(requestCode) {
                    ManifestPermission.ALL_PERMISSION_CODE -> { setTextData("All Permissions Denied!") }
                    ManifestPermission.TELEPHONY_PERMISSION_CODE -> { setTextData("Telephony Permissions Denied!") }
                    ManifestPermission.MICROPHONE_PERMISSION_CODE -> { setTextData("Microphone Permissions Denied!") }
                    ManifestPermission.CAMERA_PERMISSION_CODE -> { setTextData("Camera Permissions Denied!") }
                    ManifestPermission.VIDEO_CALL_PERMISSION_CODE -> { setTextData("Video Call Permissions Denied!") }
                    ManifestPermission.GALLERY_PERMISSION_CODE -> { setTextData("Gallery Permissions Denied!") }
                    ManifestPermission.CONTACT_PERMISSION_CODE -> { setTextData("Contact Permissions Denied!") }
                }
            }, isGranted = {
                Log.d("PermissionsResult","checkPermissionsResult isGranted")
                when(requestCode) {
                    ManifestPermission.ALL_PERMISSION_CODE -> { setTextData("All Permissions Granted!") }
                    ManifestPermission.TELEPHONY_PERMISSION_CODE -> { setTextData("Telephony Permissions Granted!") }
                    ManifestPermission.MICROPHONE_PERMISSION_CODE -> { setTextData("Microphone Permissions Granted!") }
                    ManifestPermission.CAMERA_PERMISSION_CODE -> { setTextData("Camera Permissions Granted!") }
                    ManifestPermission.VIDEO_CALL_PERMISSION_CODE -> { setTextData("Video Call Permissions Granted!") }
                    ManifestPermission.GALLERY_PERMISSION_CODE -> { setTextData("Gallery Permissions Granted!") }
                    ManifestPermission.CONTACT_PERMISSION_CODE -> { setTextData("Contact Permissions Granted!") }
                }
            }
        )
        ManifestPermission.checkPermissionsResult(this@MainActivity, ManifestPermission.ALL_PERMISSION_CODE, requestCode, permissions, grantResults,
            isNeverAskAgain = {
                Log.d("PermissionsResult","checkPermissionsResult with Selected Request Code, isNeverAskAgain")
            }, isDenied = {
                Log.d("PermissionsResult","checkPermissionsResult with Selected Request Code, isDenied")
            }, isGranted = {
                Log.d("PermissionsResult","checkPermissionsResult with Selected Request Code, isGranted")
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG,"onActivityResult($requestCode,$resultCode,$data)")
        if (requestCode == ManifestPermission.SETTINGS_PERMISSION_CODE)
            Toast.makeText(this@MainActivity,"PERMISSION_SETTINGS_CODE",Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        Log.d(TAG,"onDestroy()")
        super.onDestroy()
    }
}