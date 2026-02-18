package com.example.kalmarium.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_settings")
data class UserSettingsEntity(

    @PrimaryKey
    val id: Int = 1,   // mindig csak 1 sor lesz

    val userName: String = "",

    val activeVasarId: Int? = null,

    val themeColor: Int = 0xFF6200EE.toInt(),

)
