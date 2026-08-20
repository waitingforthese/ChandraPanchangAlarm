package com.mahaesuvidha.chandrapanchangalarm.model

import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.floor

object PanchangCalculator {

    // =========================================================
    // CONSTANTS
    // =========================================================

    private const val TITHI_SIZE = 12.0
    private const val YOGA_SIZE = 13.333333333333334
    private const val KARANA_SIZE = 6.0

    private val zoneId =
        ZoneId.systemDefault()

    // =========================================================
    // NAMES
    // =========================================================

    private val weekdayNames =
        arrayOf(
            "रविवार",
            "सोमवार",
            "मंगळवार",
            "बुधवार",
            "गुरुवार",
            "शुक्रवार",
            "शनिवार"
        )

    private val tithiNames =
        arrayOf(
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
            "अमावास्या"
        )

    private val yogaNames =
        arrayOf(
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

    private val karanaCycle =
        arrayOf(
            "बव",
            "बालव",
            "कौलव",
            "तैतिल",
            "गर",
            "वणिज",
            "विष्टि"
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

    private val praharNames =
        arrayOf(
            "प्रथम प्रहर",
            "द्वितीय प्रहर",
            "तृतीय प्रहर",
            "चतुर्थ प्रहर",
            "पंचम प्रहर",
            "षष्ठ प्रहर",
            "सप्तम प्रहर",
            "अष्टम प्रहर"
        )

    private val lagnaNames =
        arrayOf(
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

    // =========================================================
    // MAIN FUNCTION
    // =========================================================
object PanchangCalculator {

    fun getCurrentPanchangState(): PanchangState {

        val now =
            LocalDateTime.now()

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
            floor(
                normalize(sun + moon) /
                        YOGA_SIZE
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
            floor(
                elongation /
                        KARANA_SIZE
            )
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
            floor(sun / 30.0)
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

            masaStartTime =
                formatDateTime(
                    masaStart
                ),

            nextMasa =
                nextMasa.first,

            nextMasaTime =
                formatDateTime(
                    nextMasa.second
                ),

            nextMasaMillis =
                toMillis(
                    nextMasa.second
                ),

            // PRAHAR

            prahar =
                praharData.name,

            praharStartTime =
                formatDateTime(
                    praharData.start
                ),

            nextPrahar =
                praharData.nextName,

            nextPraharTime =
                formatDateTime(
                    praharData.next
                ),

            nextPraharMillis =
                toMillis(
                    praharData.next
                ),

            // LAGNA

            lagna =
                lagnaData.name,

            lagnaStartTime =
                formatDateTime(
                    lagnaData.start
                ),

            nextLagna =
                lagnaData.nextName,

            nextLagnaTime =
                formatDateTime(
                    lagnaData.next
                ),

            nextLagnaMillis =
                toMillis(
                    lagnaData.next
                )
        )
    }

    // =========================================================
    // TITHI
    // =========================================================

    private fun getTithiName(
        index: Int
    ): String {

        return tithiNames[
            index.coerceIn(0, 29)
        ]
    }

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
                    "${getTithiName(current)} → " +
                            getTithiName(next),
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
                    "${yogaNames[current]} → " +
                            yogaNames[next],
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

            0 ->
                "किंस्तुघ्न"

            57 ->
                "शकुनि"

            58 ->
                "चतुष्पाद"

            59 ->
                "नाग"

            else ->
                karanaCycle[
                    ((index - 1) % 7 + 7) % 7
                ]
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
                    "${getKaranaName(current)} → " +
                            getKaranaName(next),
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

        repeat(25000) {

            check =
                check.minusMinutes(1)

            if (
                getPaksha(check) != current
            ) {
                return check.plusMinutes(1)
            }
        }

        return now
    }

    private fun findNextPakshaChange(
        now: LocalDateTime,
        current: String
    ): Pair<String, LocalDateTime> {

        var check = now

        repeat(25000) {

            check =
                check.plusMinutes(1)

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

    private fun getMasaIndex(
        time: LocalDateTime
    ): Int {

        return floor(
            getSunLongitude(time) /
                    30.0
        )
            .toInt()
            .coerceIn(0, 11)
    }

    private fun findPreviousMasaChange(
        now: LocalDateTime,
        current: Int
    ): LocalDateTime {

        var check = now

        repeat(60000) {

            check =
                check.minusMinutes(10)

            if (
                getMasaIndex(check) != current
            ) {
                return check.plusMinutes(10)
            }
        }

        return now
    }

    private fun findNextMasaChange(
        now: LocalDateTime,
        current: Int
    ): Pair<String, LocalDateTime> {

        var check = now

        repeat(60000) {

            check =
                check.plusMinutes(10)

            val next =
                getMasaIndex(check)

            if (next != current) {

                return Pair(
                    "${
                        masaNames[current]
                    } → ${
                        masaNames[next]
                    }",
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
    // PRAHAR
    // =========================================================

    private data class PraharData(

        val name: String,

        val start: LocalDateTime,

        val nextName: String,

        val next: LocalDateTime
    )

    private fun getPraharData(
        now: LocalDateTime
    ): PraharData {

        val base =
            now
                .toLocalDate()
                .atTime(6, 0)

        val minutes =
            Duration.between(
                base,
                now
            )
                .toMinutes()

        val index =
            floor(
                minutes.toDouble() /
                        180.0
            )
                .toInt()

        val normalizedIndex =
            ((index % 8) + 8) % 8

        val start =
            base.plusMinutes(
                index.toLong() * 180L
            )

        val next =
            start.plusMinutes(
                180L
            )

        return PraharData(

            name =
                praharNames[normalizedIndex],

            start =
                start,

            nextName =
                praharNames[
                    (normalizedIndex + 1) % 8
                ],

            next =
                next
        )
    }

    // =========================================================
    // LAGNA
    // =========================================================

    private data class LagnaData(

        val name: String,

        val start: LocalDateTime,

        val nextName: String,

        val next: LocalDateTime
    )

    private fun getLagnaData(
        now: LocalDateTime
    ): LagnaData {

        /*
         * सध्याच्या version मध्ये
         * Moon longitude आधारित zodiac segment वापरला आहे.
         * पुढील version मध्ये latitude/longitude आधारित
         * exact Ascendant calculation जोडता येईल.
         */

        val longitude =
            getMoonLongitude(now)

        val index =
            floor(
                longitude / 30.0
            )
                .toInt()
                .coerceIn(0, 11)

        val start =
            now
                .withMinute(0)
                .withSecond(0)
                .withNano(0)

        val next =
            start.plusHours(2)

        return LagnaData(

            name =
                lagnaNames[index],

            start =
                start,

            nextName =
                lagnaNames[
                    (index + 1) % 12
                ],

            next =
                next
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
                280.460 +
                        0.9856474 * days
            )

        val meanAnomaly =
            Math.toRadians(
                normalize(
                    357.528 +
                            0.9856003 * days
                )
            )

        return normalize(
            meanLongitude +
                    1.915 *
                    kotlin.math.sin(meanAnomaly) +
                    0.020 *
                    kotlin.math.sin(
                        2 * meanAnomaly
                    )
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
                    kotlin.math.sin(mMoon) +
                    1.274 *
                    kotlin.math.sin(
                        2 * d - mMoon
                    ) +
                    0.658 *
                    kotlin.math.sin(
                        2 * d
                    ) +
                    0.214 *
                    kotlin.math.sin(
                        2 * mMoon
                    ) -
                    0.186 *
                    kotlin.math.sin(mSun)
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
                .atZone(zoneId)
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
    // FORMAT DATE
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

    // =========================================================
    // FORMAT DATE TIME
    // =========================================================

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
    // TO MILLIS
    // =========================================================

    private fun toMillis(
        time: LocalDateTime
    ): Long {

        return time
            .atZone(zoneId)
            .toInstant()
            .toEpochMilli()
    }

    // Compatibility function for MainActivity
    fun getCurrentPanchang(): PanchangState {
        return getCurrentPanchangState()
    }
}
}
