package com.mahaesuvidha.chandrapanchangalarm.model

import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.math.sin

object LiveMoonCalculator {

    private data class NextChange(
        val text: String,
        val timeText: String,
        val timeMillis: Long,
        val type: String
    )

    fun getCurrentMoonState(): MoonState {

        val now = LocalDateTime.now()

        val moonLongitude =
            getSiderealMoonLongitude(now)

        val rashiIndex =
            (moonLongitude / 30.0)
                .toInt()
                .coerceIn(0, 11)

        val nakshatraSize =
            360.0 / 27.0

        val nakshatraIndex =
            (moonLongitude / nakshatraSize)
                .toInt()
                .coerceIn(0, 26)

        val positionInsideNakshatra =
            moonLongitude % nakshatraSize

        val pada =
            (positionInsideNakshatra /
                    (nakshatraSize / 4.0))
                .toInt()
                .coerceIn(0, 3) + 1

        val nextChange =
            getNextChange(
                now = now,
                currentRashi = rashiIndex,
                currentNakshatra = nakshatraIndex,
                currentPada = pada
            )

        return MoonState(
            location = "दौंड, महाराष्ट्र",

            rashi =
                Rashi.entries[rashiIndex],

            nakshatra =
                Nakshatra.entries[nakshatraIndex],

            pada = pada,

            nextChange =
                nextChange.text,

            nextChangeTime =
                nextChange.timeText,

            nextChangeMillis =
                nextChange.timeMillis,

            changeType =
                nextChange.type
        )
    }


    private fun getSiderealMoonLongitude(
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

        val sunLongitude =
            280.466 +
                    0.9856474 * d

        val sunMeanAnomaly =
            357.529 +
                    0.98560028 * d

        val moonLongitude =

            l +

                    6.289 *
                    sin(
                        Math.toRadians(m)
                    ) +

                    1.274 *
                    sin(
                        Math.toRadians(
                            2 * (l - sunLongitude) - m
                        )
                    ) +

                    0.658 *
                    sin(
                        Math.toRadians(
                            2 * (l - sunLongitude)
                        )
                    ) +

                    0.214 *
                    sin(
                        Math.toRadians(
                            2 * m
                        )
                    ) -

                    0.186 *
                    sin(
                        Math.toRadians(
                            sunMeanAnomaly
                        )
                    ) -

                    0.114 *
                    sin(
                        Math.toRadians(
                            2 * f
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
            moonLongitude -
                    ayanamsa
        )
    }


    private fun getJulianDay(
        dateTime: LocalDateTime
    ): Double {

        val instant =
            dateTime.toInstant(
                ZoneOffset.ofHoursMinutes(
                    5,
                    30
                )
            )

        return
            instant.epochSecond /
                    86400.0 +
                    2440587.5
    }


    private fun getNextChange(
        now: LocalDateTime,
        currentRashi: Int,
        currentNakshatra: Int,
        currentPada: Int
    ): NextChange {

        var checkTime =
            now

        repeat(4320) {

            checkTime =
                checkTime.plusMinutes(1)

            val longitude =
                getSiderealMoonLongitude(
                    checkTime
                )

            val rashi =
                (longitude / 30.0)
                    .toInt()
                    .coerceIn(0, 11)

            val nakshatraSize =
                360.0 / 27.0

            val nakshatra =
                (longitude / nakshatraSize)
                    .toInt()
                    .coerceIn(0, 26)

            val inside =
                longitude % nakshatraSize

            val pada =
                (inside /
                        (nakshatraSize / 4.0))
                    .toInt()
                    .coerceIn(0, 3) + 1

            val timeMillis =
                checkTime
                    .toInstant(
                        ZoneOffset.ofHoursMinutes(
                            5,
                            30
                        )
                    )
                    .toEpochMilli()


            if (rashi != currentRashi) {

                return NextChange(

                    text =
                        "${Rashi.entries[currentRashi].marathi} → " +
                                Rashi.entries[rashi].marathi,

                    timeText =
                        formatDateTime(
                            checkTime
                        ),

                    timeMillis =
                        timeMillis,

                    type =
                        "rashi"
                )
            }


            if (nakshatra != currentNakshatra) {

                return NextChange(

                    text =
                        "${Nakshatra.entries[currentNakshatra].marathi} → " +
                                Nakshatra.entries[nakshatra].marathi,

                    timeText =
                        formatDateTime(
                            checkTime
                        ),

                    timeMillis =
                        timeMillis,

                    type =
                        "nakshatra"
                )
            }


            if (pada != currentPada) {

                return NextChange(

                    text =
                        "चरण $currentPada → चरण $pada",

                    timeText =
                        formatDateTime(
                            checkTime
                        ),

                    timeMillis =
                        timeMillis,

                    type =
                        "charan"
                )
            }
        }


        return NextChange(

            text =
                "पुढील बदल शोधत आहे",

            timeText =
                formatDateTime(now),

            timeMillis =
                now.plusMinutes(10)
                    .toInstant(
                        ZoneOffset.ofHoursMinutes(
                            5,
                            30
                        )
                    )
                    .toEpochMilli(),

            type =
                "charan"
        )
    }


    private fun formatDateTime(
        dateTime: LocalDateTime
    ): String {

        val day =
            dateTime.dayOfMonth
                .toString()
                .padStart(
                    2,
                    '0'
                )

        val month =
            dateTime.monthValue
                .toString()
                .padStart(
                    2,
                    '0'
                )

        val hour =
            dateTime.hour
                .toString()
                .padStart(
                    2,
                    '0'
                )

        val minute =
            dateTime.minute
                .toString()
                .padStart(
                    2,
                    '0'
                )

        return "$day-$month-${dateTime.year} $hour:$minute"
    }


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
}
