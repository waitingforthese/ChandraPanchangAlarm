package com.mahaesuvidha.chandrapanchangalarm.model

object LivePanchangCalculator {

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

            nextTithi = "अष्टमी",

            nextTithiTime = "19-08-2026 19:14",

            nextTithiMillis =
                System.currentTimeMillis() +
                        (60 * 60 * 1000),


            // ==========================================
            // YOGA
            // ==========================================

            yoga = "शुक्ल",

            nextYoga = "ब्रह्म",

            nextYogaTime = "19-08-2026 03:00",

            nextYogaMillis =
                System.currentTimeMillis() +
                        (2 * 60 * 60 * 1000),


            // ==========================================
            // KARANA
            // ==========================================

            karana = "गर",

            nextKarana = "वणिज",

            nextKaranaTime = "19-08-2026 06:09",

            nextKaranaMillis =
                System.currentTimeMillis() +
                        (3 * 60 * 60 * 1000),


            // ==========================================
            // PAKSHA
            // ==========================================

            paksha = "शुक्ल पक्ष",

            nextPaksha = "कृष्ण पक्ष",

            nextPakshaTime = "28-08-2026 12:59",

            nextPakshaMillis =
                System.currentTimeMillis() +
                        (24 * 60 * 60 * 1000),


            // ==========================================
            // MAS
            // ==========================================

            masa = "श्रावण",


            // ==========================================
            // PRAHAR
            // ==========================================

            prahar = "दिवसाचा दुसरा प्रहर",

            nextPrahar = "दिवसाचा तिसरा प्रहर",

            nextPraharTime = "19-08-2026 12:30",

            nextPraharMillis =
                System.currentTimeMillis() +
                        (30 * 60 * 1000),


            // ==========================================
            // LAGNA
            // ==========================================

            lagna = "कर्क लग्न",

            nextLagna = "सिंह लग्न",

            nextLagnaTime = "19-08-2026 11:45",

            nextLagnaMillis =
                System.currentTimeMillis() +
                        (45 * 60 * 1000)
        )
    }
}
