package com.hightech.wifidetector

import android.app.Application
import android.content.Intent

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startService(Intent(this, WiFiDetectorService::class.java))
    }
}