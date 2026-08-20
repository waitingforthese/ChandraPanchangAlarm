package com.mahaesuvidha.chandrapanchangalarm.model

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.floor
import kotlin.math.sin

object PanchangCalculator {

    private val INDIA_ZONE =
        ZoneId.of("Asia/Kolkata")

    private const val TITHI_SIZE = 12.0
    private const val YOGA_SIZE = 360.0 / 27.0
    private const val KARANA_SIZE = 6.0

    private val tithiNames = listOf(
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
        "पूर्णिमा"
    )

    private val yogaNames = listOf(
        "विष्कंभ",
        "प्रीती",
        "आयुष्मान",
        "सौभाग्य",
        "शोभन",
        "अतिगंड",
        "सुकर्म",
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

    private val weekdayNames = listOf(
        "रविवार",
        "सोमवार",
        "मंगळवार",
        "बुधवार",
        "गुरुवार",
        "शुक्रवार",
        "शनिवार"
    )

    private val masaNames = listOf(
        "चैत्र",
        "वैशाख",
        "ज्येष्ठ",
        "आषाढ",
        "श्रावण",
        "भाद्रपद",
        "आश्विन",
        "कार्तिक",
        "मार्गशीर्ष",
        "पौष",
        "माघ",
        "फाल्गुन"
    )

    private val lagnaNames = listOf(
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

    fun getCurrentPanchang(): PanchangState {

        val now =
            LocalDateTime.now(INDIA_ZONE)

        val sun =
            getSunLongitude(now)

        val moon =
            getMoonLongitude(now)

        val elongation =
            normalize(moon - sun)


        // =====================================================
        // TITHI
        // =====================================================

        val tithiIndex =
            floor(elongation / TITHI_SIZE)
                .toInt()
                .coerceIn(0, 29)

        val tithiName =
            getTithiName(tithiIndex)

        val tithiStart =
            findPreviousTithiChange(
                now,
                tithiIndex
            )

        val nextTithi =
            findNextTithiChange(
                now,
                tithiIndex
            )


        // =====================================================
        // PAKSHA
        // =====================================================

        val paksha =
            if (tithiIndex < 15) {
                "शुक्ल पक्ष"
            } else {
                "कृष्ण पक्ष"
            }

        val pakshaStart =
            findPreviousPakshaChange(
                now,
                paksha
            )

        val nextPaksha =
            findNextPakshaChange(
                now,
                paksha
            )


        // =====================================================
        // YOGA
        // =====================================================

        val yogaIndex =
            getYogaIndex(now)

        val yogaName =
            yogaNames[yogaIndex]

        val yogaStart =
            findPreviousYogaChange(
                now,
                yogaIndex
            )

        val nextYoga =
            findNextYogaChange(
                now,
                yogaIndex
            )


        // =====================================================
        // KARANA
        // =====================================================

        val karanaIndex =
            floor(elongation / KARANA_SIZE)
                .toInt()
                .coerceIn(0, 59)

        val karanaName =
            getKaranaName(karanaIndex)

        val karanaStart =
            findPreviousKaranaChange(
                now,
                karanaIndex
            )

        val nextKarana =
            findNextKaranaChange(
                now,
                karanaIndex
            )


        // =====================================================
        // MAS
        // =====================================================

        val masaData =
            getMasaData(now)


        // =====================================================
        // PRAHAR
        // =====================================================

        val praharData =
            getPraharData(now)


        // =====================================================
        // LAGNA
        // =====================================================

        val lagnaData =
            getLagnaData(now)


        // =====================================================
        // RETURN
        // =====================================================

        return PanchangState(

            date =
                formatDate(now),

            weekday =
                weekdayNames[
                    now.dayOfWeek.value % 7
                ],


            // TITHI

            tithi =
                tithiName,

            tithiStartTime =
                formatDateTime(tithiStart),

            nextTithi =
                nextTithi.first,

            nextTithiTime =
                formatDateTime(nextTithi.second),

            nextTithiMillis =
                toMillis(nextTithi.second),


            // YOGA

            yoga =
                yogaName,

            yogaStartTime =
                formatDateTime(yogaStart),

            nextYoga =
                nextYoga.first,

            nextYogaTime =
                formatDateTime(nextYoga.second),

            nextYogaMillis =
                toMillis(nextYoga.second),


            // KARANA

            karana =
                karanaName,

            karanaStartTime =
                formatDateTime(karanaStart),

            nextKarana =
                nextKarana.first,

            nextKaranaTime =
                formatDateTime(nextKarana.second),

            nextKaranaMillis =
                toMillis(nextKarana.second),


            // PAKSHA

            paksha =
                paksha,

            pakshaStartTime =
                formatDateTime(pakshaStart),

            nextPaksha =
                nextPaksha.first,

            nextPakshaTime =
                formatDateTime(nextPaksha.second),

            nextPakshaMillis =
                toMillis(nextPaksha.second),


            // MAS

            masa =
                masaData.first,

            nextMasa =
                masaData.second,

            masaStartTime =
                formatDateTime(masaData.third),

            nextMasaTime =
                formatDateTime(masaData.fourth),

            nextMasaMillis =
                toMillis(masaData.fourth),


            // PRAHAR

            prahar =
                praharData.first,

            nextPrahar =
                praharData.second,

            nextPraharTime =
                formatDateTime(praharData.third),

            nextPraharMillis =
                toMillis(praharData.third),


            // LAGNA

            lagna =
                lagnaData.first,

            nextLagna =
                lagnaData.second,

            nextLagnaTime =
                formatDateTime(lagnaData.third),

            nextLagnaMillis =
                toMillis(lagnaData.third)
        )
    }


    // =========================================================
    // TITHI
    // =========================================================

    private fun getTithiIndex(
        time: LocalDateTime
    ): Int {

        val sun =
            getSunLongitude(time)

        val moon =
            getMoonLongitude(time)

        return floor(
            normalize(moon - sun) /
                    TITHI_SIZE
        )
            .toInt()
            .coerceIn(0, 29)
    }

    private fun getTithiName(
        index: Int
    ): String {

        val base =
            tithiNames[index % 15]

        return if (index < 15) {
            base
        } else {
            when (index % 15) {
                14 -> "अमावस्या"
                else -> base
            }
        }
    }

    private fun findPreviousTithiChange(
        now: LocalDateTime,
        current: Int
    ): LocalDateTime {

        var check = now

        repeat(4320) {

            check =
                check.minusMinutes(1)

            if (
                getTithiIndex(check) != current
            ) {
                return check.plusMinutes(1)
            }
        }

        return now
    }

    private fun findNextTithiChange(
        now: LocalDateTime,
        current: Int
    ): Pair<String, LocalDateTime> {

        var check = now

        repeat(4320) {

            check =
                check.plusMinutes(1)

            val next =
                getTithiIndex(check)

            if (next != current) {

                return Pair(
                    "${getTithiName(current)} → ${getTithiName(next)}",
                    check
                )
            }
        }

        return Pair(
            "पुढील तिथी शोधत आहे",
            now.plusDays(3)
        )
    }


    // =========================================================
    // YOGA
    // =========================================================

    private fun getYogaIndex(
        time: LocalDateTime
    ): Int {

        val sun =
            getSunLongitude(time)

        val moon =
            getMoonLongitude(time)

        return floor(
            normalize(sun + moon) /
                    YOGA_SIZE
        )
            .toInt()
            .coerceIn(0, 26)
    }

    private fun findPreviousYogaChange(
        now: LocalDateTime,
        current: Int
    ): LocalDateTime {

        var check = now

        repeat(4320) {

            check =
                check.minusMinutes(1)

            if (
                getYogaIndex(check) != current
            ) {
                return check.plusMinutes(1)
            }
        }

        return now
    }

    private fun findNextYogaChange(
        now: LocalDateTime,
        current: Int
    ): Pair<String, LocalDateTime> {

        var check = now

        repeat(4320) {

            check =
                check.plusMinutes(1)

            val next =
                getYogaIndex(check)

            if (next != current) {

                return Pair(
                    "${yogaNames[current]} → ${yogaNames[next]}",
                    check
                )
            }
        }

        return Pair(
            "पुढील योग शोधत आहे",
            now.plusDays(3)
        )
    }


    // =========================================================
    // KARANA
    // =========================================================

    private fun getKaranaIndex(
        time: LocalDateTime
    ): Int {

        val sun =
            getSunLongitude(time)

        val moon =
            getMoonLongitude(time)

        return floor(
            normalize(moon - sun) /
                    KARANA_SIZE
        )
            .toInt()
            .coerceIn(0, 59)
    }

    private fun getKaranaName(
        index: Int
    ): String {

        return when (index) {

            0 -> "किंस्तुघ्न"

            1 -> "बव"

            58 -> "चतुष्पाद"

            59 -> "नाग"

            else -> {

                val names =
                    listOf(
                        "बालव",
                        "कौलव",
                        "तैतिल",
                        "गर",
                        "वणिज",
                        "विष्टि",
                        "बव"
                    )

                names[(index - 2) % names.size]
            }
        }
    }

    private fun findPreviousKaranaChange(
        now: LocalDateTime,
        current: Int
    ): LocalDateTime {

        var check = now

        repeat(4320) {

            check =
                check.minusMinutes(1)

            if (
                getKaranaIndex(check) != current
            ) {
                return check.plusMinutes(1)
            }
        }

        return now
    }

    private fun findNextKaranaChange(
        now: LocalDateTime,
        current: Int
    ): Pair<String, LocalDateTime> {

        var check = now

        repeat(4320) {

            check =
                check.plusMinutes(1)

            val next =
                getKaranaIndex(check)

            if (next != current) {

                return Pair(
                    "${getKaranaName(current)} → ${getKaranaName(next)}",
                    check
                )
            }
        }

        return Pair(
            "पुढील करण शोधत आहे",
            now.plusDays(2)
        )
    }


    // =========================================================
    // PAKSHA
    // =========================================================

    private fun getPaksha(
        time: LocalDateTime
    ): String {

        return if (
            getTithiIndex(time) < 15
        ) {
            "शुक्ल पक्ष"
        } else {
            "कृष्ण पक्ष"
        }
    }

    private fun findPreviousPakshaChange(
        now: LocalDateTime,
        current: String
    ): LocalDateTime {

        var check = now

        repeat(50000) {

            check =
                check.minusMinutes(5)

            if (
                getPaksha(check) != current
            ) {
                return check.plusMinutes(5)
            }
        }

        return now
    }

    private fun findNextPakshaChange(
        now: LocalDateTime,
        current: String
    ): Pair<String, LocalDateTime> {

        var check = now

        repeat(50000) {

            check =
                check.plusMinutes(5)

            val next =
                getPaksha(check)

            if (next != current) {

                return Pair(
                    "$current → $next",
                    check
                )
            }
        }

        return Pair(
            "पुढील पक्ष शोधत आहे",
            now.plusDays(16)
        )
    }


    // =========================================================
    // MAS
    // =========================================================

    private fun getMasaData(
        now: LocalDateTime
    ): MasaData {

        val currentIndex =
            getMasaIndex(now)

        val currentName =
            masaNames[currentIndex]

        val start =
            LocalDateTime.of(
                now.year,
                now.month,
                1,
                0,
                0
            )

        val nextStart =
            start.plusMonths(1)

        val nextIndex =
            (currentIndex + 1) % 12

        return MasaData(
            currentName,
            masaNames[nextIndex],
            start,
            nextStart
        )
    }

    private fun getMasaIndex(
        time: LocalDateTime
    ): Int {

        return (time.monthValue + 9) % 12
    }


    // =========================================================
    // PRAHAR
    // =========================================================

    private fun getPraharData(
        now: LocalDateTime
    ): Triple<String, String, LocalDateTime> {

        val hour =
            now.hour

        val currentIndex =
            hour / 3

        val praharNames = listOf(
            "पहिला प्रहर",
            "दुसरा प्रहर",
            "तिसरा प्रहर",
            "चौथा प्रहर",
            "पाचवा प्रहर",
            "सहावा प्रहर",
            "सातवा प्रहर",
            "आठवा प्रहर"
        )

        val nextIndex =
            (currentIndex + 1) % 8

        val startHour =
            currentIndex * 3

        val nextTime =
            now
                .withHour(startHour)
                .withMinute(0)
                .withSecond(0)
                .withNano(0)
                .plusHours(3)

        return Triple(
            praharNames[currentIndex],
            praharNames[nextIndex],
            nextTime
        )
    }


    // =========================================================
    // LAGNA
    // =========================================================

    private fun getLagnaData(
        now: LocalDateTime
    ): Triple<String, String, LocalDateTime> {

        val sun =
            getSunLongitude(now)

        val moon =
            getMoonLongitude(now)

        val approximateLongitude =
            normalize(
                moon +
                        sun / 12.0 +
                        now.hour * 15.0
            )

        val currentIndex =
            floor(
                approximateLongitude / 30.0
            )
                .toInt()
                .coerceIn(0, 11)

        val nextIndex =
            (currentIndex + 1) % 12

        val nextTime =
            now
                .withMinute(0)
                .withSecond(0)
                .withNano(0)
                .plusHours(2)

        return Triple(
            lagnaNames[currentIndex],
            lagnaNames[nextIndex],
            nextTime
        )
    }


    // =========================================================
    // SUN LONGITUDE
    // =========================================================

    private fun getSunLongitude(
        time: LocalDateTime
    ): Double {

        val days =
            getJulianDay(time) -
                    2451545.0

        val meanLongitude =
            normalize(
                280.46646 +
                        0.98564736 * days
            )

        val meanAnomaly =
            Math.toRadians(
                normalize(
                    357.52911 +
                            0.98560028 * days
                )
            )

        return normalize(
            meanLongitude +
                    1.915 * sin(meanAnomaly) +
                    0.020 *
                    sin(2 * meanAnomaly)
        )
    }


    // =========================================================
    // MOON LONGITUDE
    // =========================================================

    private fun getMoonLongitude(
        time: LocalDateTime
    ): Double {

        val days =
            getJulianDay(time) -
                    2451545.0

        val l0 =
            normalize(
                218.316 +
                        13.176396 * days
            )

        val mMoon =
            Math.toRadians(
                normalize(
                    134.963 +
                            13.064993 * days
                )
            )

        val mSun =
            Math.toRadians(
                normalize(
                    357.529 +
                            0.98560028 * days
                )
            )

        val d =
            Math.toRadians(
                normalize(
                    297.850 +
                            12.190749 * days
                )
            )

        return normalize(
            l0 +
                    6.289 *
                    sin(mMoon) +
                    1.274 *
                    sin(2 * d - mMoon) +
                    0.658 *
                    sin(2 * d) +
                    0.214 *
                    sin(2 * mMoon) -
                    0.186 *
                    sin(mSun)
        )
    }


    // =========================================================
    // JULIAN DAY
    // =========================================================

    private fun getJulianDay(
        time: LocalDateTime
    ): Double {

        val instant =
            time
                .atZone(INDIA_ZONE)
                .toInstant()

        return 2440587.5 +
                instant
                    .toEpochMilli()
                    .toDouble() /
                86400000.0
    }


    // =========================================================
    // NORMALIZE
    // =========================================================

    private fun normalize(
        value: Double
    ): Double {

        var result =
            value % 360.0

        if (result < 0) {
            result += 360.0
        }

        return result
    }


    // =========================================================
    // FORMAT
    // =========================================================

    private fun formatDate(
        time: LocalDateTime
    ): String {

        return time.format(
            DateTimeFormatter.ofPattern(
                "dd-MM-yyyy"
            )
        )
    }

    private fun formatDateTime(
        time: LocalDateTime
    ): String {

        return time.format(
            DateTimeFormatter.ofPattern(
                "dd-MM-yyyy HH:mm"
            )
        )
    }


    // =========================================================
    // MILLIS
    // =========================================================

    private fun toMillis(
        time: LocalDateTime
    ): Long {

        return time
            .atZone(INDIA_ZONE)
            .toInstant()
            .toEpochMilli()
    }


    // =========================================================
    // HELPER DATA
    // =========================================================

    private data class MasaData(

        val first: String,

        val second: String,

        val third: LocalDateTime,

        val fourth: LocalDateTime
    )
}
