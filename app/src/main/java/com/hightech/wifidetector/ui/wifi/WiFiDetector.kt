package com.hightech.wifidetector.ui.wifi

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.hightech.wifidetector.domain.model.WiFiDevice
import com.hightech.wifidetector.domain.model.WiFiDevices

class WiFiDetector(
    private val activity: Activity,
    private val wifiManager: WifiManager,
    private val onScanResultListener: WiFiDetectorDelegate
) {

    private constructor(builder: Builder) : this(
        builder.activity!!,
        builder.wifiManager!!,
        builder.onScanResultListener!!
    )

    private var isScanOnly: Boolean = true

    companion object {
        const val REQUEST_CHANGE_WIFI_STATE = 1201
    }

    interface WiFiDetectorDelegate {
        fun onScanSuccess(data: String)
        fun onScanFailure(message: String, data: String? = null)
        fun onDetectedSuccess()
        fun onDetectedFailure()
    }

    data class Builder(
        var activity: Activity? = null,
        var wifiManager: WifiManager? = null,
        var onScanResultListener: WiFiDetectorDelegate? = null,
        var isScanOnly: Boolean? = null
    ) {

        fun bind(activity: Activity) = apply {
            this.activity = activity
            wifiManager = activity.baseContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        }

        fun setOnScanResultListener(delegate: WiFiDetectorDelegate) = apply {
            this.onScanResultListener = delegate
        }

        fun build() = WiFiDetector(this)

    }

    private fun requestWiFiPermission(callback: () -> Unit) {
        val aclPermission = ContextCompat.checkSelfPermission(
            activity.baseContext,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        val aflPermission = ContextCompat.checkSelfPermission(
            activity.baseContext,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val accessWiFiPermission = ContextCompat.checkSelfPermission(
            activity.baseContext,
            Manifest.permission.ACCESS_WIFI_STATE
        )
        val changeWiFiPermission = ContextCompat.checkSelfPermission(
            activity.baseContext,
            Manifest.permission.CHANGE_WIFI_STATE
        )

        val isPermissionsNotGranted = aclPermission != PackageManager.PERMISSION_GRANTED ||
                aflPermission != PackageManager.PERMISSION_GRANTED ||
                accessWiFiPermission != PackageManager.PERMISSION_GRANTED ||
                changeWiFiPermission != PackageManager.PERMISSION_GRANTED

        if (isPermissionsNotGranted) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.CHANGE_WIFI_STATE
                ),
                REQUEST_CHANGE_WIFI_STATE
            )
        } else {
            callback.invoke()
        }
    }

    private fun scanWiFi(ssid: String = "", registeredWiFi: WiFiDevices = WiFiDevices(mutableListOf())) {
        val wifiScanReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
                if (success) {
                    scanSuccess(ssid, registeredWiFi)
                } else {
                    scanFailure()
                }
            }
        }

        val intentFilter = IntentFilter()
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        activity.baseContext?.registerReceiver(wifiScanReceiver, intentFilter)

        val success = wifiManager.startScan()
        if (!success) {
            // scan failure handling
            scanFailure()
        }

    }

    private fun scanSuccess(ssid: String, registeredWiFis: WiFiDevices) {
        val results = wifiManager.scanResults
        var wifiIDs = "WiFi Devices: \n"
        var detected = false
        for ((id, ap) in results.withIndex()) {
            val wifiID = "SSID=" + ap.SSID + " MAC=" + ap.BSSID
            wifiIDs += "$id. $wifiID \n"

            if (!isScanOnly) {
                if (ssid.isNotEmpty() && registeredWiFis.wifis.isNotEmpty()) {
                    val registeredMacAddresses = registeredWiFis.getMacAddresses()
                    if (ap.SSID == ssid && ap.BSSID in registeredMacAddresses) {
                        detected = true
                    }
                }
            }
        }

        if (!isScanOnly) {
            if (detected) {
                onScanResultListener.onDetectedSuccess()
            } else {
                onScanResultListener.onDetectedFailure()
            }
        }

        onScanResultListener.onScanSuccess(wifiIDs)
    }

    private fun scanFailure() {
        // handle failure: new scan did NOT succeed
        // consider using old scan results: these are the OLD results!
        val results = wifiManager.scanResults
        onScanResultListener.onScanFailure("No WiFi Device", results.toString())
    }

    fun scan() {
        requestWiFiPermission {
            isScanOnly = true
            scanWiFi()
        }
    }

    fun detect(ssid: String, registeredWiFi: WiFiDevices) {
        requestWiFiPermission {
            isScanOnly = false
            scanWiFi(ssid, registeredWiFi)
        }
    }

}