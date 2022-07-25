package com.hightech.wifidetector

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.RingtoneManager
import android.os.IBinder
import androidx.core.app.NotificationCompat

class WiFiDetectorService : Service() {

    private val NOTIF_ID = 1

    companion object {
        const val CHANNEL_ID = "MYCHANNEL"
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        // do your jobs here
        startForeground()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForeground() {
        val intent = Intent(applicationContext, MainActivity::class.java)

        val notificationChannel =
            NotificationChannel(CHANNEL_ID, "name", NotificationManager.IMPORTANCE_LOW)

        val pendingIntent =
            PendingIntent.getActivity(applicationContext, 1, intent, PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext)
                .setContentText("Anda telah memasuki wilayah kantor EDTS")
                .setContentTitle("Anda Login")
                .setContentIntent(pendingIntent)
                .addAction(android.R.drawable.sym_action_chat, "Develop by Fadhil", pendingIntent)
                .setChannelId(CHANNEL_ID)
                .setSmallIcon(android.R.drawable.sym_action_chat)

        notificationBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))

        val notification = notificationBuilder.build()

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
        notificationManager.notify(1, notification)

        startForeground(NOTIF_ID, notification)
    }

}