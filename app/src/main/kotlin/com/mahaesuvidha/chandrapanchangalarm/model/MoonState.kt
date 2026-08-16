package com.mahaesuvidha.chandrapanchangalarm.model

data class MoonState(

    // Location
    val location: String,

    // सध्याची चंद्र राशी
    val rashi: Rashi,

    // सध्याचे नक्षत्र
    val nakshatra: Nakshatra,

    // सध्याचा चरण
    val pada: Int,

    // पुढील बदलाचे नाव
    // उदाहरण: हस्त → चित्रा
    val nextChange: String,

    // पुढील बदलाची Date आणि Time
    // उदाहरण: 17-08-2026 02:04
    val nextChangeTime: String,

    // पुढील बदलाची Exact Time milliseconds मध्ये
    // AlarmScheduler साठी वापरले जाते
    val nextChangeMillis: Long,

    // कोणता बदल आहे
    // rashi / nakshatra / charan
    val changeType: String
)
