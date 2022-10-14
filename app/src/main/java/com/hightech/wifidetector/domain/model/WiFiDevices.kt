package com.hightech.wifidetector.domain.model

data class WiFiDevices(
    val wifis: MutableList<WiFiDevice>
) {

    fun getSSIDs(): List<String> {
        return wifis.map { it -> it.ssid }
    }

    fun getMacAddresses(): List<String> {
        return wifis.map { it -> it.macAddress }
    }

}