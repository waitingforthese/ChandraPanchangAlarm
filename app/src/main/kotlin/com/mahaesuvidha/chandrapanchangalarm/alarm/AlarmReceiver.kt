package com.mahaesuvidha.chandrapanchangalarm.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import androidx.core.app.NotificationCompat
import com.mahaesuvidha.chandrapanchangalarm.R

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val channelId = "moon_change_v4"

        val manager =
            context.getSystemService(NotificationManager::class.java)

        // Notification Channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                channelId,
                "चंद्र बदल अलार्म",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {

                description =
                    "चंद्र राशी, नक्षत्र आणि चरण बदल सूचना"

                lockscreenVisibility =
                    android.app.Notification.VISIBILITY_PUBLIC

                setSound(
                    null,
                    AudioAttributes.Builder()
                        .setUsage(
                            AudioAttributes.USAGE_ALARM
                        )
                        .build()
                )
            }

            manager.createNotificationChannel(channel)
        }

        val title =
            intent.getStringExtra("title")
                ?: "चंद्र पंचांग अलार्म"

        val message =
            intent.getStringExtra("message")
                ?: "चंद्र स्थितीत बदल झाला."

        // कोणता बदल झाला ते तपासा
        val alarmType =
            intent.getStringExtra("alarm_type")
                ?: "rashi"

        // योग्य MP3 निवडा
        val soundRes = when (alarmType) {

            "rashi" -> R.raw.rashi

            "nakshatra" -> R.raw.nakshatra

            "charan" -> R.raw.charan

            else -> R.raw.rashi
        }

        // योग्य Marathi आवाज वाजवा
        try {

            val mediaPlayer =
                MediaPlayer.create(context, soundRes)

            mediaPlayer.setOnCompletionListener {
                it.release()
            }

            mediaPlayer.start()

        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Lock Screen Notification
        val notification =
            NotificationCompat.Builder(
                context,
                channelId
            )
                .setSmallIcon(
                    android.R.drawable.ic_lock_idle_alarm
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
                .setVisibility(
                    NotificationCompat.VISIBILITY_PUBLIC
                )
                .setAutoCancel(true)
                .build()

        manager.notify(
            System.currentTimeMillis().toInt(),
            notification
        )
    }
}
