package com.mahaesuvidha.chandrapanchangalarm.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.NotificationChannel
import android.app.NotificationManager
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {

        val title =
            intent.getStringExtra("title")
                ?: "चंद्र सूर्य अलार्म"

        val message =
            intent.getStringExtra("message")
                ?: "पंचांग बदल झाला आहे."

        val id =
            intent.getIntExtra("id", 1)

        showNotification(
            context,
            title,
            message,
            id
        )

        // पुढील राशी / नक्षत्र / चरण बदल
        // पुन्हा आपोआप schedule करा
        val scheduler =
            AlarmScheduler(context)

        scheduler.scheduleAll()
    }

    private fun showNotification(
        context: Context,
        title: String,
        message: String,
        id: Int
    ) {

        val channelId =
            "chandra_alarm_channel"

        val notificationManager =
            context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

        val soundUri =
            Uri.parse(
                "android.resource://${context.packageName}/raw/alarm"
            )

        if (
            Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.O
        ) {

            val audioAttributes =
                AudioAttributes.Builder()
                    .setUsage(
                        AudioAttributes.USAGE_ALARM
                    )
                    .build()

            val channel =
                NotificationChannel(
                    channelId,
                    "चंद्र सूर्य अलार्म",
                    NotificationManager.IMPORTANCE_HIGH
                )

            channel.description =
                "राशी, नक्षत्र आणि चरण बदल अलार्म"

            channel.setSound(
                soundUri,
                audioAttributes
            )

            notificationManager.createNotificationChannel(
                channel
            )
        }

      val notification =
    NotificationCompat.Builder(
        context,
        channelId
    )
        .setSmallIcon(
            android.R.drawable.ic_dialog_info
        )
        .setContentTitle(title)
        .setContentText(message)
        .setPriority(
            NotificationCompat.PRIORITY_HIGH
        )
        .setAutoCancel(true)
        .build()

        notificationManager.notify(
            id,
            notification
        )
    }
}
