package com.mahaesuvidha.chandrapanchangalarm.model

data class PanchangState(

    val date: String,

    val weekday: String,

    val tithi: String,
    val nextTithi: String,
    val nextTithiTime: String,
    val nextTithiMillis: Long,

    val yoga: String,
    val nextYoga: String,
    val nextYogaTime: String,
    val nextYogaMillis: Long,

    val karana: String,
    val nextKarana: String,
    val nextKaranaTime: String,
    val nextKaranaMillis: Long,

    val paksha: String,
    val nextPaksha: String,
    val nextPakshaTime: String,
    val nextPakshaMillis: Long
    val sunrise: String
    
val sunset: String

val moonrise: String
val moonset: String

val masa: String

val prahar: String
val nextPrahar: String
val nextPraharTime: String

val lagna: String
val nextLagna: String
val nextLagnaTime: String
val nextLagnaMillis: Long
)
