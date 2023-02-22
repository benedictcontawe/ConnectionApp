package com.example.permissions

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.permissions.databinding.MainBinder

class MainActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        private val TAG : String = MainActivity::class.java.getSimpleName()
    }

    private var binder : MainBinder? = null

    override fun onCreate(savedInstanceState : Bundle?) {
        binder = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
        binder?.setLifecycleOwner(this@MainActivity)
        super.onCreate(savedInstanceState)
        //region Check Permission On Click Listener
        binder?.allCheckPermissions?.setOnClickListener(this@MainActivity)
        binder?.telephonyCheckPermissions?.setOnClickListener(this@MainActivity)
        binder?.microphoneCheckPermissions?.setOnClickListener(this@MainActivity)
        binder?.cameraCheckPermissions?.setOnClickListener(this@MainActivity)
        binder?.videoCallCheckPermissions?.setOnClickListener(this@MainActivity)
        binder?.galleryCheckPermissions?.setOnClickListener(this@MainActivity)
        binder?.contactCheckPermissions?.setOnClickListener(this@MainActivity)
        //endregion
        //region Request Permission On Click Listener
        binder?.allRequestPermissions?.setOnClickListener(this@MainActivity)
        binder?.telephonyRequestPermissions?.setOnClickListener(this@MainActivity)
        binder?.microphoneRequestPermissions?.setOnClickListener(this@MainActivity)
        binder?.cameraRequestPermissions?.setOnClickListener(this@MainActivity)
        binder?.videoCallRequestPermissions?.setOnClickListener(this@MainActivity)
        binder?.galleryRequestPermissions?.setOnClickListener(this@MainActivity)
        binder?.contactRequestPermissions?.setOnClickListener(this@MainActivity)
        //endregion
        binder?.showRationalDialog?.setOnClickListener(this@MainActivity)
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onClick(view : View) {
        when(view) {
            binder?.allCheckPermissions -> {
                ManifestPermission.checkSelfPermission(this@MainActivity,
                    ManifestPermission.allPermissions,
                    isGranted = { setTextData("All Permissions Granted!") },
                    isDenied = { setTextData("All Permissions Denied!") }
                )
            }
            binder?.telephonyCheckPermissions -> {
                ManifestPermission.checkSelfPermission(this@MainActivity,
                    ManifestPermission.telephonyPermissions,
                    isGranted = { setTextData("Telephony Permissions Granted!") },
                    isDenied = { setTextData("Telephony Permissions Denied!") }
                )
            }
            binder?.microphoneCheckPermissions -> {
                ManifestPermission.checkSelfPermission(this@MainActivity,
                    ManifestPermission.microphonePermission,
                    isGranted = { setTextData("Microphone Permissions Granted!") },
                    isDenied = { setTextData("Microphone Permissions Denied!") }
                )
            }
            binder?.cameraCheckPermissions -> {
                ManifestPermission.checkSelfPermission(this@MainActivity,
                    ManifestPermission.cameraPermission,
                    isGranted = { setTextData("Camera Permissions Granted!") },
                    isDenied = { setTextData("Camera Permissions Denied!") }
                )
            }
            binder?.videoCallCheckPermissions -> {
                ManifestPermission.checkSelfPermission(this@MainActivity,
                    ManifestPermission.videoCallPermission,
                    isGranted = { setTextData("Video Call Permissions Granted!") },
                    isDenied = { setTextData("Video Call Permissions Denied!") }
                )
            }
            binder?.galleryCheckPermissions -> {
                ManifestPermission.checkSelfPermission(this@MainActivity,
                    ManifestPermission.galleryPermissions,
                    isGranted = { setTextData("Gallery Permissions Granted!") },
                    isDenied = { setTextData("Gallery Permissions Denied!") }
                )
            }
            binder?.contactCheckPermissions -> {
                ManifestPermission.checkSelfPermission(this@MainActivity,
                    ManifestPermission.contactPermission,
                    isGranted = { setTextData("Contact Permissions Granted!") },
                    isDenied = { setTextData("Contact Permissions Denied!") }
                )
            }
            binder?.allRequestPermissions -> {
                ManifestPermission.requestPermissions(this@MainActivity,
                    ManifestPermission.allPermissions,
                    ManifestPermission.ALL_PERMISSION_CODE
                )
            }
            binder?.telephonyRequestPermissions -> {
                ManifestPermission.requestPermissions(this@MainActivity,
                    ManifestPermission.telephonyPermissions,
                    ManifestPermission.TELEPHONY_PERMISSION_CODE
                )
            }
            binder?.microphoneRequestPermissions -> {
                ManifestPermission.requestPermissions(this@MainActivity,
                    ManifestPermission.microphonePermission,
                    ManifestPermission.MICROPHONE_PERMISSION_CODE
                )
            }
            binder?.cameraRequestPermissions -> {
                ManifestPermission.requestPermissions(this@MainActivity,
                    ManifestPermission.cameraPermission,
                    ManifestPermission.CAMERA_PERMISSION_CODE
                )
            }
            binder?.videoCallRequestPermissions -> {
                ManifestPermission.requestPermissions(this@MainActivity,
                    ManifestPermission.videoCallPermission,
                    ManifestPermission.VIDEO_CALL_PERMISSION_CODE
                )
            }
            binder?.galleryRequestPermissions -> {
                ManifestPermission.requestPermissions(this@MainActivity,
                    ManifestPermission.galleryPermissions,
                    ManifestPermission.GALLERY_PERMISSION_CODE
                )
            }
            binder?.contactRequestPermissions -> {
                ManifestPermission.requestPermissions(this@MainActivity,
                    ManifestPermission.contactPermission,
                    ManifestPermission.CONTACT_PERMISSION_CODE
                )
            }
            binder?.showRationalDialog -> {
                ManifestPermission.showRationaleDialog(this@MainActivity,"Go to App Permission Settings?", activityResultLauncher)
            }
        }
    }

    private fun setTextData(text : String) {
        binder?.textData?.setText(text)
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
        } )
    }

    private val activityResultLauncher : ActivityResultLauncher<Intent> = registerForActivityResult( ActivityResultContracts.StartActivityForResult() ) { activityResult ->
        Log.d(TAG,"activityResultLauncher(${activityResult.getResultCode()},${activityResult.getData()})")
        if (activityResult.getResultCode() == Activity.RESULT_OK) {
            Log.d(TAG,"activityResult.getResultCode() == Activity.RESULT_OK")
        } else if (activityResult.getResultCode() == Activity.RESULT_CANCELED) {
            Log.d(TAG,"activityResult.getResultCode() == Activity.RESULT_CANCELED")
        }
    }

    val requestPermissionLauncher : ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
    }

    @Deprecated("Deprecated in Java")
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