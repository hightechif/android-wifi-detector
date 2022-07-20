package com.hightech.wifidetector

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.hightech.wifidetector.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var wifiManager: WifiManager

    companion object {
        const val REQUEST_CHANGE_WIFI_STATE = 1201
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        wifiManager = baseContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        requestPermission {
            scanWiFi()
        }
    }

    private fun requestPermission(onSuccess: () -> Unit) {
        val aflPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val aclPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        val accessWiFiPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE)
        val changeWiFiPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE)

        val isPermissionGranted = aflPermission != PackageManager.PERMISSION_GRANTED ||
                aclPermission != PackageManager.PERMISSION_GRANTED ||
                accessWiFiPermission != PackageManager.PERMISSION_GRANTED ||
                changeWiFiPermission != PackageManager.PERMISSION_GRANTED

        if (isPermissionGranted) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.CHANGE_WIFI_STATE
                ),
                REQUEST_CHANGE_WIFI_STATE
            )
        } else {
            onSuccess.invoke()
        }
    }

    private fun scanWiFi() {
        val wifiScanReceiver = object : BroadcastReceiver() {

            override fun onReceive(context: Context, intent: Intent) {
                val success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
                if (success) {
                    scanSuccess()
                } else {
                    scanFailure()
                }
            }
        }

        val intentFilter = IntentFilter()
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        baseContext.registerReceiver(wifiScanReceiver, intentFilter)

        val success = wifiManager.startScan()
        if (!success) {
            // scan failure handling
            scanFailure()
        }

    }

    private fun scanSuccess() {
        val results = wifiManager.scanResults
        var wifiIDs = ""
        for ((id, ap) in results.withIndex()) {
            val wifiID = "SSID=" + ap.SSID + " MAC=" + ap.BSSID
            Log.d("DEBUG FADHIL", wifiID)
            wifiIDs += "$id. $wifiID \n"
        }
        binding.tvMain.text = wifiIDs
    }

    private fun scanFailure() {
        // handle failure: new scan did NOT succeed
        // consider using old scan results: these are the OLD results!
        val results = wifiManager.scanResults
        Log.d("DEBUG FADHIL", "OLD: $results")
    }

}