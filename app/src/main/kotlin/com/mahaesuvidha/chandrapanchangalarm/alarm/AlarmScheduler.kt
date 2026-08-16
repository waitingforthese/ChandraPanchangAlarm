package com.mahaesuvidha.chandrapanchangalarm.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings

class AlarmScheduler(private val context: Context) {

    fun scheduleTestAlarm(delayMillis: Long) {

        val alarmManager = context.getSystemService(AlarmManager::class.java)

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("title", "चंद्र पंचांग अलार्म")
            putExtra("message", "हा Test Alarm आहे.")
        }

        val pending = PendingIntent.getBroadcast(
            context,
            1001,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        )

        val at = System.currentTimeMillis() + delayMillis

        scheduleExact(alarmManager, at, pending)
    }


    fun scheduleMoonChangeAlarm(
        triggerAtMillis: Long,
        title: String,
        message: String
    ) {

        val alarmManager = context.getSystemService(AlarmManager::class.java)

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("title", title)
            putExtra("message", message)
        }

        /*
         * Moon change साठी वेगळा request code.
         * त्यामुळे Test Alarm आणि Moon Alarm एकमेकांना replace करणार नाहीत.
         */
        val pending = PendingIntent.getBroadcast(
            context,
            2001,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        )

        scheduleExact(
            alarmManager,
            triggerAtMillis,
            pending
        )
    }


    private fun scheduleExact(
        alarmManager: AlarmManager,
        triggerAtMillis: Long,
        pendingIntent: PendingIntent
    ) {

        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            !alarmManager.canScheduleExactAlarms()
        ) {

            context.startActivity(
                Intent(
                    Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )

            return
        }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            pendingIntent
        )
    }
}
