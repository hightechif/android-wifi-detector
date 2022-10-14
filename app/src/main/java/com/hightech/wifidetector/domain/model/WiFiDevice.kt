package com.hightech.wifidetector.domain.model

data class WiFiDevice(
    val ssid: String,
    val macAddress: String,
    val isRegistered: Boolean
)
