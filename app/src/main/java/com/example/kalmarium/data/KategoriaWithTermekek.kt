package com.example.kalmarium.data

import androidx.room.Embedded
import androidx.room.Relation

data class KategoriaWithTermekek(

    @Embedded
    val kategoria: KategoriaEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "kategoriaId"
    )
    val termekek: List<TermekEntity>
)
