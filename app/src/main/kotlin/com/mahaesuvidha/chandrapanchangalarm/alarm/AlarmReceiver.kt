package com.mahaesuvidha.chandrapanchangalarm.alarm

import android.app.BroadcastReceiver
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class AlarmReceiver : BroadcastReceiver() {

    companion object {

        private const val CHANNEL_ID =
            "chandra_sun_alarm_v2"

        private const val CHANNEL_NAME =
            "चंद्र सूर्य अलार्म"

    }

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {

        val title =
            intent.getStringExtra("title")
                ?: "चंद्र सूर्य अलार्म"

        val message =
            intent.getStringExtra("message")
                ?: "पुढील बदलाची सूचना"

        val notificationId =
            intent.getIntExtra(
                "id",
                100
            )

        createNotificationChannel(context)

        val notification =
            NotificationCompat.Builder(
                context,
                CHANNEL_ID
            )
                .setSmallIcon(
                    android.R.drawable.ic_dialog_info
                )
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(message)
                )
                .setPriority(
                    NotificationCompat.PRIORITY_HIGH
                )
                .setCategory(
                    NotificationCompat.CATEGORY_ALARM
                )
                .setAutoCancel(true)
                .build()

        try {

            NotificationManagerCompat
                .from(context)
                .notify(
                    notificationId,
                    notification
                )

        } catch (e: SecurityException) {

            e.printStackTrace()

        }
    }

    private fun createNotificationChannel(
        context: Context
    ) {

        if (
            Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.O
        ) {

            val soundUri =
                RingtoneManager.getDefaultUri(
                    RingtoneManager.TYPE_ALARM
                )

            val audioAttributes =
                AudioAttributes.Builder()
                    .setUsage(
                        AudioAttributes.USAGE_ALARM
                    )
                    .setContentType(
                        AudioAttributes.CONTENT_TYPE_SONIFICATION
                    )
                    .build()

            val channel =
                NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {

                    description =
                        "चंद्र आणि सूर्य बदल अलार्म"

                    enableVibration(true)

                    setSound(
                        soundUri,
                        audioAttributes
                    )
                }

            val manager =
                context.getSystemService(
                    NotificationManager::class.java
                )

            manager.createNotificationChannel(
                channel
            )
        }
    }
}
