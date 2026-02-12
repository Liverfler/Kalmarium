package com.example.kalmarium.data.repository

import android.content.Context
import com.example.kalmarium.data.*
import com.example.kalmarium.data.BackupManager

class EladasRepository(
    private val eladasDao: EladasDao,
    private val vasarDao: VasarDao,
    private val termekDao: TermekDao,
    private val context: Context
) {

    suspend fun insert(eladas: EladasEntity) {
        eladasDao.insert(eladas)
        saveBackup()
    }

    fun getOsszesEladottDarab(termekNev: String) =
        eladasDao.getOsszesEladottDarab(termekNev)

    fun getOsszesBevetelTermek(termekNev: String) =
        eladasDao.getOsszesBevetelTermek(termekNev)


    suspend fun delete(eladas: EladasEntity) {
        eladasDao.delete(eladas)
        saveBackup()
    }

    suspend fun deleteAllForVasar(vasarNev: String) {
        eladasDao.deleteAllForVasar(vasarNev)
        saveBackup()
    }

    fun getEladasokVasarhoz(vasarNev: String) =
        eladasDao.getEladasokVasarhoz(vasarNev)

    fun getBevetelForVasar(vasarNev: String) =
        eladasDao.getBevetelForVasar(vasarNev)

    private suspend fun saveBackup() {
        val vasarok = vasarDao.getAllOnce()
        val termekek = termekDao.getAllOnce()
        val eladasok = eladasDao.getAllOnce()

        BackupManager.saveBackup(
            context,
            vasarok,
            termekek,
            eladasok
        )
    }
}
