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

    private val alarmManager: AlarmManager =
        context.getSystemService(
            AlarmManager::class.java
        )

    /*
     * Main alarm schedule function
     */
    fun scheduleMoonAlarm(
        triggerTimeMillis: Long,
        changeType: String,
        changeText: String
    ) {

        val requestCode =
            when (changeType) {
                "rashi" -> 1001
                "nakshatra" -> 1002
                "charan" -> 1003
                else -> 1000
            }

        val title =
            when (changeType) {
                "rashi" ->
                    "🌙 चंद्र राशीमध्ये बदल"

                "nakshatra" ->
                    "⭐ नक्षत्रामध्ये बदल"

                "charan" ->
                    "🔔 नक्षत्र चरणमध्ये बदल"

                else ->
                    "🌙 चंद्र पंचांग अलार्म"
            }

        val message =
            when (changeType) {
                "rashi" ->
                    "चंद्र राशीमध्ये बदल झाला आहे: $changeText"

                "nakshatra" ->
                    "चंद्र नक्षत्रामध्ये बदल झाला आहे: $changeText"

                "charan" ->
                    "चंद्राच्या चरणमध्ये बदल झाला आहे: $changeText"

                else ->
                    changeText
            }

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
                 * हे AlarmReceiver मध्ये वापरले जाईल
                 */
                putExtra(
                    "alarm_type",
                    changeType
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
         * Android 12+ Exact Alarm permission
         */
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

        /*
         * फोन lock असला,
         * Doze mode मध्ये असला
         * तरी शक्य तितका exact alarm
         */
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerTimeMillis,
            pendingIntent
        )
    }

    /*
     * पुढील Live Moon Change Schedule
     */
    fun scheduleNextMoonChange(
        triggerTimeMillis: Long,
        changeType: String,
        changeText: String
    ) {

        scheduleMoonAlarm(
            triggerTimeMillis = triggerTimeMillis,
            changeType = changeType,
            changeText = changeText
        )
    }

    /*
     * राशी Test Alarm
     */
    fun scheduleRashiAlarm(
        delayMillis: Long
    ) {

        val triggerTime =
            System.currentTimeMillis() +
                    delayMillis

        scheduleMoonAlarm(
            triggerTimeMillis = triggerTime,
            changeType = "rashi",
            changeText = "Test राशी बदल"
        )
    }

    /*
     * नक्षत्र Test Alarm
     */
    fun scheduleNakshatraAlarm(
        delayMillis: Long
    ) {

        val triggerTime =
            System.currentTimeMillis() +
                    delayMillis

        scheduleMoonAlarm(
            triggerTimeMillis = triggerTime,
            changeType = "nakshatra",
            changeText = "Test नक्षत्र बदल"
        )
    }

    /*
     * चरण Test Alarm
     */
    fun scheduleCharanAlarm(
        delayMillis: Long
    ) {

        val triggerTime =
            System.currentTimeMillis() +
                    delayMillis

        scheduleMoonAlarm(
            triggerTimeMillis = triggerTime,
            changeType = "charan",
            changeText = "Test चरण बदल"
        )
    }

    /*
     * जुना Test Alarm compatibility
     */
    fun scheduleTestAlarm(
        delayMillis: Long
    ) {

        scheduleRashiAlarm(
            delayMillis
        )
    }

    /*
     * सर्व Moon Alarms Cancel
     */
    fun cancelAllMoonAlarms() {

        val requestCodes =
            listOf(
                1001,
                1002,
                1003
            )

        requestCodes.forEach { requestCode ->

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
        }
    }
}
