package com.mahaesuvidha.chandrapanchangalarm.model

data class PlanetInfo(
    val name: String,
    val friendly: Boolean,
    val enemy: Boolean
)

data class JyotishInfo(
    val rashiLord: String,
    val nakshatraLord: String,
    val navamshaRashi: String,
    val navamshaLord: String,
    val enemies: List<String>
)

object JyotishMaster {

    // -----------------------------
    // RASHI LORDS
    // -----------------------------

    private val rashiLords = mapOf(
        Rashi.MESHA to "मंगळ",
        Rashi.VRISHABHA to "शुक्र",
        Rashi.MITHUNA to "बुध",
        Rashi.KARKA to "चंद्र",
        Rashi.SIMHA to "सूर्य",
        Rashi.KANYA to "बुध",
        Rashi.TULA to "शुक्र",
        Rashi.VRISHCHIKA to "मंगळ",
        Rashi.DHANU to "गुरु",
        Rashi.MAKARA to "शनि",
        Rashi.KUMBHA to "शनि",
        Rashi.MEENA to "गुरु"
    )

    // -----------------------------
    // NAKSHATRA LORDS
    // -----------------------------

    private val nakshatraLords = mapOf(
        Nakshatra.ASHWINI to "केतू",
        Nakshatra.BHARANI to "शुक्र",
        Nakshatra.KRITTIKA to "सूर्य",
        Nakshatra.ROHINI to "चंद्र",
        Nakshatra.MRIGASHIRSHA to "मंगळ",
        Nakshatra.ARDRA to "राहू",
        Nakshatra.PUNARVASU to "गुरु",
        Nakshatra.PUSHYA to "शनि",
        Nakshatra.ASHLESHA to "बुध",
        Nakshatra.MAGHA to "केतू",
        Nakshatra.PURVA_PHALGUNI to "शुक्र",
        Nakshatra.UTTARA_PHALGUNI to "सूर्य",
        Nakshatra.HASTA to "चंद्र",
        Nakshatra.CHITRA to "मंगळ",
        Nakshatra.SWATI to "राहू",
        Nakshatra.VISHAKHA to "गुरु",
        Nakshatra.ANURADHA to "शनि",
        Nakshatra.JYESHTHA to "बुध",
        Nakshatra.MOOLA to "केतू",
        Nakshatra.PURVA_ASHADHA to "शुक्र",
        Nakshatra.UTTARA_ASHADHA to "सूर्य",
        Nakshatra.SHRAVANA to "चंद्र",
        Nakshatra.DHANISHTHA to "मंगळ",
        Nakshatra.SHATABHISHA to "राहू",
        Nakshatra.PURVA_BHADRAPADA to "गुरु",
        Nakshatra.UTTARA_BHADRAPADA to "शनि",
        Nakshatra.REVATI to "बुध"
    )

    // -----------------------------
    // NAVAMSHA
    // 108 PADA -> 12 RASHI
    // -----------------------------

    private val navamshaRashis = listOf(
        Rashi.MESHA,
        Rashi.VRISHABHA,
        Rashi.MITHUNA,
        Rashi.KARKA,
        Rashi.SIMHA,
        Rashi.KANYA,
        Rashi.TULA,
        Rashi.VRISHCHIKA,
        Rashi.DHANU,
        Rashi.MAKARA,
        Rashi.KUMBHA,
        Rashi.MEENA
    )

    fun getRashiLord(
        rashi: Rashi
    ): String {
        return rashiLords[rashi] ?: "—"
    }

    fun getNakshatraLord(
        nakshatra: Nakshatra
    ): String {
        return nakshatraLords[nakshatra] ?: "—"
    }

    fun getNavamshaRashi(
        nakshatra: Nakshatra,
        pada: Int
    ): Rashi {

        val nakshatraIndex =
            Nakshatra.entries.indexOf(nakshatra)

        val safePada =
            pada.coerceIn(1, 4)

        val index =
            (nakshatraIndex * 4 + (safePada - 1)) % 12

        return navamshaRashis[index]
    }

    fun getNavamshaLord(
        navamshaRashi: Rashi
    ): String {
        return getRashiLord(navamshaRashi)
    }

    // -----------------------------
    // NATURAL PLANET RELATIONSHIPS
    // -----------------------------

    private val enemies = mapOf(

        "सूर्य" to setOf(
            "शुक्र",
            "शनि"
        ),

        "चंद्र" to emptySet(),

        "मंगळ" to setOf(
            "बुध"
        ),

        "बुध" to setOf(
            "चंद्र"
        ),

        "गुरु" to setOf(
            "बुध",
            "शुक्र"
        ),

        "शुक्र" to setOf(
            "सूर्य",
            "चंद्र"
        ),

        "शनि" to setOf(
            "सूर्य",
            "चंद्र"
        ),

        "राहू" to setOf(
            "सूर्य",
            "चंद्र",
            "मंगळ"
        ),

        "केतू" to setOf(
            "सूर्य",
            "चंद्र",
            "मंगळ"
        )
    )

    fun getEnemies(
        planet1: String,
        planet2: String
    ): Boolean {

        return enemies[planet1]
            ?.contains(planet2) == true ||
                enemies[planet2]
                    ?.contains(planet1) == true
    }

    fun getInfo(
        rashi: Rashi,
        nakshatra: Nakshatra,
        pada: Int
    ): JyotishInfo {

        val rashiLord =
            getRashiLord(rashi)

        val nakshatraLord =
            getNakshatraLord(nakshatra)

        val navamshaRashi =
            getNavamshaRashi(
                nakshatra,
                pada
            )

        val navamshaLord =
            getNavamshaLord(
                navamshaRashi
            )

        val allPlanets =
            listOf(
                rashiLord,
                nakshatraLord,
                navamshaLord
            )

        val enemyList =
            allPlanets
                .distinct()
                .filter { planet ->

                    allPlanets.any { other ->

                        other != planet &&
                            getEnemies(
                                planet,
                                other
                            )
                    }
                }

        return JyotishInfo(

            rashiLord =
                rashiLord,

            nakshatraLord =
                nakshatraLord,

            navamshaRashi =
                navamshaRashi.marathi,

            navamshaLord =
                navamshaLord,

            enemies =
                enemyList
        )
    }
}
