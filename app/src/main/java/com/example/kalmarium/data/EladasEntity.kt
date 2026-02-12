package com.example.kalmarium.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "eladas")
data class EladasEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val vasarNev: String = "",
    val termekNev: String = "",
    val kategoria: String = "",
    val mennyiseg: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)
