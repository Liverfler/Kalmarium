package com.example.kalmarium.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TermekDao {

    @Insert
    suspend fun insert(termek: TermekEntity)

    @Update
    suspend fun update(termek: TermekEntity)

    @Delete
    suspend fun delete(termek: TermekEntity)

    @Query("SELECT * FROM termek ORDER BY sorrend ASC")
    fun getAll(): Flow<List<TermekEntity>>

    @Query("SELECT * FROM termek ORDER BY sorrend ASC")
    suspend fun getAllOnce(): List<TermekEntity>

    @Query("SELECT * FROM termek WHERE kategoriaId = :kategoriaId ORDER BY sorrend ASC")
    suspend fun getByKategoriaIdOnce(kategoriaId: Int): List<TermekEntity>

    @Query("UPDATE termek SET ar = :ujAr WHERE id = :termekId")
    suspend fun updateAr(termekId: Int, ujAr: Int)

    @Query("UPDATE termek SET kategoria = :ujNev WHERE kategoria = :regiNev")
    suspend fun renameKategoria(regiNev: String, ujNev: String)

    @Query("DELETE FROM termek WHERE kategoria = :kategoriaNev")
    suspend fun deleteByKategoria(kategoriaNev: String)
}
