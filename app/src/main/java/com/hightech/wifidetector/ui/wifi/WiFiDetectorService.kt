package com.hightech.wifidetector.ui.wifi

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.hightech.wifidetector.util.CommonUtil

class WiFiDetectorService : Service() {

    var hasShowedUp = false

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        // do your jobs here
        if (!hasShowedUp) {
//            startForeground()
            hasShowedUp = true
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForeground() {
        val notification = CommonUtil.setNotification(this)
        startForeground(CommonUtil.NOTIF_ID, notification)
    }

}