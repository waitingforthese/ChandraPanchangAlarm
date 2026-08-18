package com.mahaesuvidha.chandrapanchangalarm.settings

import android.content.Context

class AlarmPrefs(
    context: Context
) {

    private val prefs =
        context.getSharedPreferences(
            "alarm_prefs",
            Context.MODE_PRIVATE
        )

    // ==========================================
    // MOON
    // ==========================================

    var moon: Boolean
        get() =
            prefs.getBoolean(
                "moon",
                true
            )
        set(value) {
            prefs.edit()
                .putBoolean(
                    "moon",
                    value
                )
                .apply()
        }


    // ==========================================
    // SUN
    // ==========================================

    var sun: Boolean
        get() =
            prefs.getBoolean(
                "sun",
                true
            )
        set(value) {
            prefs.edit()
                .putBoolean(
                    "sun",
                    value
                )
                .apply()
        }


    // ==========================================
    // RASHI
    // ==========================================

    var rashi: Boolean
        get() =
            prefs.getBoolean(
                "rashi",
                true
            )
        set(value) {
            prefs.edit()
                .putBoolean(
                    "rashi",
                    value
                )
                .apply()
        }


    // ==========================================
    // NAKSHATRA
    // ==========================================

    var nak: Boolean
        get() =
            prefs.getBoolean(
                "nak",
                true
            )
        set(value) {
            prefs.edit()
                .putBoolean(
                    "nak",
                    value
                )
                .apply()
        }


    // ==========================================
    // PADA / CHARAN
    // ==========================================

    var pada: Boolean
        get() =
            prefs.getBoolean(
                "pada",
                true
            )
        set(value) {
            prefs.edit()
                .putBoolean(
                    "pada",
                    value
                )
                .apply()
        }


    // ==========================================
    // PANCHANG - TITHI
    // ==========================================

    var tithi: Boolean
        get() =
            prefs.getBoolean(
                "tithi",
                true
            )
        set(value) {
            prefs.edit()
                .putBoolean(
                    "tithi",
                    value
                )
                .apply()
        }


    // ==========================================
    // PANCHANG - YOGA
    // ==========================================

    var yoga: Boolean
        get() =
            prefs.getBoolean(
                "yoga",
                true
            )
        set(value) {
            prefs.edit()
                .putBoolean(
                    "yoga",
                    value
                )
                .apply()
        }


    // ==========================================
    // PANCHANG - KARANA
    // ==========================================

    var karana: Boolean
        get() =
            prefs.getBoolean(
                "karana",
                true
            )
        set(value) {
            prefs.edit()
                .putBoolean(
                    "karana",
                    value
                )
                .apply()
        }


    // ==========================================
    // PANCHANG - PAKSHA
    // ==========================================

    var paksha: Boolean
        get() =
            prefs.getBoolean(
                "paksha",
                true
            )
        set(value) {
            prefs.edit()
                .putBoolean(
                    "paksha",
                    value
                )
                .apply()
        }
}
