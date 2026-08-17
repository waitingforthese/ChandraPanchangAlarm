package com.mahaesuvidha.chandrapanchangalarm.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import com.mahaesuvidha.chandrapanchangalarm.model.LiveMoonCalculator

class AlarmScheduler(
    private val context: Context
) {

    private fun scheduleAlarm(
        alarmTime: Long,
        requestCode: Int,
        title: String,
        message: String,
        alarmType: String
    ) {

        val alarmManager =
            context.getSystemService(
                AlarmManager::class.java
            )

        val intent =
            Intent(
                context,
                AlarmReceiver::class.java
            ).apply {

                putExtra(
                    "title",
                    title
                )

                putExtra(
                    "message",
                    message
                )

                putExtra(
                    "alarm_type",
                    alarmType
                )
            }

        val pendingIntent =
            PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or
                        PendingIntent.FLAG_IMMUTABLE
            )

        if (
            Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.S &&
            !alarmManager.canScheduleExactAlarms()
        ) {

            val settingsIntent =
                Intent(
                    Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                ).apply {

                    addFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK
                    )
                }

            context.startActivity(
                settingsIntent
            )

            return
        }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarmTime,
            pendingIntent
        )
    }

    // --------------------------------------------------
    // TEST RASHI
    // --------------------------------------------------

    fun scheduleRashiAlarm(
        delayMillis: Long
    ) {

        scheduleAlarm(
            alarmTime =
                System.currentTimeMillis() +
                        delayMillis,

            requestCode =
                1001,

            title =
                "चंद्र राशीमध्ये बदल",

            message =
                "चंद्र राशीमध्ये बदल झाला आहे.",

            alarmType =
                "rashi"
        )
    }

    // --------------------------------------------------
    // TEST NAKSHATRA
    // --------------------------------------------------

    fun scheduleNakshatraAlarm(
        delayMillis: Long
    ) {

        scheduleAlarm(
            alarmTime =
                System.currentTimeMillis() +
                        delayMillis,

            requestCode =
                1002,

            title =
                "नक्षत्रामध्ये बदल",

            message =
                "चंद्राच्या नक्षत्रामध्ये बदल झाला आहे.",

            alarmType =
                "nakshatra"
        )
    }

    // --------------------------------------------------
    // TEST CHARAN
    // --------------------------------------------------

    fun scheduleCharanAlarm(
        delayMillis: Long
    ) {

        scheduleAlarm(
            alarmTime =
                System.currentTimeMillis() +
                        delayMillis,

            requestCode =
                1003,

            title =
                "नक्षत्र चरणमध्ये बदल",

            message =
                "नक्षत्र चरणमध्ये बदल झाला आहे.",

            alarmType =
                "charan"
        )
    }

    // --------------------------------------------------
    // SCHEDULE ALL THREE LIVE ALARMS
    // --------------------------------------------------

    fun scheduleNextLiveAlarms() {

        scheduleNextRashiAlarm()

        scheduleNextNakshatraAlarm()

        scheduleNextCharanAlarm()
    }

    // --------------------------------------------------
    // NEXT RASHI
    // --------------------------------------------------

    fun scheduleNextRashiAlarm() {

        val state =
            LiveMoonCalculator
                .getCurrentMoonState()

        scheduleAlarm(

            alarmTime =
                state.nextRashiMillis,

            requestCode =
                2001,

            title =
                "चंद्र राशीमध्ये बदल",

            message =
                state.nextRashi,

            alarmType =
                "rashi"
        )
    }

    // --------------------------------------------------
    // NEXT NAKSHATRA
    // --------------------------------------------------

    fun scheduleNextNakshatraAlarm() {

        val state =
            LiveMoonCalculator
                .getCurrentMoonState()

        scheduleAlarm(

            alarmTime =
                state.nextNakshatraMillis,

            requestCode =
                2002,

            title =
                "नक्षत्रामध्ये बदल",

            message =
                state.nextNakshatra,

            alarmType =
                "nakshatra"
        )
    }

    // --------------------------------------------------
    // NEXT CHARAN
    // --------------------------------------------------

    fun scheduleNextCharanAlarm() {

        val state =
            LiveMoonCalculator
                .getCurrentMoonState()

        scheduleAlarm(

            alarmTime =
                state.nextCharanMillis,

            requestCode =
                2003,

            title =
                "नक्षत्र चरणमध्ये बदल",

            message =
                state.nextCharan,

            alarmType =
                "charan"
        )
    }

    // --------------------------------------------------
    // AFTER ALARM → SCHEDULE NEXT SAME TYPE
    // --------------------------------------------------

    fun scheduleNextAlarmForType(
        alarmType: String
    ) {

        when (alarmType) {

            "rashi" ->
                scheduleNextRashiAlarm()

            "nakshatra" ->
                scheduleNextNakshatraAlarm()

            "charan" ->
                scheduleNextCharanAlarm()
        }
    }
}
