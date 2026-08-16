package com.mahaesuvidha.chandrapanchangalarm.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings

class AlarmScheduler(
    private val context: Context
) {

    private val alarmManager =
        context.getSystemService(
            AlarmManager::class.java
        )

    /*
     * कोणत्याही exact time साठी Alarm schedule
     */
    private fun scheduleExactAlarm(
        timeMillis: Long,
        requestCode: Int,
        title: String,
        message: String,
        alarmType: String
    ) {

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

                /*
                 * AlarmReceiver मध्ये हाच key वापरला आहे
                 */
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

        /*
         * Android 12+ Exact Alarm Permission
         */
        if (
            Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.S
        ) {

            if (
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
        }

        /*
         * Phone lock / Doze mode असताना सुद्धा Alarm
         */
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            timeMillis,
            pendingIntent
        )
    }


    /*
     * LIVE Moon Calculator कडून आलेल्या
     * पुढील बदलाच्या वेळेस Alarm schedule
     */
    fun scheduleNextLiveAlarm(
        timeMillis: Long,
        changeType: String,
        changeText: String
    ) {

        /*
         * जुनी वेळ असेल तर Alarm schedule करू नका
         */
        if (
            timeMillis <=
            System.currentTimeMillis()
        ) {
            return
        }

        when (changeType) {

            "rashi" -> {

                scheduleExactAlarm(
                    timeMillis = timeMillis,
                    requestCode = 1001,

                    title =
                        "🌙 चंद्र राशीमध्ये बदल",

                    message =
                        "चंद्र राशी बदल: $changeText",

                    alarmType =
                        "rashi"
                )
            }


            "nakshatra" -> {

                scheduleExactAlarm(
                    timeMillis = timeMillis,
                    requestCode = 1002,

                    title =
                        "⭐ नक्षत्रामध्ये बदल",

                    message =
                        "नक्षत्र बदल: $changeText",

                    alarmType =
                        "nakshatra"
                )
            }


            "charan" -> {

                scheduleExactAlarm(
                    timeMillis = timeMillis,
                    requestCode = 1003,

                    title =
                        "🔔 चरणामध्ये बदल",

                    message =
                        "चरण बदल: $changeText",

                    alarmType =
                        "charan"
                )
            }


            else -> {

                scheduleExactAlarm(
                    timeMillis = timeMillis,
                    requestCode = 1004,

                    title =
                        "🌙 चंद्र पंचांग अलार्म",

                    message =
                        changeText,

                    alarmType =
                        "rashi"
                )
            }
        }
    }


    /*
     * राशी बदलण्यासाठी Test Alarm
     */
    fun scheduleRashiAlarm(
        delayMillis: Long
    ) {

        val alarmTime =
            System.currentTimeMillis() +
                    delayMillis

        scheduleExactAlarm(
            timeMillis = alarmTime,

            requestCode = 2001,

            title =
                "🌙 चंद्र राशीमध्ये बदल",

            message =
                "हा राशी बदलाचा Test Alarm आहे.",

            alarmType =
                "rashi"
        )
    }


    /*
     * नक्षत्र बदलण्यासाठी Test Alarm
     */
    fun scheduleNakshatraAlarm(
        delayMillis: Long
    ) {

        val alarmTime =
            System.currentTimeMillis() +
                    delayMillis

        scheduleExactAlarm(
            timeMillis = alarmTime,

            requestCode = 2002,

            title =
                "⭐ नक्षत्रामध्ये बदल",

            message =
                "हा नक्षत्र बदलाचा Test Alarm आहे.",

            alarmType =
                "nakshatra"
        )
    }


    /*
     * चरण बदलण्यासाठी Test Alarm
     */
    fun scheduleCharanAlarm(
        delayMillis: Long
    ) {

        val alarmTime =
            System.currentTimeMillis() +
                    delayMillis

        scheduleExactAlarm(
            timeMillis = alarmTime,

            requestCode = 2003,

            title =
                "🔔 नक्षत्र चरणमध्ये बदल",

            message =
                "हा चरण बदलाचा Test Alarm आहे.",

            alarmType =
                "charan"
        )
    }


    /*
     * सामान्य 10 सेकंदांचा Test Alarm
     */
    fun scheduleTestAlarm(
        delayMillis: Long
    ) {

        scheduleRashiAlarm(
            delayMillis
        )
    }


    /*
     * सर्व Moon Change Alarms Cancel करण्यासाठी
     */
    fun cancelAllAlarms() {

        cancelAlarm(1001)
        cancelAlarm(1002)
        cancelAlarm(1003)
        cancelAlarm(1004)

        cancelAlarm(2001)
        cancelAlarm(2002)
        cancelAlarm(2003)
    }


    private fun cancelAlarm(
        requestCode: Int
    ) {

        val intent =
            Intent(
                context,
                AlarmReceiver::class.java
            )

        val pendingIntent =
            PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or
                        PendingIntent.FLAG_IMMUTABLE
            )

        alarmManager.cancel(
            pendingIntent
        )

        pendingIntent.cancel()
    }
}
