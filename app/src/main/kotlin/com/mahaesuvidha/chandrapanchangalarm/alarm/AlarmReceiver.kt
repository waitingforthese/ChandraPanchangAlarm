package com.mahaesuvidha.chandrapanchangalarm.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.mahaesuvidha.chandrapanchangalarm.MainActivity

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val channelId = "moon_change"

        val manager =
            context.getSystemService(NotificationManager::class.java)

        // Android 8+ Notification Channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                channelId,
                "चंद्र राशी / नक्षत्र बदल",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "चंद्र राशी किंवा नक्षत्र बदल अलर्ट"
                enableVibration(true)
            }

            manager.createNotificationChannel(channel)
        }

        val title =
            intent.getStringExtra("title")
                ?: "🌙 चंद्र पंचांग अलार्म"

        val message =
            intent.getStringExtra("message")
                ?: "चंद्र स्थितीत बदल झाला."

        // Notification वर click केल्यावर App उघडेल
        val openAppIntent =
            Intent(context, MainActivity::class.java)

        val pendingIntent =
            PendingIntent.getActivity(
                context,
                2001,
                openAppIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or
                        PendingIntent.FLAG_IMMUTABLE
            )

        val notification =
            NotificationCompat.Builder(context, channelId)
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(message)
                )
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build()

        manager.notify(
            2001,
            notification
        )
    }
}
