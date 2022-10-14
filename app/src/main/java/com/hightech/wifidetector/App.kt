package com.hightech.wifidetector

import android.app.Application
import android.content.Intent
import com.hightech.wifidetector.ui.wifi.WiFiDetectorService

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startService(Intent(this, WiFiDetectorService::class.java))
    }
}