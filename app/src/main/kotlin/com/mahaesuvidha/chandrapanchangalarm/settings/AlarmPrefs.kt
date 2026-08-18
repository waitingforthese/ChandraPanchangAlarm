package com.mahaesuvidha.chandrapanchangalarm.settings

import android.content.Context

class AlarmPrefs(
    context: Context
) {

    private val p =
        context.getSharedPreferences(
            "alarm_prefs",
            Context.MODE_PRIVATE
        )


    // ==========================================
    // GRAHA
    // ==========================================

    var moon: Boolean
        get() = p.getBoolean("moon", true)
        set(value) {
            p.edit()
                .putBoolean("moon", value)
                .apply()
        }


    var sun: Boolean
        get() = p.getBoolean("sun", true)
        set(value) {
            p.edit()
                .putBoolean("sun", value)
                .apply()
        }


    // ==========================================
    // GRAHA BADAL
    // ==========================================

    var rashi: Boolean
        get() = p.getBoolean("rashi", true)
        set(value) {
            p.edit()
                .putBoolean("rashi", value)
                .apply()
        }


    var nak: Boolean
        get() = p.getBoolean("nak", true)
        set(value) {
            p.edit()
                .putBoolean("nak", value)
                .apply()
        }


    var pada: Boolean
        get() = p.getBoolean("pada", true)
        set(value) {
            p.edit()
                .putBoolean("pada", value)
                .apply()
        }


    // ==========================================
    // PANCHANG BADAL
    // ==========================================

    var tithi: Boolean
        get() = p.getBoolean("tithi", true)
        set(value) {
            p.edit()
                .putBoolean("tithi", value)
                .apply()
        }


    var yoga: Boolean
        get() = p.getBoolean("yoga", true)
        set(value) {
            p.edit()
                .putBoolean("yoga", value)
                .apply()
        }


    var karana: Boolean
        get() = p.getBoolean("karana", true)
        set(value) {
            p.edit()
                .putBoolean("karana", value)
                .apply()
        }


    var paksha: Boolean
        get() = p.getBoolean("paksha", true)
        set(value) {
            p.edit()
                .putBoolean("paksha", value)
                .apply()
        }
}
