package com.hightech.wifidetector.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.hightech.wifidetector.ui.MainActivity
import com.hightech.wifidetector.ui.wifi.WiFiDetectorService

class CommonUtil {

    companion object {

        const val CHANNEL_ID = "MYCHANNEL"
        const val NOTIF_ID = 1

        fun setNotification(context: Context): Notification {
            val intent = Intent(context, MainActivity::class.java)

            val pendingIntent =
                PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_IMMUTABLE)

            val notificationBuilder: NotificationCompat.Builder =
                NotificationCompat.Builder(context)
                    .setContentTitle("Selamat datang di Wisma 46 lantai 42 SG-EDTS")
                    .setContentText("Selamat datang dan semangat bekerja :)")
                    .setContentIntent(pendingIntent)
                    .addAction(android.R.drawable.sym_action_chat, "Develop by Fadhil", pendingIntent)
                    .setChannelId(CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.sym_action_chat)

            notificationBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))

            return notificationBuilder.build()
        }

        fun setNotificationChannel(context: Context): NotificationManager {
            val notificationChannel = NotificationChannel(CHANNEL_ID, "name", NotificationManager.IMPORTANCE_LOW)
            val notificationManager = context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
            return notificationManager
        }

        fun showNotification(context: Context) {
            val notification = setNotification(context)
            val notificationManager = setNotificationChannel(context)
            notificationManager.notify(NOTIF_ID, notification)
        }
    }

}