package com.example.kalmarium.data.repository

import android.content.Context
import com.example.kalmarium.data.*

class TermekRepository(
    private val termekDao: TermekDao,
    private val vasarDao: VasarDao,
    private val eladasDao: EladasDao,
    private val context: Context
) {

    // ===== INSERT =====
    suspend fun insert(termek: TermekEntity) {
        termekDao.insert(termek)
        saveBackup()
    }

    // ===== GET ALL =====
    fun getAll() = termekDao.getAll()

    // ===== UPDATE =====
    suspend fun updateAr(id: Int, ujAr: Int) {
        termekDao.updateAr(id, ujAr)
        saveBackup()
    }

    suspend fun renameKategoria(regi: String, uj: String) {
        termekDao.renameKategoria(regi, uj)
        saveBackup()
    }

    suspend fun deleteByKategoria(kategoria: String) {
        termekDao.deleteByKategoria(kategoria)
        saveBackup()
    }

    // ===== BACKUP =====
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
