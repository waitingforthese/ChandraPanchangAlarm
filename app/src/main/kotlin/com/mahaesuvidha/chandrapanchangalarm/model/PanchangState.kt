package com.mahaesuvidha.chandrapanchangalarm.model

data class PanchangState(

    // ==========================================
    // BASIC DATE
    // ==========================================

    val date: String,

    val weekday: String,


    // ==========================================
    // TITHI
    // ==========================================

    val tithi: String,

    val nextTithi: String,

    val nextTithiTime: String,

    val nextTithiMillis: Long,


    // ==========================================
    // YOGA
    // ==========================================

    val yoga: String,

    val nextYoga: String,

    val nextYogaTime: String,

    val nextYogaMillis: Long,


    // ==========================================
    // KARANA
    // ==========================================

    val karana: String,

    val nextKarana: String,

    val nextKaranaTime: String,

    val nextKaranaMillis: Long,


    // ==========================================
    // PAKSHA
    // ==========================================

    val paksha: String,

    val nextPaksha: String,

    val nextPakshaTime: String,

    val nextPakshaMillis: Long,


    // ==========================================
    // MAS
    // ==========================================

    val masa: String,


    // ==========================================
    // PRAHAR
    // ==========================================

    val prahar: String,

    val nextPrahar: String,

    val nextPraharTime: String,

    val nextPraharMillis: Long,


    // ==========================================
    // LAGNA
    // ==========================================

    val lagna: String,

    val nextLagna: String,

    val nextLagnaTime: String,

    val nextLagnaMillis: Long

    // ==========================================
// MAS
// ==========================================

val masa: String = "—",

// ==========================================
// PRAHAR
// ==========================================

val prahar: String = "—",

val nextPrahar: String = "—",

val nextPraharTime: String = "—",

val nextPraharMillis: Long = 0L,

// ==========================================
// LAGNA
// ==========================================

val lagna: String = "—",

val nextLagna: String = "—",

val nextLagnaTime: String = "—",

val nextLagnaMillis: Long = 0L
)
