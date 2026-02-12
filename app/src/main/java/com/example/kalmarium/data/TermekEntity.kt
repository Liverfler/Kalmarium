package com.example.kalmarium.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "termek")
data class TermekEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nev: String = "",
    val kategoria: String = "",
    val ar: Int = 0,
)
