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

        // सध्याची राशी
        val rashiIndex =
            (moonLongitude / 30.0)
                .toInt()
                .coerceIn(0, 11)

        // सध्याचे नक्षत्र
        val nakshatraSize =
            360.0 / 27.0

        val nakshatraIndex =
            (moonLongitude / nakshatraSize)
                .toInt()
                .coerceIn(0, 26)

        // नक्षत्रातील position
        val positionInsideNakshatra =
            moonLongitude % nakshatraSize

        // सध्याचा चरण
        val pada =
            (
                positionInsideNakshatra /
                    (nakshatraSize / 4.0)
            )
                .toInt()
                .coerceIn(0, 3) + 1

        // पुढील बदल शोधा
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

    /*
     * LIVE AUTO ALARM साठी
     * पुढील चंद्र बदलाची माहिती मिळवा
     */
    fun getNextAlarm(): MoonState {

        return getCurrentMoonState()
    }

    /*
     * Sidereal Moon Longitude
     */
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

        val moonLongitude =
            l +
                6.289 *
                    sin(
                        Math.toRadians(m)
                    ) +
                1.274 *
                    sin(
                        Math.toRadians(
                            2 * (l - 280.466) - m
                        )
                    ) +
                0.658 *
                    sin(
                        Math.toRadians(
                            2 * (l - 280.466)
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
                            357.529 +
                                0.98560028 * d
                        )
                    ) -
                0.114 *
                    sin(
                        Math.toRadians(
                            2 * f
                        )
                    )

        /*
         * Approximate Lahiri Ayanamsa
         */
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

    /*
     * Julian Day Calculation
     * India Time Zone +05:30
     */
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

    /*
     * पुढील राशी / नक्षत्र / चरण
     * यापैकी जो बदल आधी होईल तो शोधा
     */
    private fun getNextChange(
        now: LocalDateTime,
        currentRashi: Int,
        currentNakshatra: Int,
        currentPada: Int
    ): NextChange {

        var checkTime =
            now

        /*
         * पुढील 3 दिवस
         * Minute by minute तपासणी
         */
        repeat(4320) {

            checkTime =
                checkTime.plusMinutes(1)

            val longitude =
                getSiderealMoonLongitude(
                    checkTime
                )

            // राशी
            val rashi =
                (longitude / 30.0)
                    .toInt()
                    .coerceIn(0, 11)

            // नक्षत्र
            val nakshatraSize =
                360.0 / 27.0

            val nakshatra =
                (longitude / nakshatraSize)
                    .toInt()
                    .coerceIn(0, 26)

            // नक्षत्रातील स्थान
            val inside =
                longitude %
                    nakshatraSize

            // चरण
            val pada =
                (
                    inside /
                        (nakshatraSize / 4.0)
                )
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

            /*
             * जो बदल सर्वात आधी मिळेल
             * तो LIVE Alarm म्हणून वापरा
             */

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

            if (
                nakshatra !=
                currentNakshatra
            ) {

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

            if (
                pada !=
                currentPada
            ) {

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

        /*
         * 3 दिवसांत बदल सापडला नाही तर
         * 10 मिनिटांनी पुन्हा check
         */
        val fallbackTime =
            now.plusMinutes(10)

        return NextChange(

            text =
                "पुढील बदल शोधत आहे",

            timeText =
                formatDateTime(
                    fallbackTime
                ),

            timeMillis =
                fallbackTime
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

    /*
     * Date आणि Time format
     */
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

        return
            "$day-$month-${dateTime.year} $hour:$minute"
    }

    /*
     * Longitude 0 ते 360 मध्ये ठेवा
     */
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
