package com.example.kalmarium.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface KategoriaDao {

    @Insert
    suspend fun insert(kategoria: KategoriaEntity)

    @Update
    suspend fun update(kategoria: KategoriaEntity)

    @Delete
    suspend fun delete(kategoria: KategoriaEntity)

    // 🔥 MINDEN sorrend szerint
    @Query("SELECT * FROM kategoria ORDER BY sorrend ASC")
    fun getAll(): Flow<List<KategoriaEntity>>

    @Query("SELECT * FROM kategoria ORDER BY sorrend ASC")
    suspend fun getAllOnce(): List<KategoriaEntity>

    @Transaction
    @Query("SELECT * FROM kategoria ORDER BY sorrend ASC")
    fun getKategoriakWithTermekek():
            Flow<List<KategoriaWithTermekek>>

    @Transaction
    @Query("SELECT * FROM kategoria ORDER BY sorrend ASC")
    fun getWithTermekek():
            Flow<List<KategoriaWithTermekek>>
}
