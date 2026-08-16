package com.mahaesuvidha.chandrapanchangalarm.model

enum class Rashi(val marathi: String, val english: String) {
    MESHA("मेष", "Aries"), VRISHABHA("वृषभ", "Taurus"),
    MITHUNA("मिथुन", "Gemini"), KARKA("कर्क", "Cancer"),
    SIMHA("सिंह", "Leo"), KANYA("कन्या", "Virgo"),
    TULA("तुला", "Libra"), VRISCHIKA("वृश्चिक", "Scorpio"),
    DHANU("धनु", "Sagittarius"), MAKARA("मकर", "Capricorn"),
    KUMBHA("कुंभ", "Aquarius"), MEENA("मीन", "Pisces")
}

enum class Nakshatra(val marathi: String, val english: String) {
    ASHWINI("अश्विनी", "Ashwini"), BHARANI("भरणी", "Bharani"),
    KRITTIKA("कृत्तिका", "Krittika"), ROHINI("रोहिणी", "Rohini"),
    MRIGASHIRSHA("मृगशीर्ष", "Mrigashirsha"), ARDRA("आर्द्रा", "Ardra"),
    PUNARVASU("पुनर्वसू", "Punarvasu"), PUSHYA("पुष्य", "Pushya"),
    ASHLESHA("आश्लेषा", "Ashlesha"), MAGHA("मघा", "Magha"),
    PURVA_PHALGUNI("पूर्वाफाल्गुनी", "Purva Phalguni"),
    UTTARA_PHALGUNI("उत्तराफाल्गुनी", "Uttara Phalguni"),
    HASTA("हस्त", "Hasta"), CHITRA("चित्रा", "Chitra"),
    SWATI("स्वाती", "Swati"), VISHAKHA("विशाखा", "Vishakha"),
    ANURADHA("अनुराधा", "Anuradha"), JYESHTHA("ज्येष्ठा", "Jyeshtha"),
    MULA("मूळ", "Mula"), PURVA_ASHADHA("पूर्वाषाढा", "Purva Ashadha"),
    UTTARA_ASHADHA("उत्तराषाढा", "Uttara Ashadha"),
    SHRAVANA("श्रवण", "Shravana"), DHANISHTHA("धनिष्ठा", "Dhanishtha"),
    SHATABHISHA("शतभिषा", "Shatabhisha"), PURVA_BHADRAPADA("पूर्वाभाद्रपदा", "Purva Bhadrapada"),
    UTTARA_BHADRAPADA("उत्तराभाद्रपदा", "Uttara Bhadrapada"),
    REVATI("रेवती", "Revati")
}

data class MoonState(
    val location: String,
    val rashi: Rashi,
    val nakshatra: Nakshatra,
    val pada: Int,
    val nextChange: String,
    val nextChangeTime: String
)
