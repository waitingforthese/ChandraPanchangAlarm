package com.mahaesuvidha.chandrapanchangalarm.model

import swisseph.SweConst
import swisseph.SweDate
import swisseph.SwissEph
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object LiveMoonCalculator {

    // ==========================================
    // JULIAN DAY
    // ==========================================

    private fun getJulianDay(): Double {

        val calendar =
            Calendar.getInstance(
                TimeZone.getTimeZone("UTC")
            )

        val year =
            calendar.get(Calendar.YEAR)

        val month =
            calendar.get(Calendar.MONTH) + 1

        val day =
            calendar.get(Calendar.DAY_OF_MONTH)

        val hour =
            calendar.get(Calendar.HOUR_OF_DAY) +
                    calendar.get(Calendar.MINUTE) / 60.0 +
                    calendar.get(Calendar.SECOND) / 3600.0

        return SweDate.getJulDay(
            year,
            month,
            day,
            hour,
            SweDate.SE_GREG_CAL
        )
    }


    // ==========================================
    // MOON LONGITUDE
    // ==========================================

    fun getMoonLongitude(): Double {

        val swe =
            SwissEph()

        swe.swe_set_sid_mode(
            SweConst.SE_SIDM_LAHIRI,
            0.0,
            0.0
        )

        val xx =
            DoubleArray(6)

        val serr =
            StringBuffer()

        swe.swe_calc_ut(
            getJulianDay(),
            SweConst.SE_MOON,
            SweConst.SEFLG_SWIEPH or
                    SweConst.SEFLG_SIDEREAL,
            xx,
            serr
        )

        return xx[0]
    }


    // ==========================================
    // CURRENT RASHI
    // ==========================================

    fun getCurrentRashi(): Rashi {

        val longitude =
            getMoonLongitude()

        val index =
            (longitude / 30.0)
                .toInt()
                .coerceIn(0, 11)

        return Rashi.entries[index]
    }


    // ==========================================
    // CURRENT NAKSHATRA
    // ==========================================

    fun getCurrentNakshatra(): Nakshatra {

        val longitude =
            getMoonLongitude()

        val nakshatraSize =
            360.0 / 27.0

        val index =
            (longitude / nakshatraSize)
                .toInt()
                .coerceIn(0, 26)

        return Nakshatra.entries[index]
    }


    // ==========================================
    // CURRENT PADA
    // ==========================================

    fun getCurrentCharan(): Int {

        val longitude =
            getMoonLongitude()

        val nakshatraSize =
            360.0 / 27.0

        val padaSize =
            nakshatraSize / 4.0

        return (
            (longitude % nakshatraSize) /
                    padaSize
            ).toInt() + 1
    }


    // ==========================================
    // NEXT RASHI
    // ==========================================

    private fun getNextRashi(): String {

        val current =
            getCurrentRashi()

        val nextIndex =
            (current.ordinal + 1) % Rashi.entries.size

        return Rashi.entries[nextIndex]
            .marathi
    }


    // ==========================================
    // NEXT NAKSHATRA
    // ==========================================

    private fun getNextNakshatra(): String {

        val current =
            getCurrentNakshatra()

        val nextIndex =
            (current.ordinal + 1) %
                    Nakshatra.entries.size

        return Nakshatra.entries[nextIndex]
            .marathi
    }


    // ==========================================
    // NEXT CHARAN
    // ==========================================

    private fun getNextCharan(): String {

        val current =
            getCurrentCharan()

        val next =
            if (current >= 4) 1
            else current + 1

        return "चरण $next"
    }


    // ==========================================
    // FORMAT TIME
    // ==========================================

    private fun formatTime(
        millis: Long
    ): String {

        val formatter =
            SimpleDateFormat(
                "dd-MM-yyyy HH:mm",
                Locale.getDefault()
            )

        return formatter.format(
            Date(millis)
        )
    }


    // ==========================================
    // CURRENT MOON STATE
    // ==========================================

    fun getCurrentMoonState(): MoonState {

        val currentRashi =
            getCurrentRashi()

        val currentNakshatra =
            getCurrentNakshatra()

        val currentPada =
            getCurrentCharan()

        val now =
            System.currentTimeMillis()

        /*
         * सध्या next change timing अंदाजे आहे.
         * पुढच्या टप्प्यात Swiss Ephemeris वापरून
         * exact राशी / नक्षत्र / चरण बदलाची वेळ शोधू.
         */

        val nextRashiMillis =
            now + (60 * 60 * 1000L)

        val nextNakshatraMillis =
            now + (60 * 60 * 1000L)

        val nextCharanMillis =
            now + (60 * 60 * 1000L)


        return MoonState(

            location =
                "Daund",

            rashi =
                currentRashi,

            nakshatra =
                currentNakshatra,

            pada =
                currentPada,


            // ======================================
            // NEXT RASHI
            // ======================================

            nextRashi =
                getNextRashi(),

            nextRashiTime =
                formatTime(
                    nextRashiMillis
                ),

            nextRashiMillis =
                nextRashiMillis,


            // ======================================
            // NEXT NAKSHATRA
            // ======================================

            nextNakshatra =
                getNextNakshatra(),

            nextNakshatraTime =
                formatTime(
                    nextNakshatraMillis
                ),

            nextNakshatraMillis =
                nextNakshatraMillis,


            // ======================================
            // NEXT CHARAN
            // ======================================

            nextCharan =
                getNextCharan(),

            nextCharanTime =
                formatTime(
                    nextCharanMillis
                ),

            nextCharanMillis =
                nextCharanMillis
        )
    }
}
