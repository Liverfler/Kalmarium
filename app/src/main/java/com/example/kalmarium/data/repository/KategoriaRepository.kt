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


    suspend fun insert(kategoria: KategoriaEntity) {
        kategoriaDao.insert(kategoria)
    }

    suspend fun delete(kategoria: KategoriaEntity) {
        kategoriaDao.delete(kategoria)
    }

    suspend fun update(kategoria: KategoriaEntity) {
        kategoriaDao.update(kategoria)
    }


}
