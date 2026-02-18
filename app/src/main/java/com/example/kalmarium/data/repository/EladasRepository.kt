package com.example.kalmarium.data.repository

import com.example.kalmarium.data.EladasDao
import com.example.kalmarium.data.EladasEntity
import kotlinx.coroutines.flow.Flow

class EladasRepository(
    private val eladasDao: EladasDao
) {

    // =========================
    // INSERT
    // =========================

    suspend fun insert(eladas: EladasEntity) {
        eladasDao.insert(eladas)
    }

    // =========================
    // DELETE
    // =========================

    suspend fun delete(eladas: EladasEntity) {
        eladasDao.delete(eladas)
    }

    suspend fun deleteAllForVasar(vasarId: Int) {
        eladasDao.deleteAllForVasar(vasarId)
    }

    // =========================
    // GET
    // =========================

    fun getAll(): Flow<List<EladasEntity>> {
        return eladasDao.getAll()
    }

    fun getEladasokVasarhoz(vasarId: Int): Flow<List<EladasEntity>> {
        return eladasDao.getEladasokVasarhoz(vasarId)
    }

    fun getBevetelForVasar(vasarId: Int): Flow<Int> {
        return eladasDao.getBevetelForVasar(vasarId)
    }

    fun getOsszesEladottDarab(termekNev: String): Flow<Int> {
        return eladasDao.getOsszesEladottDarab(termekNev)
    }

    fun getOsszesBevetelTermek(termekNev: String): Flow<Int> {
        return eladasDao.getOsszesBevetelTermek(termekNev)
    }




    suspend fun getAllOnce(): List<EladasEntity> {
        return eladasDao.getAllOnce()
    }
}
