package com.example.kalmarium.data.repository

import com.example.kalmarium.data.KategoriaDao
import com.example.kalmarium.data.KategoriaEntity
import com.example.kalmarium.data.KategoriaWithTermekek
import kotlinx.coroutines.flow.Flow

class KategoriaRepository(
    private val kategoriaDao: KategoriaDao
) {

    fun getAll(): Flow<List<KategoriaEntity>> {
        return kategoriaDao.getAll()
    }

    fun getWithTermekek(): Flow<List<KategoriaWithTermekek>> {
        return kategoriaDao.getWithTermekek()
    }

    suspend fun insert(kategoria: KategoriaEntity) {
        kategoriaDao.insert(kategoria)
    }

    suspend fun delete(kategoria: KategoriaEntity) {
        kategoriaDao.delete(kategoria)
    }

    suspend fun update(kategoria: KategoriaEntity) {
        kategoriaDao.update(kategoria)
    }

    // ✅ EZ HIÁNYZOTT NÁLAD
    suspend fun updateKategoriaOrder(kategoriak: List<KategoriaEntity>) {

        kategoriak.forEachIndexed { index, kategoria ->
            kategoriaDao.update(
                kategoria.copy(sorrend = index)
            )
        }
    }
}
