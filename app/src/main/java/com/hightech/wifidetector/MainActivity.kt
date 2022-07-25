package com.hightech.wifidetector

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.hightech.wifidetector.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var wifiDetector: WiFiDetector
    private lateinit var registeredDevices: Map<String, List<String>>

    companion object {
        const val CHANNEL_ID = "MYCHANNEL"
    }

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
                    showNotification()
                }

                override fun onDetectedFailure() {
                    binding.tvDetectStatus.text = getString(R.string.detect_status_negative)
                    binding.tvDetectStatus.setTextColor(Color.RED)
                }

            })
            .build()

        wifiDetector.detect("Elevenia-BOD", listOf("c0:7b:bc:84:08:2d"))
    }

    override fun onPause() {
        super.onPause()
        wifiDetector.detect("Elevenia-BOD", listOf("c0:7b:bc:84:08:2d"))
    }

    fun showNotification() {
        val intent = Intent(applicationContext, MainActivity::class.java)

        val notificationChannel =
            NotificationChannel(CHANNEL_ID, "name", NotificationManager.IMPORTANCE_LOW)

        val pendingIntent = PendingIntent.getActivity(applicationContext, 1, intent, PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext)
            .setContentText("Anda telah memasuki wilayah kantor EDTS")
            .setContentTitle("Anda Login")
            .setContentIntent(pendingIntent)
            .addAction(android.R.drawable.sym_action_chat, "Title", pendingIntent)
            .setChannelId(CHANNEL_ID)
            .setSmallIcon(android.R.drawable.sym_action_chat)

        notificationBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))

        val notification = notificationBuilder.build()

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
        notificationManager.notify(1, notification)
    }

}