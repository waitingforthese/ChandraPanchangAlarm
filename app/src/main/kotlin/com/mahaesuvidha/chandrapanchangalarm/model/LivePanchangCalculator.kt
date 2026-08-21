package com.mahaesuvidha.chandrapanchangalarm.model

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

object LivePanchangCalculator {

    // ==========================================
    // DATE TIME → MILLIS
    // ==========================================

    private fun toMillis(
        dateTime: String
    ): Long {

        return try {

            val formatter =
                SimpleDateFormat(
                    "dd-MM-yyyy HH:mm",
                    Locale.getDefault()
                )

            formatter.timeZone =
                TimeZone.getDefault()

            formatter.parse(dateTime)?.time
                ?: 0L

        } catch (_: Exception) {

            0L
        }
    }


    fun getCurrentPanchangState(): PanchangState {

        return PanchangState(

            // ==========================================
            // BASIC
            // ==========================================

            date = "19-08-2026",

            weekday = "बुधवार",


            // ==========================================
            // TITHI
            // ==========================================

            tithi = "सप्तमी",

            tithiStartTime = "18-08-2026 18:42",

            nextTithi = "अष्टमी",

            nextTithiTime = "19-08-2026 19:14",

            nextTithiMillis =
                toMillis(
                    "19-08-2026 19:14"
                ),


            // ==========================================
            // YOGA
            // ==========================================

            yoga = "शुक्ल",

            yogaStartTime = "18-08-2026 03:02",

            nextYoga = "ब्रह्म",

            nextYogaTime = "19-08-2026 03:00",

            nextYogaMillis =
                toMillis(
                    "19-08-2026 03:00"
                ),


            // ==========================================
            // KARANA
            // ==========================================

            karana = "गर",

            karanaStartTime = "18-08-2026 17:52",

            nextKarana = "वणिज",

            nextKaranaTime = "19-08-2026 06:09",

            nextKaranaMillis =
                toMillis(
                    "19-08-2026 06:09"
                ),


            // ==========================================
            // PAKSHA
            // ==========================================

            paksha = "शुक्ल पक्ष",

            pakshaStartTime = "14-08-2026 12:10",

            nextPaksha = "कृष्ण पक्ष",

            nextPakshaTime = "28-08-2026 12:59",

            nextPakshaMillis =
                toMillis(
                    "28-08-2026 12:59"
                ),


            // ==========================================
            // MAS
            // ==========================================

            masa = "श्रावण",

            masaStartTime = "25-07-2026 08:31",

            nextMasa = "भाद्रपद",

            nextMasaTime = "23-08-2026 12:18",

            nextMasaMillis =
                toMillis(
                    "23-08-2026 12:18"
                ),


            // ==========================================
            // PRAHAR
            // ==========================================

            prahar = "दिवसाचा दुसरा प्रहर",

            praharStartTime = "19-08-2026 09:30",

            nextPrahar = "दिवसाचा तिसरा प्रहर",

            nextPraharTime = "19-08-2026 12:30",

            nextPraharMillis =
                toMillis(
                    "19-08-2026 12:30"
                ),


            // ==========================================
            // LAGNA
            // ==========================================

            lagna = "कर्क लग्न",

            lagnaStartTime = "19-08-2026 09:45",

            nextLagna = "सिंह लग्न",

            nextLagnaTime = "19-08-2026 11:45",

            nextLagnaMillis =
                toMillis(
                    "19-08-2026 11:45"
                )
        )
    }
}
