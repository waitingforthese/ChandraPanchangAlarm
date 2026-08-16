package com.mahaesuvidha.chandrapanchangalarm.model

import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.math.*

object LiveMoonCalculator {

    fun getCurrentMoonState(): MoonState {

        val now = LocalDateTime.now()
        val moonLongitude = getSiderealMoonLongitude(now)

        val rashiIndex = (moonLongitude / 30.0).toInt().coerceIn(0, 11)

        val nakshatraSize = 360.0 / 27.0
        val nakshatraIndex =
            (moonLongitude / nakshatraSize).toInt().coerceIn(0, 26)

        val positionInsideNakshatra =
            moonLongitude % nakshatraSize

        val pada =
            (positionInsideNakshatra / (nakshatraSize / 4.0))
                .toInt()
                .coerceIn(0, 3) + 1

        val nextChangeInfo =
            getNextChange(now, rashiIndex, nakshatraIndex, pada)

        return MoonState(
            location = "दौंड, महाराष्ट्र",
            rashi = Rashi.entries[rashiIndex],
            nakshatra = Nakshatra.entries[nakshatraIndex],
            pada = pada,
            nextChange = nextChangeInfo.first,
            nextChangeTime = nextChangeInfo.second
        )
    }

    private fun getSiderealMoonLongitude(
        dateTime: LocalDateTime
    ): Double {

        val jd = getJulianDay(dateTime)

        val d = jd - 2451545.0

        val l =
            normalize(
                218.316 + 13.176396 * d
            )

        val m =
            normalize(
                134.963 + 13.064993 * d
            )

        val f =
            normalize(
                93.272 + 13.229350 * d
            )

        val moonLongitude =
            l +
                6.289 * sin(Math.toRadians(m)) +
                1.274 * sin(Math.toRadians(2 * (l - 280.466) - m)) +
                0.658 * sin(Math.toRadians(2 * (l - 280.466))) +
                0.214 * sin(Math.toRadians(2 * m)) -
                0.186 * sin(Math.toRadians(357.529 + 0.98560028 * d)) -
                0.114 * sin(Math.toRadians(2 * f))

        /*
         * Approximate Lahiri Ayanamsa.
         * Tropical longitude मधून वजा करून
         * Indian/Sidereal longitude मिळते.
         */
        val ayanamsa =
            23.85675 + (jd - 2451545.0) / 36525.0 * 1.396971

        return normalize(moonLongitude - ayanamsa)
    }

    private fun getJulianDay(
        dateTime: LocalDateTime
    ): Double {

        val instant =
            dateTime.toInstant(
                ZoneOffset.ofHoursMinutes(5, 30)
            )

        return instant.epochSecond / 86400.0 + 2440587.5
    }

    private fun getNextChange(
        now: LocalDateTime,
        currentRashi: Int,
        currentNakshatra: Int,
        currentPada: Int
    ): Pair<String, String> {

        var checkTime = now

        /*
         * पुढील 3 दिवस minute-by-minute तपासणी.
         * चंद्राची राशी/नक्षत्र/चरण बदलण्याची
         * पुढील वेळ शोधतो.
         */
        repeat(4320) {

            checkTime = checkTime.plusMinutes(1)

            val longitude =
                getSiderealMoonLongitude(checkTime)

            val rashi =
                (longitude / 30.0).toInt().coerceIn(0, 11)

            val nakshatraSize = 360.0 / 27.0

            val nakshatra =
                (longitude / nakshatraSize)
                    .toInt()
                    .coerceIn(0, 26)

            val inside =
                longitude % nakshatraSize

            val pada =
                (inside / (nakshatraSize / 4.0))
                    .toInt()
                    .coerceIn(0, 3) + 1

            when {
                rashi != currentRashi -> {
                    return Pair(
                        "${Rashi.entries[currentRashi].marathi} → " +
                                Rashi.entries[rashi].marathi,
                        formatDateTime(checkTime)
                    )
                }

                nakshatra != currentNakshatra -> {
                    return Pair(
                        "${Nakshatra.entries[currentNakshatra].marathi} → " +
                                Nakshatra.entries[nakshatra].marathi,
                        formatDateTime(checkTime)
                    )
                }

                pada != currentPada -> {
                    return Pair(
                        "चरण $currentPada → चरण $pada",
                        formatDateTime(checkTime)
                    )
                }
            }
        }

        return Pair(
            "पुढील बदल शोधत आहे",
            formatDateTime(now)
        )
    }

    private fun formatDateTime(
        dateTime: LocalDateTime
    ): String {

        val day =
            dateTime.dayOfMonth.toString()
                .padStart(2, '0')

        val month =
            dateTime.monthValue.toString()
                .padStart(2, '0')

        val hour =
            dateTime.hour.toString()
                .padStart(2, '0')

        val minute =
            dateTime.minute.toString()
                .padStart(2, '0')

        return "$day-$month-${dateTime.year} $hour:$minute"
    }

    private fun normalize(value: Double): Double {

        var result = value % 360.0

        if (result < 0) {
            result += 360.0
        }

        return result
    }
}
