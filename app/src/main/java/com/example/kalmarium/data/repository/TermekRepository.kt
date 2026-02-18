package com.example.kalmarium.data.repository

import android.content.Context
import com.example.kalmarium.data.*

class TermekRepository(
    private val termekDao: TermekDao,
    private val vasarDao: VasarDao,
    private val eladasDao: EladasDao,
    private val kategoriaDao: KategoriaDao,
    private val context: Context
) {

    // =========================
    // INSERT
    // =========================
    suspend fun insert(termek: TermekEntity) {
        termekDao.insert(termek)
        saveBackup()
    }

    // =========================
    // DELETE
    // =========================
    suspend fun delete(termek: TermekEntity) {
        termekDao.delete(termek)
        saveBackup()
    }

    // =========================
    // GET
    // =========================
    fun getAll() = termekDao.getAll()

    suspend fun getAllOnce() = termekDao.getAllOnce()

    // =========================
    // UPDATE
    // =========================
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

    // =========================
    // KATEGÓRIA SORREND
    // =========================
    suspend fun updateKategoriakOrder(kategoriak: List<KategoriaEntity>) {

        kategoriak.forEachIndexed { index, kategoria ->
            kategoriaDao.update(
                kategoria.copy(sorrend = index)
            )
        }

        saveBackup()
    }

    // =========================
    // TERMÉK SORREND
    // =========================
    suspend fun updateTermekOrder(termekek: List<TermekEntity>) {

        termekek.forEachIndexed { index, termek ->
            termekDao.update(
                termek.copy(sorrend = index)
            )
        }

        saveBackup()
    }

    // =========================
    // BACKUP
    // =========================
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

    // =========================
    // UPDATE TELJES TERMÉK
    // =========================
    suspend fun update(termek: TermekEntity) {
        termekDao.update(termek)
        saveBackup()
    }

}
