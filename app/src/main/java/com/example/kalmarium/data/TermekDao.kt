package com.example.kalmarium.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TermekDao {

    @Insert
    suspend fun insert(termek: TermekEntity)

    @Query("SELECT * FROM termek ORDER BY nev ASC")
    fun getAll(): Flow<List<TermekEntity>>

    @Delete
    suspend fun delete(termek: TermekEntity)

    @Query("UPDATE termek SET ar = :ujAr WHERE id = :termekId")
    suspend fun updateAr(termekId: Int, ujAr: Int)

    @Query("UPDATE termek SET kategoria = :ujNev WHERE kategoria = :regiNev")
    suspend fun renameKategoria(regiNev: String, ujNev: String)

    @Query("DELETE FROM termek WHERE kategoria = :kategoriaNev")
    suspend fun deleteByKategoria(kategoriaNev: String)

    @Query("SELECT * FROM termek")
    suspend fun getAllOnce(): List<TermekEntity>



}
