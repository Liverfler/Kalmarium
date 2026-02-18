package com.example.kalmarium.data.repository

import android.content.Context
import com.example.kalmarium.data.DatabaseProvider

object RepositoryProvider {

    fun provideTermekRepository(context: Context): TermekRepository {
        val db = DatabaseProvider.getDatabase(context)

        return TermekRepository(
            termekDao = db.termekDao(),
            vasarDao = db.vasarDao(),
            eladasDao = db.eladasDao(),
            kategoriaDao = db.kategoriaDao(),
            context = context
        )
    }

    fun provideVasarRepository(context: Context): VasarRepository {
        val db = DatabaseProvider.getDatabase(context)

        return VasarRepository(
            vasarDao = db.vasarDao()
        )
    }

    fun provideEladasRepository(context: Context): EladasRepository {
        val db = DatabaseProvider.getDatabase(context)

        return EladasRepository(
            eladasDao = db.eladasDao()
        )
    }

    fun provideKategoriaRepository(context: Context): KategoriaRepository {
        val db = DatabaseProvider.getDatabase(context)

        return KategoriaRepository(
            kategoriaDao = db.kategoriaDao()
        )
    }

    fun provideUserSettingsRepository(context: Context): UserSettingsRepository {
        val db = DatabaseProvider.getDatabase(context)

        return UserSettingsRepository(
            dao = db.userSettingsDao()
        )
    }
}
