package com.example.kalmarium.ui.screen.statisztika

data class TermekStatUi(
    val termekNev: String,
    val kategoria: String,
    val osszesDarab: Int,
    val osszesBevetel: Int
)

data class VasarStatUi(
    val vasarId: Int,
    val nev: String,
    val hely: String,
    val datum: String,
    val bevetel: Int
)
