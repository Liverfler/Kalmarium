package com.example.kalmarium.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kategoria")
data class KategoriaEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nev: String,

    val sorrend: Int = 0,

    // ARGB Hex (pl: 0xFFFF5722)
    val szin: Long = 0xFF9E9E9E
)
