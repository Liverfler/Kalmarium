package com.example.kalmarium.data.repository

import com.example.kalmarium.data.VasarDao
import com.example.kalmarium.data.VasarEntity
import kotlinx.coroutines.flow.Flow

class VasarRepository(
    private val vasarDao: VasarDao
) {

    // =============================
    // INSERT
    // =============================
    suspend fun insertVasar(vasar: VasarEntity): Long {
        return vasarDao.insert(vasar)
    }

    // =============================
    // DELETE
    // =============================
    suspend fun deleteVasar(vasar: VasarEntity) {
        vasarDao.delete(vasar)
    }

    // =============================
    // UPDATE
    // =============================
    suspend fun updateVasar(vasar: VasarEntity) {
        vasarDao.update(vasar)
    }

    // =============================
    // GET
    // =============================
    fun getAll(): Flow<List<VasarEntity>> {
        return vasarDao.getAll()
    }

    fun getLatestFive(): Flow<List<VasarEntity>> {
        return vasarDao.getLatestFive()
    }

    suspend fun getAllOnce(): List<VasarEntity> {
        return vasarDao.getAllOnce()
    }

    fun getVasarByNev(nev: String): Flow<VasarEntity?> {
        return vasarDao.getVasarByNev(nev)
    }



        // =============================
        // Archiválás (Befejezés) frissítése
        // =============================
        suspend fun archiveVasar(vasarId: Int) {
            vasarDao.updateVasarStatus(vasarId, true)
        }

        // =============================
        // Vásár állapotának visszaállítása
        // =============================
        suspend fun unarchiveVasar(vasarId: Int) {
            vasarDao.updateVasarStatus(vasarId, false)


    }

}
