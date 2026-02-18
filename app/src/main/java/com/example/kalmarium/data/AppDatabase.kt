package com.example.kalmarium.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        VasarEntity::class,
        TermekEntity::class,
        EladasEntity::class,
        KategoriaEntity::class,
        UserSettingsEntity::class
    ],
    version = 4
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun vasarDao(): VasarDao
    abstract fun termekDao(): TermekDao
    abstract fun eladasDao(): EladasDao
    abstract fun kategoriaDao(): KategoriaDao
    abstract fun userSettingsDao(): UserSettingsDao
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "ALTER TABLE termek ADD COLUMN keszlet INTEGER NOT NULL DEFAULT 0"
        )
    }
}

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "ALTER TABLE vasar ADD COLUMN bevetel INTEGER NOT NULL DEFAULT 0"
        )
    }
}
