package com.mahaesuvidha.chandrapanchangalarm.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import androidx.core.app.NotificationCompat
import com.mahaesuvidha.chandrapanchangalarm.R

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {

        val channelId = "moon_change"

        val manager =
            context.getSystemService(
                NotificationManager::class.java
            )

        /*
         * Notification Channel
         */
        if (
            Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.O
        ) {

            val channel =
                NotificationChannel(
                    channelId,
                    "चंद्र बदल अलार्म",
                    NotificationManager.IMPORTANCE_HIGH
                )

            manager.createNotificationChannel(
                channel
            )
        }

        val title =
            intent.getStringExtra("title")
                ?: "चंद्र पंचांग अलार्म"

        val message =
            intent.getStringExtra("message")
                ?: "चंद्र स्थितीत बदल झाला."

        val alarmType =
            intent.getStringExtra("alarm_type")
                ?: "rashi"

        /*
         * योग्य Marathi MP3
         */
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

        /*
         * Marathi आवाज वाजवा
         */
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

        /*
         * Notification दाखवा
         */
        val notification =
            NotificationCompat.Builder(
                context,
                channelId
            )
                .setSmallIcon(
                    android.R.drawable
                        .ic_lock_idle_alarm
                )
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(
                    NotificationCompat.PRIORITY_HIGH
                )
                .setAutoCancel(true)
                .build()

        manager.notify(
            System.currentTimeMillis()
                .toInt(),
            notification
        )

        /*
         * हा LIVE Alarm असल्यास
         * पुढील चंद्र बदलाचा Alarm schedule करा.
         */
        val isLiveAlarm =
            intent.getBooleanExtra(
                "live_alarm",
                false
            )

        if (isLiveAlarm) {

            try {

                val scheduler =
                    AlarmScheduler(context)

                scheduler.scheduleNextLiveAlarm()

            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
    }
}
