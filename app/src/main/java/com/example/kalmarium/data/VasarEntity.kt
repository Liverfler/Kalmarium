package com.example.kalmarium.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vasar")
data class VasarEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nev: String = "",
    val hely: String = "",
    val datum: String = "",
    val koltseg: Int = 0,
    val bevetel: Int = 0,
    val isArchived: Boolean = false
)

