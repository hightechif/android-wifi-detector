package com.hightech.wifidetector.ui

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hightech.wifidetector.R
import com.hightech.wifidetector.ui.wifi.WiFiDetector
import com.hightech.wifidetector.databinding.ActivityMainBinding
import com.hightech.wifidetector.domain.model.WiFiDevice
import com.hightech.wifidetector.domain.model.WiFiDevices
import com.hightech.wifidetector.util.CommonUtil

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var wifiDetector: WiFiDetector
    private lateinit var registeredWiFis: WiFiDevices
    private lateinit var registeredDevices: Map<String, List<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registeredWiFis = WiFiDevices(mutableListOf(WiFiDevice("Elevenia-BOD", "c0:7b:bc:84:08:2d", true)))
        registeredDevices = mapOf(
            "G15AD" to listOf("f4:f2:6d:5a:07:6c"),
            "Elevenian" to listOf("c0:7b:bc:84:08:2f", "18:9c:5d:6d:d6:7f"),
            "Elevenia-BOD" to listOf("c0:7b:bc:84:08:2d"),
            "Elevenia-Guest" to listOf("c0:7b:bc:84:08:2e")
        )

        wifiDetector = WiFiDetector.Builder()
            .bind(this)
            .setOnScanResultListener(object : WiFiDetector.WiFiDetectorDelegate {
                override fun onScanSuccess(data: String) {
                    binding.tvScan.text = data
                }

                override fun onScanFailure(message: String, data: String?) {
                    binding.tvScan.text = message
                    binding.tvDetectStatus.text = getString(R.string.scan_status_negative)
                    binding.tvDetectStatus.setTextColor(Color.RED)
                }

                override fun onDetectedSuccess() {
                    binding.tvDetectStatus.text = getString(R.string.detect_status_positive)
                    binding.tvDetectStatus.setTextColor(Color.GREEN)
                    CommonUtil.showNotification(this@MainActivity)
                }

                override fun onDetectedFailure() {
                    binding.tvDetectStatus.text = getString(R.string.detect_status_negative)
                    binding.tvDetectStatus.setTextColor(Color.RED)
                }

            })
            .build()

        wifiDetector.detect("Elevenia-BOD", registeredWiFis)

    }

    override fun onPause() {
        super.onPause()
        wifiDetector.detect("Elevenia-BOD", registeredWiFis)
    }

}