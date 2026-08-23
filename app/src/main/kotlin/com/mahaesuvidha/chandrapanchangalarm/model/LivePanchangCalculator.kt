package com.mahaesuvidha.chandrapanchangalarm.model

import swisseph.SweConst
import swisseph.SweDate
import swisseph.SwissEph
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import kotlin.math.floor

object LivePanchangCalculator {

    private const val TITHI_SIZE = 12.0
    private const val KARANA_SIZE = 6.0
    private const val YOGA_SIZE = 360.0 / 27.0

    private val indiaTimeZone =
        TimeZone.getTimeZone("Asia/Kolkata")


    // ==========================================
    // CURRENT JULIAN DAY
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
    // PLANET LONGITUDE
    // ==========================================

    private fun getLongitude(
        planet: Int,
        jd: Double
    ): Double {

        val swe = SwissEph()

        swe.swe_set_sid_mode(
            SweConst.SE_SIDM_LAHIRI,
            0.0,
            0.0
        )

        val xx = DoubleArray(6)
        val serr = StringBuffer()

        swe.swe_calc_ut(
            jd,
            planet,
            SweConst.SEFLG_SWIEPH or
                    SweConst.SEFLG_SIDEREAL,
            xx,
            serr
        )

        return normalize(xx[0])
    }


    private fun getSunLongitude(jd: Double): Double =
        getLongitude(SweConst.SE_SUN, jd)


    private fun getMoonLongitude(jd: Double): Double =
        getLongitude(SweConst.SE_MOON, jd)


    // ==========================================
    // NORMALIZE
    // ==========================================

    private fun normalize(value: Double): Double {

        var result = value % 360.0

        if (result < 0) {
            result += 360.0
        }

        return result
    }


    // ==========================================
    // INDEX CALCULATIONS
    // ==========================================

    private fun getTithiIndex(
        sun: Double,
        moon: Double
    ): Int {

        val difference =
            normalize(moon - sun)

        return floor(
            difference / TITHI_SIZE
        ).toInt() + 1
    }


    private fun getPakshaIndex(
        tithiIndex: Int
    ): Int =
        if (tithiIndex <= 15) 0 else 1


    private fun getPaksha(
        tithiIndex: Int
    ): String =
        if (tithiIndex <= 15) {
            "शुक्ल पक्ष"
        } else {
            "कृष्ण पक्ष"
        }


    private fun getKaranaIndex(
        sun: Double,
        moon: Double
    ): Int {

        val difference =
            normalize(moon - sun)

        return floor(
            difference / KARANA_SIZE
        ).toInt()
    }


    private fun getYogaIndex(
        sun: Double,
        moon: Double
    ): Int {

        val total =
            normalize(sun + moon)

        return floor(
            total / YOGA_SIZE
        ).toInt()
    }


    // ==========================================
    // NAMES
    // ==========================================

    private fun getTithiName(index: Int): String {

        val names = arrayOf(
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
            "पौर्णिमा"
        )

        return if (index == 30) {
            "अमावस्या"
        } else {
            names[(index - 1).coerceIn(0, 14)]
        }
    }


    private fun getKaranaName(index: Int): String {

        val repeating = arrayOf(
            "बव",
            "बालव",
            "कौलव",
            "तैतिल",
            "गर",
            "वणिज",
            "विष्टि"
        )

        return when (index) {
            0 -> "किंस्तुघ्न"
            57 -> "शकुनि"
            58 -> "चतुष्पाद"
            59 -> "नाग"
            else -> repeating[(index - 1) % 7]
        }
    }


    private fun getYogaName(index: Int): String {

        val names = arrayOf(
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
            "सिद्धि",
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

        return names[index.coerceIn(0, 26)]
    }


    // ==========================================
    // INDEX AT A GIVEN MILLIS
    // ==========================================

    private fun tithiAt(millis: Long): Int {

        val jd =
            2440587.5 +
                    millis / 86400000.0

        return getTithiIndex(
            getSunLongitude(jd),
            getMoonLongitude(jd)
        )
    }


    private fun yogaAt(millis: Long): Int {

        val jd =
            2440587.5 +
                    millis / 86400000.0

        return getYogaIndex(
            getSunLongitude(jd),
            getMoonLongitude(jd)
        )
    }


    private fun karanaAt(millis: Long): Int {

        val jd =
            2440587.5 +
                    millis / 86400000.0

        return getKaranaIndex(
            getSunLongitude(jd),
            getMoonLongitude(jd)
        )
    }


    private fun pakshaAt(millis: Long): Int =
        getPakshaIndex(tithiAt(millis))


    // ==========================================
    // PREVIOUS / NEXT BOUNDARY
    //
    // First find the boundary in 30-minute steps,
    // then refine it to approximately 1 second.
    // ==========================================

    private fun findBoundary(
        now: Long,
        currentIndex: Int,
        forward: Boolean,
        maxMinutes: Int,
        getIndex: (Long) -> Int
    ): Long {

        val step = 30L * 60_000L

        var previous = now
        var current = now

        repeat(maxMinutes / 30 + 2) {

            current =
                if (forward) {
                    current + step
                } else {
                    current - step
                }

            val index =
                getIndex(current)

            if (index != currentIndex) {

                var low =
                    if (forward) previous else current

                var high =
                    if (forward) current else previous

                repeat(25) {

                    val middle =
                        low + (high - low) / 2

                    val middleIndex =
                        getIndex(middle)

                    if (forward) {

                        if (middleIndex == currentIndex) {
                            low = middle
                        } else {
                            high = middle
                        }

                    } else {

                        if (middleIndex == currentIndex) {
                            high = middle
                        } else {
                            low = middle
                        }
                    }
                }

                return if (forward) high else low
            }

            previous = current
        }

        return if (forward) {
            now + 24 * 60 * 60 * 1000L
        } else {
            now - 24 * 60 * 60 * 1000L
        }
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

        formatter.timeZone = indiaTimeZone

        return formatter.format(millis)
    }


    // ==========================================
    // MAIN PANCHANG STATE
    // ==========================================

    fun getCurrentPanchangState(): PanchangState {

        val now =
            System.currentTimeMillis()

        val jd =
            2440587.5 +
                    now / 86400000.0

        val sun =
            getSunLongitude(jd)

        val moon =
            getMoonLongitude(jd)


        // ==========================================
        // TITHI
        // ==========================================

        val tithiIndex =
            getTithiIndex(sun, moon)

        val previousTithiMillis =
            findBoundary(
                now = now,
                currentIndex = tithiIndex,
                forward = false,
                maxMinutes = 2880,
                getIndex = ::tithiAt
            )

        val nextTithiMillis =
            findBoundary(
                now = now,
                currentIndex = tithiIndex,
                forward = true,
                maxMinutes = 2880,
                getIndex = ::tithiAt
            )

        val nextTithiIndex =
            tithiAt(nextTithiMillis)


        // ==========================================
        // YOGA
        // ==========================================

        val yogaIndex =
            getYogaIndex(sun, moon)

        val previousYogaMillis =
            findBoundary(
                now = now,
                currentIndex = yogaIndex,
                forward = false,
                maxMinutes = 2880,
                getIndex = ::yogaAt
            )

        val nextYogaMillis =
            findBoundary(
                now = now,
                currentIndex = yogaIndex,
                forward = true,
                maxMinutes = 2880,
                getIndex = ::yogaAt
            )

        val nextYogaIndex =
            yogaAt(nextYogaMillis)


        // ==========================================
        // KARANA
        // ==========================================

        val karanaIndex =
            getKaranaIndex(sun, moon)

        val previousKaranaMillis =
            findBoundary(
                now = now,
                currentIndex = karanaIndex,
                forward = false,
                maxMinutes = 1440,
                getIndex = ::karanaAt
            )

        val nextKaranaMillis =
            findBoundary(
                now = now,
                currentIndex = karanaIndex,
                forward = true,
                maxMinutes = 1440,
                getIndex = ::karanaAt
            )

        val nextKaranaIndex =
            karanaAt(nextKaranaMillis)


        // ==========================================
        // PAKSHA
        // ==========================================

        val pakshaIndex =
            getPakshaIndex(tithiIndex)

        val previousPakshaMillis =
            findBoundary(
                now = now,
                currentIndex = pakshaIndex,
                forward = false,
                maxMinutes = 21600,
                getIndex = ::pakshaAt
            )

        val nextPakshaMillis =
            findBoundary(
                now = now,
                currentIndex = pakshaIndex,
                forward = true,
                maxMinutes = 21600,
                getIndex = ::pakshaAt
            )

        val nextPakshaTithiIndex =
            tithiAt(nextPakshaMillis)


        // ==========================================
        // DATE / WEEKDAY
        // ==========================================

        val calendar =
            Calendar.getInstance(
                indiaTimeZone
            )

        val dateFormatter =
            SimpleDateFormat(
                "dd-MM-yyyy",
                Locale.getDefault()
            )

        dateFormatter.timeZone =
            indiaTimeZone

        val weekdayFormatter =
            SimpleDateFormat(
                "EEEE",
                Locale("mr", "IN")
            )

        weekdayFormatter.timeZone =
            indiaTimeZone


        // ==========================================
        // RETURN
        // ==========================================

        return PanchangState(

            // BASIC

            date =
                dateFormatter.format(
                    calendar.time
                ),

            weekday =
                weekdayFormatter.format(
                    calendar.time
                ),


            // TITHI

            tithi =
                getTithiName(
                    tithiIndex
                ),

            tithiStartTime =
                formatTime(
                    previousTithiMillis
                ),

            nextTithi =
                getTithiName(
                    nextTithiIndex
                ),

            nextTithiTime =
                formatTime(
                    nextTithiMillis
                ),

            nextTithiMillis =
                nextTithiMillis,


            // YOGA

            yoga =
                getYogaName(
                    yogaIndex
                ),

            yogaStartTime =
                formatTime(
                    previousYogaMillis
                ),

            nextYoga =
                getYogaName(
                    nextYogaIndex
                ),

            nextYogaTime =
                formatTime(
                    nextYogaMillis
                ),

            nextYogaMillis =
                nextYogaMillis,


            // KARANA

            karana =
                getKaranaName(
                    karanaIndex
                ),

            karanaStartTime =
                formatTime(
                    previousKaranaMillis
                ),

            nextKarana =
                getKaranaName(
                    nextKaranaIndex
                ),

            nextKaranaTime =
                formatTime(
                    nextKaranaMillis
                ),

            nextKaranaMillis =
                nextKaranaMillis,


            // PAKSHA

            paksha =
                getPaksha(
                    tithiIndex
                ),

            pakshaStartTime =
                formatTime(
                    previousPakshaMillis
                ),

            nextPaksha =
                getPaksha(
                    nextPakshaTithiIndex
                ),

            nextPakshaTime =
                formatTime(
                    nextPakshaMillis
                ),

            nextPakshaMillis =
                nextPakshaMillis

            // MASA / PRAHAR / LAGNA
            // अजून calculator मध्ये implement केलेले नाहीत.
        )
    }
}
