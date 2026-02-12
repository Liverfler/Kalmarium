package com.example.kalmarium.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        VasarEntity::class,
        TermekEntity::class,
        EladasEntity::class
    ],
    version = 7,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun vasarDao(): VasarDao
    abstract fun termekDao(): TermekDao
    abstract fun eladasDao(): EladasDao
}
