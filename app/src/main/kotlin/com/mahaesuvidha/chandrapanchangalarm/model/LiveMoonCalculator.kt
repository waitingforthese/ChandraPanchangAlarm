package com.mahaesuvidha.chandrapanchangalarm.model

import swisseph.SweConst
import swisseph.SweDate
import swisseph.SwissEph
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object LiveMoonCalculator {

    // ==========================================
    // CONSTANTS
    // ==========================================

    private const val RASHI_SIZE = 30.0

    private const val NAKSHATRA_SIZE =
        360.0 / 27.0

    private const val PADA_SIZE =
        360.0 / 108.0

    private const val SEARCH_STEP_MILLIS =
        60_000L


    // ==========================================
    // JULIAN DAY FOR GIVEN TIME
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
                    calendar.get(Calendar.MILLISECOND) / 3_600_000.0

        return SweDate.getJulDay(
            year,
            month,
            day,
            hour,
            SweDate.SE_GREG_CAL
        )
    }


    // ==========================================
    // MOON LONGITUDE
    // ==========================================

    private fun getMoonLongitudeAt(
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
            SweConst.SE_MOON,
            SweConst.SEFLG_SWIEPH or
                    SweConst.SEFLG_SIDEREAL,
            xx,
            serr
        )

        return xx[0]
    }


    // ==========================================
    // CURRENT MOON LONGITUDE
    // ==========================================

    fun getMoonLongitude(): Double {

        return getMoonLongitudeAt(
            System.currentTimeMillis()
        )
    }


    // ==========================================
    // CURRENT RASHI
    // ==========================================

    fun getCurrentRashi(): Rashi {

        val longitude =
            getMoonLongitude()

        val index =
            (longitude / RASHI_SIZE)
                .toInt()
                .coerceIn(0, 11)

        return Rashi.entries[index]
    }


    // ==========================================
    // CURRENT NAKSHATRA
    // ==========================================

    fun getCurrentNakshatra(): Nakshatra {

        val longitude =
            getMoonLongitude()

        val index =
            (longitude / NAKSHATRA_SIZE)
                .toInt()
                .coerceIn(0, 26)

        return Nakshatra.entries[index]
    }


    // ==========================================
    // CURRENT PADA
    // ==========================================

    fun getCurrentCharan(): Int {

        val longitude =
            getMoonLongitude()

        return (
            (longitude % NAKSHATRA_SIZE) /
                    PADA_SIZE
            ).toInt() + 1
    }


    // ==========================================
    // NEXT RASHI INDEX
    // ==========================================

    private fun getRashiIndexAt(
        millis: Long
    ): Int {

        return (
            getMoonLongitudeAt(millis) /
                    RASHI_SIZE
            )
            .toInt()
            .coerceIn(0, 11)
    }


    // ==========================================
    // NEXT NAKSHATRA INDEX
    // ==========================================

    private fun getNakshatraIndexAt(
        millis: Long
    ): Int {

        return (
            getMoonLongitudeAt(millis) /
                    NAKSHATRA_SIZE
            )
            .toInt()
            .coerceIn(0, 26)
    }


    // ==========================================
    // CURRENT PADA INDEX
    // ==========================================

    private fun getPadaNumberAt(
        millis: Long
    ): Int {

        val longitude =
            getMoonLongitudeAt(millis)

        return (
            (longitude % NAKSHATRA_SIZE) /
                    PADA_SIZE
            ).toInt() + 1
    }


    // ==========================================
    // FIND NEXT RASHI CHANGE
    // ==========================================

    private fun findNextRashiChange(): Long {

        val now =
            System.currentTimeMillis()

        val currentIndex =
            getRashiIndexAt(now)

        var checkTime =
            now

        val maxTime =
            now + (3 * 24 * 60 * 60 * 1000L)

        while (checkTime < maxTime) {

            val index =
                getRashiIndexAt(checkTime)

            if (index != currentIndex) {

                return refineRashiChange(
                    checkTime - SEARCH_STEP_MILLIS,
                    checkTime,
                    currentIndex
                )
            }

            checkTime +=
                SEARCH_STEP_MILLIS
        }

        return now
    }


    // ==========================================
    // REFINE RASHI CHANGE
    // ==========================================

    private fun refineRashiChange(
        start: Long,
        end: Long,
        oldIndex: Int
    ): Long {

        var low =
            start

        var high =
            end

        while (high - low > 1000L) {

            val mid =
                (low + high) / 2

            if (
                getRashiIndexAt(mid) ==
                oldIndex
            ) {

                low = mid

            } else {

                high = mid
            }
        }

        return high
    }


    // ==========================================
    // FIND NEXT NAKSHATRA CHANGE
    // ==========================================

    private fun findNextNakshatraChange(): Long {

        val now =
            System.currentTimeMillis()

        val currentIndex =
            getNakshatraIndexAt(now)

        var checkTime =
            now

        val maxTime =
            now + (2 * 24 * 60 * 60 * 1000L)

        while (checkTime < maxTime) {

            val index =
                getNakshatraIndexAt(checkTime)

            if (index != currentIndex) {

                return refineNakshatraChange(
                    checkTime - SEARCH_STEP_MILLIS,
                    checkTime,
                    currentIndex
                )
            }

            checkTime +=
                SEARCH_STEP_MILLIS
        }

        return now
    }


    // ==========================================
    // REFINE NAKSHATRA CHANGE
    // ==========================================

    private fun refineNakshatraChange(
        start: Long,
        end: Long,
        oldIndex: Int
    ): Long {

        var low =
            start

        var high =
            end

        while (high - low > 1000L) {

            val mid =
                (low + high) / 2

            if (
                getNakshatraIndexAt(mid) ==
                oldIndex
            ) {

                low = mid

            } else {

                high = mid
            }
        }

        return high
    }


    // ==========================================
    // FIND NEXT PADA CHANGE
    // ==========================================

    private fun findNextPadaChange(): Long {

        val now =
            System.currentTimeMillis()

        val currentPada =
            getPadaNumberAt(now)

        var checkTime =
            now

        val maxTime =
            now + (12 * 60 * 60 * 1000L)

        while (checkTime < maxTime) {

            val pada =
                getPadaNumberAt(checkTime)

            if (pada != currentPada) {

                return refinePadaChange(
                    checkTime - SEARCH_STEP_MILLIS,
                    checkTime,
                    currentPada
                )
            }

            checkTime +=
                SEARCH_STEP_MILLIS
        }

        return now
    }


    // ==========================================
    // REFINE PADA CHANGE
    // ==========================================

    private fun refinePadaChange(
        start: Long,
        end: Long,
        oldPada: Int
    ): Long {

        var low =
            start

        var high =
            end

        while (high - low > 1000L) {

            val mid =
                (low + high) / 2

            if (
                getPadaNumberAt(mid) ==
                oldPada
            ) {

                low = mid

            } else {

                high = mid
            }
        }

        return high
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

        return formatter.format(
            Date(millis)
        )
    }


    // ==========================================
    // CURRENT MOON STATE
    // ==========================================

    fun getCurrentMoonState(): MoonState {

        val currentRashi =
            getCurrentRashi()

        val currentNakshatra =
            getCurrentNakshatra()

        val currentPada =
            getCurrentCharan()


        // ======================================
        // CALCULATE EXACT NEXT CHANGES
        // ======================================

        val nextRashiMillis =
            findNextRashiChange()

        val nextNakshatraMillis =
            findNextNakshatraChange()

        val nextCharanMillis =
            findNextPadaChange()


        // ======================================
        // NEXT VALUES
        // ======================================

        val nextRashiIndex =
            getRashiIndexAt(
                nextRashiMillis + 1000L
            )

        val nextNakshatraIndex =
            getNakshatraIndexAt(
                nextNakshatraMillis + 1000L
            )

        val nextPada =
            getPadaNumberAt(
                nextCharanMillis + 1000L
            )


        return MoonState(

            location =
                "Daund",

            rashi =
                currentRashi,

            nakshatra =
                currentNakshatra,

            pada =
                currentPada,


            // ==================================
            // NEXT RASHI
            // ==================================

            nextRashi =
                Rashi.entries[
                    nextRashiIndex
                ].marathi,

            nextRashiTime =
                formatTime(
                    nextRashiMillis
                ),

            nextRashiMillis =
                nextRashiMillis,


            // ==================================
            // NEXT NAKSHATRA
            // ==================================

            nextNakshatra =
                Nakshatra.entries[
                    nextNakshatraIndex
                ].marathi,

            nextNakshatraTime =
                formatTime(
                    nextNakshatraMillis
                ),

            nextNakshatraMillis =
                nextNakshatraMillis,


            // ==================================
            // NEXT CHARAN
            // ==================================

            nextCharan =
                "चरण $nextPada",

            nextCharanTime =
                formatTime(
                    nextCharanMillis
                ),

            nextCharanMillis =
                nextCharanMillis
        )
    }
}
