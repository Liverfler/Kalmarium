package com.example.kalmarium.data

import androidx.room.*

@Entity(
    tableName = "termek",
    foreignKeys = [
        ForeignKey(
            entity = KategoriaEntity::class,
            parentColumns = ["id"],
            childColumns = ["kategoriaId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("kategoriaId")]
)
data class TermekEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nev: String,

    val kategoriaId: Int,

    val kategoria: String,

    val ar: Int,

    val sorrend: Int = 0,

    val szin: Long = 0xFF2196F3,

    val keszlet: Int = 0
)
