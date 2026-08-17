package com.mahaesuvidha.chandrapanchangalarm.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.core.app.NotificationCompat
import com.mahaesuvidha.chandrapanchangalarm.R

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {

        val channelId =
            "moon_change"

        val manager =
            context.getSystemService(
                NotificationManager::class.java
            )

        manager.createNotificationChannel(
            NotificationChannel(
                channelId,
                "चंद्र बदल अलार्म",
                NotificationManager.IMPORTANCE_HIGH
            )
        )

        val title =
            intent.getStringExtra("title")
                ?: "चंद्र पंचांग अलार्म"

        val message =
            intent.getStringExtra("message")
                ?: "चंद्र स्थितीत बदल झाला."

        val alarmType =
            intent.getStringExtra("alarm_type")
                ?: "rashi"

        // --------------------------------------------
        // योग्य Marathi आवाज
        // --------------------------------------------

        val soundRes =
            when (alarmType) {

                "rashi" ->
                    R.raw.rashi

                "nakshatra" ->
                    R.raw.nakshatra

                "charan" ->
                    R.raw.charan

                else ->
                    R.raw.rashi
            }

        try {

            val mediaPlayer =
                MediaPlayer.create(
                    context,
                    soundRes
                )

            mediaPlayer.setOnCompletionListener {
                it.release()
            }

            mediaPlayer.start()

        } catch (e: Exception) {

            e.printStackTrace()
        }

        // --------------------------------------------
        // Notification
        // --------------------------------------------

        val notification =
            NotificationCompat.Builder(
                context,
                channelId
            )

                .setSmallIcon(
                    android.R.drawable.ic_lock_idle_alarm
                )

                .setContentTitle(
                    title
                )

                .setContentText(
                    message
                )

                .setPriority(
                    NotificationCompat.PRIORITY_HIGH
                )

                .setCategory(
                    NotificationCompat.CATEGORY_ALARM
                )

                .setAutoCancel(
                    true
                )

                .build()

        manager.notify(
            System.currentTimeMillis().toInt(),
            notification
        )

        // --------------------------------------------
        // पुढचा त्याच प्रकारचा Alarm schedule करा
        // --------------------------------------------

        try {

            AlarmScheduler(context)
                .scheduleNextAlarmForType(
                    alarmType
                )

        } catch (e: Exception) {

            e.printStackTrace()
        }
    }
}
