package com.mahaesuvidha.chandrapanchangalarm.model

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.sin

object PanchangCalculator {

    private val INDIA_ZONE =
        ZoneId.of("Asia/Kolkata")

    private const val TITHI_SIZE =
        12.0

    private const val YOGA_SIZE =
        360.0 / 27.0

    private const val KARANA_SIZE =
        6.0

    // =========================================================
    // NAMES
    // =========================================================

    private val tithiNames =
        listOf(
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

    private val yogaNames =
        listOf(
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

    private val weekdayNames =
        listOf(
            "रविवार",
            "सोमवार",
            "मंगळवार",
            "बुधवार",
            "गुरुवार",
            "शुक्रवार",
            "शनिवार"
        )

    private val masaNames =
        arrayOf(
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


    // =========================================================
    // MAIN PANCHANG
    // =========================================================

    fun getCurrentPanchang(): PanchangState {

        val now =
            LocalDateTime.now(INDIA_ZONE)

        val sun =
            getSunLongitude(now)

        val moon =
            getMoonLongitude(now)

        val elongation =
            normalize(moon - sun)


        // -----------------------------------------------------
        // TITHI
        // -----------------------------------------------------

        val tithiIndex =
            (elongation / TITHI_SIZE)
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


        // -----------------------------------------------------
        // PAKSHA
        // -----------------------------------------------------

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


        // -----------------------------------------------------
        // YOGA
        // -----------------------------------------------------

        val yogaIndex =
            (
                normalize(
                    sun + moon
                ) / YOGA_SIZE
            )
                .toInt()
                .coerceIn(0, 26)

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


        // -----------------------------------------------------
        // KARANA
        // -----------------------------------------------------

        val karanaIndex =
            (elongation / KARANA_SIZE)
                .toInt()
                .coerceIn(0, 59)

        val karanaName =
            getKaranaName(
                karanaIndex
            )

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


        // -----------------------------------------------------
        // MAS
        // -----------------------------------------------------

        val masaIndex =
            (sun / 30.0)
                .toInt()
                .coerceIn(0, 11)

        val masaName =
            masaNames[masaIndex]

        val masaStart =
            findPreviousMasaChange(
                now,
                masaIndex
            )

        val nextMasa =
            findNextMasaChange(
                now,
                masaIndex
            )
// -----------------------------------------------------
// PRAHAR
// -----------------------------------------------------

val praharData =
    getPraharData(now)


// -----------------------------------------------------
// LAGNA
// -----------------------------------------------------

val lagnaData =
    getLagnaData(now)

        // =====================================================
        // RETURN PANCHANG STATE
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
                formatDateTime(
                    tithiStart
                ),

            nextTithi =
                nextTithi.first,

            nextTithiTime =
                formatDateTime(
                    nextTithi.second
                ),

            nextTithiMillis =
                toMillis(
                    nextTithi.second
                ),


            // YOGA

            yoga =
                yogaName,

            yogaStartTime =
                formatDateTime(
                    yogaStart
                ),

            nextYoga =
                nextYoga.first,

            nextYogaTime =
                formatDateTime(
                    nextYoga.second
                ),

            nextYogaMillis =
                toMillis(
                    nextYoga.second
                ),


            // KARANA

            karana =
                karanaName,

            karanaStartTime =
                formatDateTime(
                    karanaStart
                ),

            nextKarana =
                nextKarana.first,

            nextKaranaTime =
                formatDateTime(
                    nextKarana.second
                ),

            nextKaranaMillis =
                toMillis(
                    nextKarana.second
                ),


            // PAKSHA

            paksha =
                paksha,

            pakshaStartTime =
                formatDateTime(
                    pakshaStart
                ),

            nextPaksha =
                nextPaksha.first,

            nextPakshaTime =
                formatDateTime(
                    nextPaksha.second
                ),

            nextPakshaMillis =
                toMillis(
                    nextPaksha.second
                ),


            // MAS

            masa =
                masaName,

            nextMasa =
                nextMasa.first,

            masaStartTime =
                formatDateTime(
                    masaStart
                ),

            nextMasaTime =
                formatDateTime(
                    nextMasa.second
                ),

            nextMasaMillis =
                toMillis(
                    nextMasa.second
                )
        )
    }


    // =========================================================
    // PREVIOUS TITHI
    // =========================================================

    private fun findPreviousTithiChange(
        now: LocalDateTime,
        current: Int
    ): LocalDateTime {

        var check = now

        repeat(4320) {

            check =
                check.minusMinutes(1)

            val sun =
                getSunLongitude(check)

            val moon =
                getMoonLongitude(check)

            val elongation =
                normalize(moon - sun)

            val previous =
                (elongation / TITHI_SIZE)
                    .toInt()
                    .coerceIn(0, 29)

            if (previous != current) {
                return check.plusMinutes(1)
            }
        }

        return now
    }


    // =========================================================
    // NEXT TITHI
    // =========================================================

    private fun findNextTithiChange(
        now: LocalDateTime,
        current: Int
    ): Pair<String, LocalDateTime> {

        var check = now

        repeat(4320) {

            check =
                check.plusMinutes(1)

            val sun =
                getSunLongitude(check)

            val moon =
                getMoonLongitude(check)

            val elongation =
                normalize(moon - sun)

            val next =
                (elongation / TITHI_SIZE)
                    .toInt()
                    .coerceIn(0, 29)

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
    // PREVIOUS YOGA
    // =========================================================

    private fun findPreviousYogaChange(
        now: LocalDateTime,
        current: Int
    ): LocalDateTime {

        var check = now

        repeat(4320) {

            check =
                check.minusMinutes(1)

            val sun =
                getSunLongitude(check)

            val moon =
                getMoonLongitude(check)

            val yoga =
                (
                    normalize(
                        sun + moon
                    ) / YOGA_SIZE
                )
                    .toInt()
                    .coerceIn(0, 26)

            if (yoga != current) {
                return check.plusMinutes(1)
            }
        }

        return now
    }


    // =========================================================
    // NEXT YOGA
    // =========================================================

    private fun findNextYogaChange(
        now: LocalDateTime,
        current: Int
    ): Pair<String, LocalDateTime> {

        var check = now

        repeat(4320) {

            check =
                check.plusMinutes(1)

            val sun =
                getSunLongitude(check)

            val moon =
                getMoonLongitude(check)

            val yoga =
                (
                    normalize(
                        sun + moon
                    ) / YOGA_SIZE
                )
                    .toInt()
                    .coerceIn(0, 26)

            if (yoga != current) {

                return Pair(
                    "${yogaNames[current]} → ${yogaNames[yoga]}",
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
    // PREVIOUS KARANA
    // =========================================================

    private fun findPreviousKaranaChange(
        now: LocalDateTime,
        current: Int
    ): LocalDateTime {

        var check = now

        repeat(4320) {

            check =
                check.minusMinutes(1)

            val sun =
                getSunLongitude(check)

            val moon =
                getMoonLongitude(check)

            val elongation =
                normalize(moon - sun)

            val karana =
                (elongation / KARANA_SIZE)
                    .toInt()
                    .coerceIn(0, 59)

            if (karana != current) {
                return check.plusMinutes(1)
            }
        }

        return now
    }


    // =========================================================
    // NEXT KARANA
    // =========================================================

    private fun findNextKaranaChange(
        now: LocalDateTime,
        current: Int
    ): Pair<String, LocalDateTime> {

        var check = now

        repeat(4320) {

            check =
                check.plusMinutes(1)

            val sun =
                getSunLongitude(check)

            val moon =
                getMoonLongitude(check)

            val elongation =
                normalize(moon - sun)

            val karana =
                (elongation / KARANA_SIZE)
                    .toInt()
                    .coerceIn(0, 59)

            if (karana != current) {

                return Pair(
                    "${getKaranaName(current)} → ${getKaranaName(karana)}",
                    check
                )
            }
        }

        return Pair(
            "पुढील करण शोधत आहे",
            now.plusDays(3)
        )
    }


    // =========================================================
    // PREVIOUS MAS
    // =========================================================

    private fun findPreviousMasaChange(
        now: LocalDateTime,
        current: Int
    ): LocalDateTime {

        var check = now

        repeat(50400) {

            check =
                check.minusMinutes(1)

            val sun =
                getSunLongitude(check)

            val masa =
                (sun / 30.0)
                    .toInt()
                    .coerceIn(0, 11)

            if (masa != current) {
                return check.plusMinutes(1)
            }
        }

        return now
    }


    // =========================================================
    // NEXT MAS
    // =========================================================

    private fun findNextMasaChange(
        now: LocalDateTime,
        current: Int
    ): Pair<String, LocalDateTime> {

        var check = now

        repeat(50400) {

            check =
                check.plusMinutes(1)

            val sun =
                getSunLongitude(check)

            val masa =
                (sun / 30.0)
                    .toInt()
                    .coerceIn(0, 11)

            if (masa != current) {

                return Pair(
                    "${masaNames[current]} → ${masaNames[masa]}",
                    check
                )
            }
        }

        return Pair(
            "पुढील मास शोधत आहे",
            now.plusDays(35)
        )
    }


    // =========================================================
    // PREVIOUS PAKSHA
    // =========================================================

    private fun findPreviousPakshaChange(
        now: LocalDateTime,
        current: String
    ): LocalDateTime {

        var check = now

        repeat(50000) {

            check =
                check.minusMinutes(1)

            val sun =
                getSunLongitude(check)

            val moon =
                getMoonLongitude(check)

            val elongation =
                normalize(moon - sun)

            val tithi =
                (elongation / TITHI_SIZE)
                    .toInt()
                    .coerceIn(0, 29)

            val paksha =
                if (tithi < 15) {
                    "शुक्ल पक्ष"
                } else {
                    "कृष्ण पक्ष"
                }

            if (paksha != current) {
                return check.plusMinutes(1)
            }
        }

        return now
    }


    // =========================================================
    // NEXT PAKSHA
    // =========================================================

    private fun findNextPakshaChange(
        now: LocalDateTime,
        current: String
    ): Pair<String, LocalDateTime> {

        var check = now

        repeat(50000) {

            check =
                check.plusMinutes(1)

            val sun =
                getSunLongitude(check)

            val moon =
                getMoonLongitude(check)

            val elongation =
                normalize(moon - sun)

            val tithi =
                (elongation / TITHI_SIZE)
                    .toInt()
                    .coerceIn(0, 29)

            val paksha =
                if (tithi < 15) {
                    "शुक्ल पक्ष"
                } else {
                    "कृष्ण पक्ष"
                }

            if (paksha != current) {

                return Pair(
                    "$current → $paksha",
                    check
                )
            }
        }

        return Pair(
            "पुढील पक्ष शोधत आहे",
            now.plusDays(20)
        )
    }


    // =========================================================
    // TITHI NAME
    // =========================================================

    private fun getTithiName(
        index: Int
    ): String {

        val safe =
            index.coerceIn(0, 29)

        return if (safe < 15) {
            tithiNames[safe]
        } else {
            when (safe) {
                15 -> "प्रतिपदा"
                16 -> "द्वितीया"
                17 -> "तृतीया"
                18 -> "चतुर्थी"
                19 -> "पंचमी"
                20 -> "षष्ठी"
                21 -> "सप्तमी"
                22 -> "अष्टमी"
                23 -> "नवमी"
                24 -> "दशमी"
                25 -> "एकादशी"
                26 -> "द्वादशी"
                27 -> "त्रयोदशी"
                28 -> "चतुर्दशी"
                else -> "अमावस्या"
            }
        }
    }


    // =========================================================
    // KARANA NAME
    // =========================================================

    private fun getKaranaName(
        index: Int
    ): String {

        val names =
            listOf(
                "बव",
                "बालव",
                "कौलव",
                "तैतिल",
                "गर",
                "वणिज",
                "विष्टि"
            )

        if (index == 0) {
            return "किंस्तुघ्न"
        }

        if (index >= 57) {
            return when (index) {
                57 -> "शकुनि"
                58 -> "चतुष्पाद"
                59 -> "नाग"
                else -> "—"
            }
        }

        return names[
            (index - 1) % 7
        ]
    }


    // =========================================================
    // SUN LONGITUDE
    // =========================================================

    private fun getSunLongitude(
        dateTime: LocalDateTime
    ): Double {

        val jd =
            getJulianDay(dateTime)

        val d =
            jd - 2451545.0

        val meanLongitude =
            normalize(
                280.46646 +
                        0.98564736 * d
            )

        val meanAnomaly =
            normalize(
                357.52911 +
                        0.98560028 * d
            )

        val equationOfCenter =
            1.914602 *
                    sin(
                        Math.toRadians(
                            meanAnomaly
                        )
                    ) +
                    0.019993 *
                    sin(
                        Math.toRadians(
                            2.0 * meanAnomaly
                        )
                    ) +
                    0.000289 *
                    sin(
                        Math.toRadians(
                            3.0 * meanAnomaly
                        )
                    )

        val tropical =
            normalize(
                meanLongitude +
                        equationOfCenter
            )

        val ayanamsa =
            23.85675 +
                    (
                        (jd - 2451545.0) /
                                36525.0
                        ) *
                    1.396971

        return normalize(
            tropical - ayanamsa
        )
    }


    // =========================================================
    // MOON LONGITUDE
    // =========================================================

    private fun getMoonLongitude(
        dateTime: LocalDateTime
    ): Double {

        val jd =
            getJulianDay(dateTime)

        val d =
            jd - 2451545.0

        val l =
            normalize(
                218.316 +
                        13.176396 * d
            )

        val m =
            normalize(
                134.963 +
                        13.064993 * d
            )

        val f =
            normalize(
                93.272 +
                        13.229350 * d
            )

        val moonLongitude =
            l +
                    6.289 *
                    sin(
                        Math.toRadians(m)
                    ) +
                    1.274 *
                    sin(
                        Math.toRadians(
                            2.0 * (l - 280.466) - m
                        )
                    ) +
                    0.658 *
                    sin(
                        Math.toRadians(
                            2.0 * (l - 280.466)
                        )
                    ) +
                    0.214 *
                    sin(
                        Math.toRadians(
                            2.0 * m
                        )
                    ) -
                    0.186 *
                    sin(
                        Math.toRadians(
                            357.529 +
                                    0.98560028 * d
                        )
                    ) -
                    0.114 *
                    sin(
                        Math.toRadians(
                            2.0 * f
                        )
                    )

        val ayanamsa =
            23.85675 +
                    (
                        (jd - 2451545.0) /
                                36525.0
                        ) *
                    1.396971

        return normalize(
            moonLongitude - ayanamsa
        )
    }


    // =========================================================
    // JULIAN DAY
    // =========================================================

    private fun getJulianDay(
        dateTime: LocalDateTime
    ): Double {

        val instant =
            dateTime
                .atZone(INDIA_ZONE)
                .toInstant()

        return (
            instant.epochSecond /
                    86400.0
            ) +
                2440587.5
    }


    // =========================================================
    // TIME
    // =========================================================

    private fun toMillis(
        dateTime: LocalDateTime
    ): Long {

        return dateTime
            .atZone(INDIA_ZONE)
            .toInstant()
            .toEpochMilli()
    }

    private fun formatDate(
        dateTime: LocalDateTime
    ): String {

        return dateTime.format(
            DateTimeFormatter.ofPattern(
                "dd-MM-yyyy"
            )
        )
    }

    private fun formatDateTime(
        dateTime: LocalDateTime
    ): String {

        return dateTime.format(
            DateTimeFormatter.ofPattern(
                "dd-MM-yyyy HH:mm"
            )
        )
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
// PRAHAR DATA
// =========================================================

private data class PraharData(
    val current: String,
    val next: String,
    val nextTime: LocalDateTime
)

private fun getPraharData(
    now: LocalDateTime
): PraharData {

    val hour =
        now.hour

    val minute =
        now.minute

    val totalMinutes =
        hour * 60 + minute


    val praharIndex =
        when {

            totalMinutes >= 360 &&
                    totalMinutes < 540 -> 0

            totalMinutes >= 540 &&
                    totalMinutes < 720 -> 1

            totalMinutes >= 720 &&
                    totalMinutes < 900 -> 2

            totalMinutes >= 900 &&
                    totalMinutes < 1080 -> 3

            totalMinutes >= 1080 &&
                    totalMinutes < 1260 -> 4

            totalMinutes >= 1260 ||
                    totalMinutes < 0 -> 5

            totalMinutes >= 0 &&
                    totalMinutes < 180 -> 6

            else -> 7
        }


    val names =
        listOf(
            "दिवसाचा पहिला प्रहर",
            "दिवसाचा दुसरा प्रहर",
            "दिवसाचा तिसरा प्रहर",
            "दिवसाचा चौथा प्रहर",
            "रात्रीचा पहिला प्रहर",
            "रात्रीचा दुसरा प्रहर",
            "रात्रीचा तिसरा प्रहर",
            "रात्रीचा चौथा प्रहर"
        )


    val boundaries =
        listOf(
            360,
            540,
            720,
            900,
            1080,
            1260,
            1440,
            180,
            360
        )


    val nextIndex =
        (praharIndex + 1) % 8

    val nextBoundary =
        boundaries[praharIndex + 1]


    var nextTime =
        now
            .withSecond(0)
            .withNano(0)


    if (nextBoundary == 1440) {

        nextTime =
            nextTime
                .toLocalDate()
                .plusDays(1)
                .atStartOfDay()

    } else if (nextBoundary <= totalMinutes) {

        nextTime =
            nextTime
                .toLocalDate()
                .plusDays(1)
                .atTime(
                    nextBoundary / 60,
                    nextBoundary % 60
                )

    } else {

        nextTime =
            nextTime
                .toLocalDate()
                .atTime(
                    nextBoundary / 60,
                    nextBoundary % 60
                )
    }


    return PraharData(
        current =
            names[praharIndex],

        next =
            names[nextIndex],

        nextTime =
            nextTime
    )
}


// =========================================================
// LAGNA DATA
// =========================================================

private data class LagnaData(
    val current: String,
    val next: String,
    val nextTime: LocalDateTime
)

private fun getLagnaData(
    now: LocalDateTime
): LagnaData {

    val sun =
        getSunLongitude(now)

    val moon =
        getMoonLongitude(now)


    /*
     * Approximate Lagna calculation.
     *
     * प्रत्येक 30 अंशाला एक लग्न.
     * पुढील version मध्ये स्थानानुसार
     * अधिक precise ascendant calculation
     * जोडता येईल.
     */

    val localMinutes =
        now.hour * 60 +
                now.minute

    val rotation =
        (
            localMinutes /
                    1440.0
        ) * 360.0


    val ascendantLongitude =
        normalize(
            sun +
                    rotation
        )


    val lagnaIndex =
        (
            ascendantLongitude / 30.0
        )
            .toInt()
            .coerceIn(
                0,
                11
            )


    val lagnaNames =
        listOf(
            "मेष लग्न",
            "वृषभ लग्न",
            "मिथुन लग्न",
            "कर्क लग्न",
            "सिंह लग्न",
            "कन्या लग्न",
            "तुळ लग्न",
            "वृश्चिक लग्न",
            "धनु लग्न",
            "मकर लग्न",
            "कुंभ लग्न",
            "मीन लग्न"
        )


    val nextIndex =
        (lagnaIndex + 1) % 12


    val currentDegrees =
        ascendantLongitude % 30.0

    val remainingDegrees =
        30.0 -
                currentDegrees


    val minutesRemaining =
        (
            remainingDegrees /
                    360.0 *
                    1440.0
        )
            .toLong()
            .coerceAtLeast(1L)


    val nextTime =
        now
            .withSecond(0)
            .withNano(0)
            .plusMinutes(
                minutesRemaining
            )


    return LagnaData(

        current =
            lagnaNames[lagnaIndex],

        next =
            lagnaNames[nextIndex],

        nextTime =
            nextTime
    )
}
}
