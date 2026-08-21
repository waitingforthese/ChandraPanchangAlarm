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
            jd,
            planet,
            SweConst.SEFLG_SWIEPH or
                    SweConst.SEFLG_SIDEREAL,
            xx,
            serr
        )

        return normalize(xx[0])
    }


    // ==========================================
    // SUN + MOON LONGITUDE
    // ==========================================

    private fun getSunLongitude(
        jd: Double
    ): Double {

        return getLongitude(
            SweConst.SE_SUN,
            jd
        )
    }

    private fun getMoonLongitude(
        jd: Double
    ): Double {

        return getLongitude(
            SweConst.SE_MOON,
            jd
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

        if (result < 0) {
            result += 360.0
        }

        return result
    }


    // ==========================================
    // TITHI
    // ==========================================

    private fun getTithiIndex(
        sun: Double,
        moon: Double
    ): Int {

        val difference =
            normalize(
                moon - sun
            )

        return floor(
            difference / TITHI_SIZE
        ).toInt() + 1
    }


    private fun getTithiName(
        index: Int
    ): String {

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

            names[
                (index - 1) % 15
            ]
        }
    }


    // ==========================================
    // PAKSHA
    // ==========================================

    private fun getPaksha(
        tithiIndex: Int
    ): String {

        return if (
            tithiIndex <= 15
        ) {

            "शुक्ल पक्ष"

        } else {

            "कृष्ण पक्ष"
        }
    }


    // ==========================================
    // KARANA
    // ==========================================

    private fun getKaranaIndex(
        sun: Double,
        moon: Double
    ): Int {

        val difference =
            normalize(
                moon - sun
            )

        return floor(
            difference / KARANA_SIZE
        ).toInt()
    }


    private fun getKaranaName(
        index: Int
    ): String {

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

            0 ->
                "किंस्तुघ्न"

            57 ->
                "शकुनि"

            58 ->
                "चतुष्पाद"

            59 ->
                "नाग"

            else ->
                repeating[
                    (index - 1) % 7
                ]
        }
    }


    // ==========================================
    // YOGA
    // ==========================================

    private fun getYogaIndex(
        sun: Double,
        moon: Double
    ): Int {

        val total =
            normalize(
                sun + moon
            )

        return floor(
            total / YOGA_SIZE
        ).toInt()
    }


    private fun getYogaName(
        index: Int
    ): String {

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

        return names[
            index.coerceIn(
                0,
                26
            )
        ]
    }


    // ==========================================
    // FIND NEXT CHANGE
    // ==========================================

    private fun findNextChange(
        currentIndex: Int,
        getIndex: (Double) -> Int
    ): Long {

        val now =
            System.currentTimeMillis()

        var checkMillis =
            now

        repeat(10080) {

            checkMillis +=
                60_000L

            val jd =
                2440587.5 +
                        checkMillis /
                        86400000.0

            val newIndex =
                getIndex(jd)

            if (
                newIndex !=
                currentIndex
            ) {

                return checkMillis
            }
        }

        return now
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

        formatter.timeZone =
            indiaTimeZone

        return formatter.format(
            millis
        )
    }


    // ==========================================
    // MAIN PANCHANG STATE
    // ==========================================

    fun getCurrentPanchangState(): PanchangState {

        val jd =
            getJulianDay()

        val sun =
            getSunLongitude(jd)

        val moon =
            getMoonLongitude(jd)


        // TITHI

        val tithiIndex =
            getTithiIndex(
                sun,
                moon
            )

        val nextTithiMillis =
            findNextChange(
                tithiIndex
            ) { checkJd ->

                val checkSun =
                    getSunLongitude(
                        checkJd
                    )

                val checkMoon =
                    getMoonLongitude(
                        checkJd
                    )

                getTithiIndex(
                    checkSun,
                    checkMoon
                )
            }

        val nextTithiIndex =
            getTithiIndex(
                getSunLongitude(
                    2440587.5 +
                            nextTithiMillis /
                            86400000.0
                ),
                getMoonLongitude(
                    2440587.5 +
                            nextTithiMillis /
                            86400000.0
                )
            )


        // YOGA

        val yogaIndex =
            getYogaIndex(
                sun,
                moon
            )

        val nextYogaMillis =
            findNextChange(
                yogaIndex
            ) { checkJd ->

                getYogaIndex(
                    getSunLongitude(checkJd),
                    getMoonLongitude(checkJd)
                )
            }


        // KARANA

        val karanaIndex =
            getKaranaIndex(
                sun,
                moon
            )

        val nextKaranaMillis =
            findNextChange(
                karanaIndex
            ) { checkJd ->

                getKaranaIndex(
                    getSunLongitude(checkJd),
                    getMoonLongitude(checkJd)
                )
            }


        // CURRENT DATE

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
                Locale(
                    "mr",
                    "IN"
                )
            )

        weekdayFormatter.timeZone =
            indiaTimeZone


        return PanchangState(

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

            nextYoga =
                getYogaName(
                    getYogaIndex(
                        getSunLongitude(
                            2440587.5 +
                                    nextYogaMillis /
                                    86400000.0
                        ),
                        getMoonLongitude(
                            2440587.5 +
                                    nextYogaMillis /
                                    86400000.0
                        )
                    )
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

            nextKarana =
                getKaranaName(
                    getKaranaIndex(
                        getSunLongitude(
                            2440587.5 +
                                    nextKaranaMillis /
                                    86400000.0
                        ),
                        getMoonLongitude(
                            2440587.5 +
                                    nextKaranaMillis /
                                    86400000.0
                        )
                    )
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

            nextPaksha =
                getPaksha(
                    nextTithiIndex
                ),

            nextPakshaTime =
                formatTime(
                    nextTithiMillis
                ),

            nextPakshaMillis =
                nextTithiMillis
        )
    }
}
