package com.mahaesuvidha.chandrapanchangalarm.model

import swisseph.SweConst
import swisseph.SweDate
import swisseph.SwissEph
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

object LivePanchangCalculator {

    // ==========================================
    // TITHI NAMES
    // ==========================================

    private val tithiNames = arrayOf(
        "प्रतिपदा",
        "द्वितीया",
        "तृतीया",
        "चतुर्थी",
        "पंचमी",
        "षष्ठी",
        "सप्तमी",
        "अष्टमी",
        "नवमी",
        "दशमी",
        "एकादशी",
        "द्वादशी",
        "त्रयोदशी",
        "चतुर्दशी",
        "पौर्णिमा",
        "प्रतिपदा",
        "द्वितीया",
        "तृतीया",
        "चतुर्थी",
        "पंचमी",
        "षष्ठी",
        "सप्तमी",
        "अष्टमी",
        "नवमी",
        "दशमी",
        "एकादशी",
        "द्वादशी",
        "त्रयोदशी",
        "चतुर्दशी",
        "अमावस्या"
    )

    // ==========================================
    // YOGA NAMES
    // ==========================================

    private val yogaNames = arrayOf(
        "विष्कंभ",
        "प्रीति",
        "आयुष्मान",
        "सौभाग्य",
        "शोभन",
        "अतिगंड",
        "सुकर्मा",
        "धृति",
        "शूल",
        "गंड",
        "वृद्धि",
        "ध्रुव",
        "व्याघात",
        "हर्षण",
        "वज्र",
        "सिद्धी",
        "व्यतीपात",
        "वरीयान",
        "परिघ",
        "शिव",
        "सिद्ध",
        "साध्य",
        "शुभ",
        "शुक्ल",
        "ब्रह्म",
        "इंद्र",
        "वैधृति"
    )

    // ==========================================
    // DATE / TIME FORMAT
    // ==========================================

    private fun formatDateTime(
        millis: Long
    ): String {

        val formatter =
            SimpleDateFormat(
                "dd-MM-yyyy HH:mm",
                Locale.getDefault()
            )

        formatter.timeZone =
            TimeZone.getDefault()

        return formatter.format(millis)
    }

    private fun formatDate(
        millis: Long
    ): String {

        val formatter =
            SimpleDateFormat(
                "dd-MM-yyyy",
                Locale.getDefault()
            )

        formatter.timeZone =
            TimeZone.getDefault()

        return formatter.format(millis)
    }

    // ==========================================
    // WEEKDAY
    // ==========================================

    private fun getWeekday(
        millis: Long
    ): String {

        val calendar =
            Calendar.getInstance()

        calendar.timeInMillis =
            millis

        return when (
            calendar.get(Calendar.DAY_OF_WEEK)
        ) {
            Calendar.SUNDAY -> "रविवार"
            Calendar.MONDAY -> "सोमवार"
            Calendar.TUESDAY -> "मंगळवार"
            Calendar.WEDNESDAY -> "बुधवार"
            Calendar.THURSDAY -> "गुरुवार"
            Calendar.FRIDAY -> "शुक्रवार"
            Calendar.SATURDAY -> "शनिवार"
            else -> ""
        }
    }

    // ==========================================
    // JULIAN DAY
    // ==========================================

    private fun getJulianDay(
        millis: Long
    ): Double {

        val calendar =
            Calendar.getInstance(
                TimeZone.getTimeZone("UTC")
            )

        calendar.timeInMillis =
            millis

        val year =
            calendar.get(Calendar.YEAR)

        val month =
            calendar.get(Calendar.MONTH) + 1

        val day =
            calendar.get(Calendar.DAY_OF_MONTH)

        val hour =
            calendar.get(Calendar.HOUR_OF_DAY) +
                    calendar.get(Calendar.MINUTE) / 60.0 +
                    calendar.get(Calendar.SECOND) / 3600.0 +
                    calendar.get(Calendar.MILLISECOND) / 3600000.0

        return SweDate.getJulDay(
            year,
            month,
            day,
            hour,
            SweDate.SE_GREG_CAL
        )
    }

    // ==========================================
    // SIDEREAL LONGITUDE
    // ==========================================

    private fun getLongitude(
        planet: Int,
        millis: Long
    ): Double {

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
            getJulianDay(millis),
            planet,
            SweConst.SEFLG_SWIEPH or
                    SweConst.SEFLG_SIDEREAL,
            xx,
            serr
        )

        return xx[0]
    }

    // ==========================================
    // SUN LONGITUDE
    // ==========================================

    private fun getSunLongitude(
        millis: Long
    ): Double {

        return getLongitude(
            SweConst.SE_SUN,
            millis
        )
    }

    // ==========================================
    // MOON LONGITUDE
    // ==========================================

    private fun getMoonLongitude(
        millis: Long
    ): Double {

        return getLongitude(
            SweConst.SE_MOON,
            millis
        )
    }

    // ==========================================
    // NORMALIZE
    // ==========================================

    private fun normalize(
        value: Double
    ): Double {

        var result =
            value % 360.0

        if (result < 0.0) {
            result += 360.0
        }

        return result
    }

    // ==========================================
    // TITHI INDEX
    // ==========================================

    private fun getTithiIndex(
        millis: Long
    ): Int {

        val sun =
            getSunLongitude(millis)

        val moon =
            getMoonLongitude(millis)

        val difference =
            normalize(moon - sun)

        return (
            difference / 12.0
        ).toInt()
            .coerceIn(0, 29)
    }

    // ==========================================
    // YOGA INDEX
    // ==========================================

    private fun getYogaIndex(
        millis: Long
    ): Int {

        val sun =
            getSunLongitude(millis)

        val moon =
            getMoonLongitude(millis)

        val value =
            normalize(sun + moon)

        val yogaSize =
            360.0 / 27.0

        return (
            value / yogaSize
        ).toInt()
            .coerceIn(0, 26)
    }

    // ==========================================
    // KARANA INDEX
    // ==========================================

    private fun getKaranaIndex(
        millis: Long
    ): Int {

        val sun =
            getSunLongitude(millis)

        val moon =
            getMoonLongitude(millis)

        val difference =
            normalize(moon - sun)

        return (
            difference / 6.0
        ).toInt()
            .coerceIn(0, 59)
    }

    // ==========================================
    // KARANA NAME
    // ==========================================

    private fun getKaranaName(
        index: Int
    ): String {

        return when (index) {

            0 -> "किंस्तुघ्न"

            1 -> "बव"

            2 -> "बालव"

            3 -> "कौलव"

            4 -> "तैतिल"

            5 -> "गर"

            6 -> "वणिज"

            7 -> "विष्टि"

            57 -> "शकुनि"

            58 -> "चतुष्पाद"

            59 -> "नाग"

            else -> {

                val repeating =
                    arrayOf(
                        "बव",
                        "बालव",
                        "कौलव",
                        "तैतिल",
                        "गर",
                        "वणिज",
                        "विष्टि"
                    )

                repeating[
                    (index - 1) % 7
                ]
            }
        }
    }

    // ==========================================
    // PAKSHA
    // ==========================================

    private fun getPaksha(
        tithiIndex: Int
    ): String {

        return if (tithiIndex < 15) {
            "शुक्ल पक्ष"
        } else {
            "कृष्ण पक्ष"
        }
    }

    // ==========================================
    // FIND NEXT TITHI
    // ==========================================

    private fun findNextTithi(
        now: Long,
        current: Int
    ): Pair<Int, Long> {

        var check =
            now

        repeat(3000) {

            check += 60_000L

            val next =
                getTithiIndex(check)

            if (next != current) {

                return Pair(
                    next,
                    check
                )
            }
        }

        return Pair(
            current,
            now + 24L * 60 * 60 * 1000
        )
    }

    // ==========================================
    // FIND NEXT YOGA
    // ==========================================

    private fun findNextYoga(
        now: Long,
        current: Int
    ): Pair<Int, Long> {

        var check =
            now

        repeat(3000) {

            check += 60_000L

            val next =
                getYogaIndex(check)

            if (next != current) {

                return Pair(
                    next,
                    check
                )
            }
        }

        return Pair(
            current,
            now + 24L * 60 * 60 * 1000
        )
    }

    // ==========================================
    // FIND NEXT KARANA
    // ==========================================

    private fun findNextKarana(
        now: Long,
        current: Int
    ): Pair<Int, Long> {

        var check =
            now

        repeat(1500) {

            check += 60_000L

            val next =
                getKaranaIndex(check)

            if (next != current) {

                return Pair(
                    next,
                    check
                )
            }
        }

        return Pair(
            current,
            now + 12L * 60 * 60 * 1000
        )
    }

    // ==========================================
    // FIND NEXT PAKSHA
    // ==========================================

    private fun findNextPaksha(
        now: Long,
        currentPaksha: String
    ): Pair<String, Long> {

        var check =
            now

        repeat(25000) {

            check += 60_000L

            val tithi =
                getTithiIndex(check)

            val paksha =
                getPaksha(tithi)

            if (paksha != currentPaksha) {

                return Pair(
                    paksha,
                    check
                )
            }
        }

        return Pair(
            currentPaksha,
            now + 15L * 24 * 60 * 60 * 1000
        )
    }

    // ==========================================
    // MAIN PANCHANG STATE
    // ==========================================

    fun getCurrentPanchangState(): PanchangState {

        val now =
            System.currentTimeMillis()

        // CURRENT

        val currentTithiIndex =
            getTithiIndex(now)

        val currentYogaIndex =
            getYogaIndex(now)

        val currentKaranaIndex =
            getKaranaIndex(now)

        val currentPaksha =
            getPaksha(currentTithiIndex)

        // NEXT

        val nextTithi =
            findNextTithi(
                now,
                currentTithiIndex
            )

        val nextYoga =
            findNextYoga(
                now,
                currentYogaIndex
            )

        val nextKarana =
            findNextKarana(
                now,
                currentKaranaIndex
            )

        val nextPaksha =
            findNextPaksha(
                now,
                currentPaksha
            )

        return PanchangState(

            // BASIC

            date =
                formatDate(now),

            weekday =
                getWeekday(now),


            // TITHI

            tithi =
                tithiNames[currentTithiIndex],

            tithiStartTime =
                "चालू",

            nextTithi =
                tithiNames[nextTithi.first],

            nextTithiTime =
                formatDateTime(
                    nextTithi.second
                ),

            nextTithiMillis =
                nextTithi.second,


            // YOGA

            yoga =
                yogaNames[currentYogaIndex],

            yogaStartTime =
                "चालू",

            nextYoga =
                yogaNames[nextYoga.first],

            nextYogaTime =
                formatDateTime(
                    nextYoga.second
                ),

            nextYogaMillis =
                nextYoga.second,


            // KARANA

            karana =
                getKaranaName(
                    currentKaranaIndex
                ),

            karanaStartTime =
                "चालू",

            nextKarana =
                getKaranaName(
                    nextKarana.first
                ),

            nextKaranaTime =
                formatDateTime(
                    nextKarana.second
                ),

            nextKaranaMillis =
                nextKarana.second,


            // PAKSHA

            paksha =
                currentPaksha,

            pakshaStartTime =
                "चालू",

            nextPaksha =
                nextPaksha.first,

            nextPakshaTime =
                formatDateTime(
                    nextPaksha.second
                ),

            nextPakshaMillis =
                nextPaksha.second,


            // MAS
            // पुढच्या स्टेपमध्ये astronomical calculation

            masa =
                "—",

            masaStartTime =
                "—",

            nextMasa =
                "—",

            nextMasaTime =
                "—",

            nextMasaMillis =
                0L,


            // PRAHAR
            // पुढच्या स्टेपमध्ये सूर्योदय/सूर्यास्तावर आधारित

            prahar =
                "—",

            praharStartTime =
                "—",

            nextPrahar =
                "—",

            nextPraharTime =
                "—",

            nextPraharMillis =
                0L,


            // LAGNA
            // पुढच्या स्टेपमध्ये location-based calculation

            lagna =
                "—",

            lagnaStartTime =
                "—",

            nextLagna =
                "—",

            nextLagnaTime =
                "—",

            nextLagnaMillis =
                0L
        )
    }
}
