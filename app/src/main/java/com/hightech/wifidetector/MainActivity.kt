package com.hightech.wifidetector

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hightech.wifidetector.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var wifiDetector: WiFiDetector
    private lateinit var registeredDevices: Map<String, List<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                }

                override fun onDetectedFailure() {
                    binding.tvDetectStatus.text = getString(R.string.detect_status_negative)
                    binding.tvDetectStatus.setTextColor(Color.RED)
                }

            })
            .build()

        wifiDetector.detect("Elevenia-BOD", listOf("c0:7b:bc:84:08:2d"))
    }

}