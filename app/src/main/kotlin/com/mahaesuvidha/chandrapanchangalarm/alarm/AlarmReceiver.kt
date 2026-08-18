package com.mahaesuvidha.chandrapanchangalarm.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.mahaesuvidha.chandrapanchangalarm.R

class AlarmReceiver : BroadcastReceiver() {

    companion object {

        private const val CHANNEL_ID =
            "chandra_alarm_sound_v2"

        private const val CHANNEL_NAME =
            "चंद्र सूर्य अलार्म"

        private fun createChannel(
            context: Context
        ) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                val manager =
                    context.getSystemService(
                        NotificationManager::class.java
                    )

                val soundUri =
                    RingtoneManager.getDefaultUri(
                        RingtoneManager.TYPE_NOTIFICATION
                    )

                val audioAttributes =
                    AudioAttributes.Builder()
                        .setUsage(
                            AudioAttributes.USAGE_NOTIFICATION
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
                    )

                channel.description =
                    "चंद्र आणि सूर्य राशी, नक्षत्र व चरण बदल अलार्म"

                channel.setSound(
                    soundUri,
                    audioAttributes
                )

                channel.enableVibration(true)

                channel.vibrationPattern =
                    longArrayOf(
                        0,
                        500,
                        300,
                        500
                    )

                manager.createNotificationChannel(
                    channel
                )
            }
        }
    }

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {

        createChannel(context)

        val title =
            intent.getStringExtra("title")
                ?: "चंद्र सूर्य अलार्म"

        val message =
            intent.getStringExtra("message")
                ?: "पुढील बदल झाला आहे."

        val notificationManager =
            context.getSystemService(
                NotificationManager::class.java
            )

        val notification =
            NotificationCompat.Builder(
                context,
                CHANNEL_ID
            )
                .setSmallIcon(
                    R.drawable.ic_launcher_foreground
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
                .setDefaults(
                    NotificationCompat.DEFAULT_SOUND or
                            NotificationCompat.DEFAULT_VIBRATE
                )
                .build()

        val notificationId =
            intent.getIntExtra(
                "id",
                System.currentTimeMillis().toInt()
            )

        notificationManager.notify(
            notificationId,
            notification
        )
    }
}
