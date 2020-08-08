package com.example.benedict.permission

import android.content.Intent
import android.os.AsyncTask
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

    val contactsProvider : ContactsProvider = ContactsProvider()

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //region Check Permission On Click Listener
        allCheckPermissions.setOnClickListener(this)
        telephonyCheckPermissions.setOnClickListener(this)
        microphoneCheckPermissions.setOnClickListener(this)
        cameraCheckPermissions.setOnClickListener(this)
        videoCallCheckPermissions.setOnClickListener(this)
        galleryCheckPermissions.setOnClickListener(this)
        contactCheckPermissions.setOnClickListener(this)
        //endregion
        //region Request Permission On Click Listener
        allRequestPermissions.setOnClickListener(this)
        telephonyRequestPermissions.setOnClickListener(this)
        microphoneRequestPermissions.setOnClickListener(this)
        cameraRequestPermissions.setOnClickListener(this)
        videoCallRequestPermissions.setOnClickListener(this)
        galleryRequestPermissions.setOnClickListener(this)
        contactRequestPermissions.setOnClickListener(this)
        //endregion
        showRationalDialog.setOnClickListener(this)
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        AsyncTask.execute {
            contactsProvider.getContactsList(this@MainActivity)
        }
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
            contactCheckPermissions -> {
                ManifestPermission.checkSelfPermission(this,
                    ManifestPermission.contactPermission,
                    isGranted = { setTextData("Contact Permissions Granted!") },
                    isDenied = { setTextData("Contact Permissions Denied!") }
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
                    ManifestPermission.GALLERY_PERMISSION_CODE
                )
            }
            contactRequestPermissions -> {
                ManifestPermission.requestPermissions(this,
                    ManifestPermission.contactPermission,
                    ManifestPermission.CONTACT_PERMISSION_CODE
                )
            }
            showRationalDialog -> {
                ManifestPermission.showRationalDialog(this)
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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG,"onActivityResult($requestCode,$resultCode,$data)")
        if (requestCode == ManifestPermission.SETTINGS_PERMISSION_CODE)
            Toast.makeText(this,"PERMISSION_SETTINGS_CODE",Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        Log.d(TAG,"onDestroy()")
        super.onDestroy()
    }
}