package com.mahaesuvidha.chandrapanchangalarm.model

import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.math.sin

object LiveSunCalculator {

    private val INDIA_ZONE =
        ZoneOffset.ofHoursMinutes(5, 30)

    private const val NAKSHATRA_SIZE =
        360.0 / 27.0

    private const val PADA_SIZE =
        NAKSHATRA_SIZE / 4.0

    fun getCurrentSunState(): SunState {

        val now = LocalDateTime.now()

        val currentLongitude =
            getSiderealSunLongitude(now)

        val currentRashi =
            getRashiIndex(currentLongitude)

        val currentNakshatra =
            getNakshatraIndex(currentLongitude)

        val currentPada =
            getPada(currentLongitude)

        val nextRashi =
            findNextRashiChange(
                now,
                currentRashi
            )

        val nextNakshatra =
            findNextNakshatraChange(
                now,
                currentNakshatra
            )

        val nextCharan =
            findNextPadaChange(
                now,
                currentNakshatra,
                currentPada
            )

        return SunState(

            rashi =
                Rashi.entries[currentRashi],

            nakshatra =
                Nakshatra.entries[currentNakshatra],

            pada =
                currentPada,

            nextRashi =
                nextRashi.first,

            nextRashiTime =
                formatDateTime(nextRashi.second),

            nextRashiMillis =
                toMillis(nextRashi.second),

            nextNakshatra =
                nextNakshatra.first,

            nextNakshatraTime =
                formatDateTime(nextNakshatra.second),

            nextNakshatraMillis =
                toMillis(nextNakshatra.second),

            nextCharan =
                nextCharan.first,

            nextCharanTime =
                formatDateTime(nextCharan.second),

            nextCharanMillis =
                toMillis(nextCharan.second)
        )
    }

    private fun findNextRashiChange(
        now: LocalDateTime,
        currentRashi: Int
    ): Pair<String, LocalDateTime> {

        var check = now

        repeat(60000) {

            check = check.plusMinutes(1)

            val longitude =
                getSiderealSunLongitude(check)

            val rashi =
                getRashiIndex(longitude)

            if (rashi != currentRashi) {

                return Pair(
                    "${Rashi.entries[currentRashi].marathi} → " +
                            Rashi.entries[rashi].marathi,
                    check
                )
            }
        }

        return Pair(
            "पुढील राशी बदल शोधत आहे",
            now.plusDays(35)
        )
    }

    private fun findNextNakshatraChange(
        now: LocalDateTime,
        currentNakshatra: Int
    ): Pair<String, LocalDateTime> {

        var check = now

        repeat(30000) {

            check = check.plusMinutes(1)

            val longitude =
                getSiderealSunLongitude(check)

            val nakshatra =
                getNakshatraIndex(longitude)

            if (nakshatra != currentNakshatra) {

                return Pair(
                    "${Nakshatra.entries[currentNakshatra].marathi} → " +
                            Nakshatra.entries[nakshatra].marathi,
                    check
                )
            }
        }

        return Pair(
            "पुढील नक्षत्र बदल शोधत आहे",
            now.plusDays(20)
        )
    }

    private fun findNextPadaChange(
        now: LocalDateTime,
        currentNakshatra: Int,
        currentPada: Int
    ): Pair<String, LocalDateTime> {

        var check = now

        repeat(10000) {

            check = check.plusMinutes(1)

            val longitude =
                getSiderealSunLongitude(check)

            val nakshatra =
                getNakshatraIndex(longitude)

            val pada =
                getPada(longitude)

            if (
                nakshatra != currentNakshatra ||
                pada != currentPada
            ) {

                val nextPada =
                    if (nakshatra != currentNakshatra) 1
                    else pada

                return Pair(
                    "चरण $currentPada → चरण $nextPada",
                    check
                )
            }
        }

        return Pair(
            "पुढील चरण बदल शोधत आहे",
            now.plusDays(5)
        )
    }

    private fun getRashiIndex(
        longitude: Double
    ): Int {

        return (longitude / 30.0)
            .toInt()
            .coerceIn(0, 11)
    }

    private fun getNakshatraIndex(
        longitude: Double
    ): Int {

        return (longitude / NAKSHATRA_SIZE)
            .toInt()
            .coerceIn(0, 26)
    }

    private fun getPada(
        longitude: Double
    ): Int {

        val nakshatraPosition =
            longitude % NAKSHATRA_SIZE

        return (nakshatraPosition / PADA_SIZE)
            .toInt()
            .coerceIn(0, 3) + 1
    }

    private fun getSiderealSunLongitude(
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
                    sin(Math.toRadians(meanAnomaly)) +
                    0.019993 *
                    sin(Math.toRadians(2 * meanAnomaly)) +
                    0.000289 *
                    sin(Math.toRadians(3 * meanAnomaly))

        val tropicalLongitude =
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
            tropicalLongitude - ayanamsa
        )
    }

    private fun getJulianDay(
        dateTime: LocalDateTime
    ): Double {

        val instant =
            dateTime.toInstant(
                INDIA_ZONE
            )

        return (
                instant.epochSecond /
                        86400.0
                ) +
                2440587.5
    }

    private fun toMillis(
        dateTime: LocalDateTime
    ): Long {

        return dateTime
            .toInstant(
                INDIA_ZONE
            )
            .toEpochMilli()
    }

    private fun formatDateTime(
        dateTime: LocalDateTime
    ): String {

        val day =
            dateTime.dayOfMonth
                .toString()
                .padStart(2, '0')

        val month =
            dateTime.monthValue
                .toString()
                .padStart(2, '0')

        val hour =
            dateTime.hour
                .toString()
                .padStart(2, '0')

        val minute =
            dateTime.minute
                .toString()
                .padStart(2, '0')

        return "$day-$month-${dateTime.year} $hour:$minute"
    }

    private fun normalize(
        value: Double
    ): Double {

        var result = value % 360.0

        if (result < 0) {
            result += 360.0
        }

        return result
    }
}
