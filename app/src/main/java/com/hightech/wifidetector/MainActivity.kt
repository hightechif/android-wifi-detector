package com.hightech.wifidetector

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hightech.wifidetector.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var wifiDetector: WiFiDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        wifiDetector = WiFiDetector.Builder()
            .bind(this)
            .setOnScanResultListener(object : WiFiDetector.WiFiDetectorDelegate {
                override fun onScanSuccess(data: String) {
                    binding.tvMain.text = data
                }

                override fun onScanFailure(message: String, data: String?) {
                    binding.tvMain.text = message
                }

                override fun onDetectedSuccess() {
                    // TODO: Login Action or Attend Action
                }

                override fun onDetectedFailure() {
                    // Do nothing
                }

            })
            .build()
    }

    override fun onResume() {
        super.onResume()
        wifiDetector.scan()
        wifiDetector.detect("G15AD",listOf("02:15:b2:00:01:00"))
    }
}