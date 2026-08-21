package com.mahaesuvidha.chandrapanchangalarm.model

import swisseph.SweConst
import swisseph.SweDate
import swisseph.SwissEph
import java.util.Calendar
import java.util.TimeZone

object LiveMoonCalculator {

    private val rashiNames = arrayOf(
        "मेष",
        "वृषभ",
        "मिथुन",
        "कर्क",
        "सिंह",
        "कन्या",
        "तुळ",
        "वृश्चिक",
        "धनु",
        "मकर",
        "कुंभ",
        "मीन"
    )

    private val nakshatraNames = arrayOf(
        "अश्विनी",
        "भरणी",
        "कृत्तिका",
        "रोहिणी",
        "मृगशीर्ष",
        "आर्द्रा",
        "पुनर्वसू",
        "पुष्य",
        "आश्लेषा",
        "मघा",
        "पूर्वाफाल्गुनी",
        "उत्तराफाल्गुनी",
        "हस्त",
        "चित्रा",
        "स्वाती",
        "विशाखा",
        "अनुराधा",
        "ज्येष्ठा",
        "मूळ",
        "पूर्वाषाढा",
        "उत्तराषाढा",
        "श्रवण",
        "धनिष्ठा",
        "शततारका",
        "पूर्वाभाद्रपदा",
        "उत्तराभाद्रपदा",
        "रेवती"
    )

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

    fun getCurrentRashi(): String {

        val longitude =
            getMoonLongitude()

        val index =
            (longitude / 30.0)
                .toInt()
                .coerceIn(0, 11)

        return rashiNames[index]
    }

    fun getCurrentNakshatra(): String {

        val longitude =
            getMoonLongitude()

        val nakshatraSize =
            360.0 / 27.0

        val index =
            (longitude / nakshatraSize)
                .toInt()
                .coerceIn(0, 26)

        return nakshatraNames[index]
    }

    fun getCurrentCharan(): Int {

        val longitude =
            getMoonLongitude()

        val padaSize =
            360.0 / 108.0

        return (
            (longitude % (360.0 / 27.0)) /
                    padaSize
            ).toInt() + 1
    }
    fun getCurrentCharan(): Int {

        val longitude =
            getMoonLongitude()

        val padaSize =
            360.0 / 108.0

        return (
            (longitude % (360.0 / 27.0)) /
                    padaSize
            ).toInt() + 1
    }


    // ==========================================
    // CURRENT MOON STATE
    // ==========================================

    fun getCurrentMoonState(): MoonState {

        val rashi =
            getCurrentRashi()

        val nakshatra =
            getCurrentNakshatra()

        val charan =
            getCurrentCharan()

        val now =
            System.currentTimeMillis()

        return MoonState(

            rashi = rashi,

            nakshatra = nakshatra,

            charan = charan,

            nextRashi =
                getNextRashi(),

            nextNakshatra =
                getNextNakshatra(),

            nextCharan =
                getNextCharan(),

            nextRashiMillis =
                now + (60 * 60 * 1000L),

            nextNakshatraMillis =
                now + (60 * 60 * 1000L),

            nextCharanMillis =
                now + (60 * 60 * 1000L)
        )
    }


    // ==========================================
    // NEXT RASHI
    // ==========================================

    private fun getNextRashi(): String {

        val longitude =
            getMoonLongitude()

        val currentIndex =
            (longitude / 30.0)
                .toInt()
                .coerceIn(0, 11)

        val nextIndex =
            (currentIndex + 1) % 12

        return rashiNames[nextIndex]
    }


    // ==========================================
    // NEXT NAKSHATRA
    // ==========================================

    private fun getNextNakshatra(): String {

        val longitude =
            getMoonLongitude()

        val nakshatraSize =
            360.0 / 27.0

        val currentIndex =
            (longitude / nakshatraSize)
                .toInt()
                .coerceIn(0, 26)

        val nextIndex =
            (currentIndex + 1) % 27

        return nakshatraNames[nextIndex]
    }


    // ==========================================
    // NEXT CHARAN
    // ==========================================

    private fun getNextCharan(): String {

        val current =
            getCurrentCharan()

        return if (current >= 4) {

            "चरण 1"

        } else {

            "चरण ${current + 1}"
        }
    }
}
