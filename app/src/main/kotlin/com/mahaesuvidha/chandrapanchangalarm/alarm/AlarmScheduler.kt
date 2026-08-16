package com.mahaesuvidha.chandrapanchangalarm.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings

class AlarmScheduler(private val context: Context) {

    private fun scheduleAlarm(
        delayMillis: Long,
        requestCode: Int,
        title: String,
        message: String,
        soundType: String
    ) {

        val alarmManager =
            context.getSystemService(AlarmManager::class.java)

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("title", title)
            putExtra("message", message)
            putExtra("soundType", soundType)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        )

        val alarmTime =
            System.currentTimeMillis() + delayMillis

        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            !alarmManager.canScheduleExactAlarms()
        ) {

            val settingsIntent =
                Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }

            context.startActivity(settingsIntent)
            return
        }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarmTime,
            pendingIntent
        )
    }

    // चंद्र राशी बदल
    fun scheduleRashiAlarm(delayMillis: Long) {

        scheduleAlarm(
            delayMillis = delayMillis,
            requestCode = 1001,
            title = "चंद्र राशीमध्ये बदल",
            message = "चंद्र राशीमध्ये बदल झाला आहे.",
            soundType = "rashi"
        )
    }

    // नक्षत्र बदल
    fun scheduleNakshatraAlarm(delayMillis: Long) {

        scheduleAlarm(
            delayMillis = delayMillis,
            requestCode = 1002,
            title = "नक्षत्रामध्ये बदल",
            message = "चंद्राच्या नक्षत्रामध्ये बदल झाला आहे.",
            soundType = "nakshatra"
        )
    }

    // नक्षत्र चरण बदल
    fun scheduleCharanAlarm(delayMillis: Long) {

        scheduleAlarm(
            delayMillis = delayMillis,
            requestCode = 1003,
            title = "नक्षत्र चरणमध्ये बदल",
            message = "नक्षत्र चरणमध्ये बदल झाला आहे.",
            soundType = "charan"
        )
    }

    // 10 सेकंद Test Alarm
    fun scheduleTestAlarm(delayMillis: Long) {

        scheduleRashiAlarm(delayMillis)
    }
}
