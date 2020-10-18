package com.example.benedict.internetconnection

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG : String = MainActivity::class.java.getSimpleName()
        private const val INTERNET_STATE : Int = 0
        fun newIntent(context : Context) : Intent {
            val intent : Intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            return intent
        }
    }

    private val viewModel : MainViewModel by lazy(LazyThreadSafetyMode.NONE, initializer = {
        ViewModelProvider(this@MainActivity).get(MainViewModel::class.java)
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        observeData()
    }

    public fun observeData() {
        viewModel.observeLiveInternet().observe(this@MainActivity, object : Observer<Boolean> {
            override fun onChanged(value : Boolean) {
                requestPermissions(viewModel.checkPermission())
                if (value) {
                    txtInternet.setText("Network Connection is available")
                } else {
                    txtInternet.setText("Network Connection is not available")
                }
            }
        })

        viewModel.observeLivePing().observe(this@MainActivity, object : Observer<Boolean> {
            override fun onChanged(value : Boolean) {
                requestPermissions(viewModel.checkPermission())
                if (value) {
                    txtPing.setText("Google Successfuly Ping")
                    txtMetered.setVisibility(View.VISIBLE)
                } else {
                    txtPing.setText("Google Unreachable ping")
                    txtMetered.setVisibility(View.INVISIBLE)
                }
            }

        })

        viewModel.observeLiveMeter().observe(this@MainActivity, object : Observer<Boolean> {
            override fun onChanged(value : Boolean) {
                if (value) {
                    txtMetered.setText("Network Connection is Metered")
                } else {
                    txtMetered.setText("Network Connection is not Metered")
                }
            }
        })
    }

    override fun onPause() {
        super.onPause()
        try {
            viewModel.unregisterConnectivity()
        } catch (ex: Exception) {
            showAppPermissionSettings()
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            viewModel.registerConnectivity()
            viewModel.checkConnections()
        } catch (ex: Exception) {

        }
    }

    private fun requestPermissions(permissionGranted: Boolean) {
        if (!permissionGranted) {
            ActivityCompat.requestPermissions(
                this@MainActivity, arrayOf(
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.ACCESS_NETWORK_STATE
                ),
                MainActivity.INTERNET_STATE
            )
        }
    }

    private fun showAppPermissionSettings() {
        Toast.makeText(this@MainActivity, "Internet Permissions Disabled", Toast.LENGTH_LONG).show()
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", packageName, null)
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}