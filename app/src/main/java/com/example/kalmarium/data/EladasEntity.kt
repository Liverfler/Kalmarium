package com.example.kalmarium.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "eladas")
data class EladasEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val vasarId: Int,        // ID alapú kapcsolat

    val termekNev: String,   // SNAPSHOT
    val kategoria: String,   // SNAPSHOT
    val mennyiseg: Int,
    val eladasiAr: Int,
    val timestamp: Long = System.currentTimeMillis()
)


