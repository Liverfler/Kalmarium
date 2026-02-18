package com.example.kalmarium.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface VasarDao {

    @Insert
    suspend fun insert(vasar: VasarEntity): Long

    @Delete
    suspend fun delete(vasar: VasarEntity)

    @Query("SELECT * FROM vasar ORDER BY id DESC")
    fun getAll(): Flow<List<VasarEntity>>

    @Query("SELECT * FROM vasar ORDER BY datum DESC LIMIT 5")
    fun getLatestFive(): Flow<List<VasarEntity>>

    @Query("SELECT * FROM vasar")
    suspend fun getAllOnce(): List<VasarEntity>

    @Update
    suspend fun update(vasar: VasarEntity)

    @Query("SELECT * FROM vasar WHERE nev = :nev LIMIT 1")
    fun getVasarByNev(nev: String): Flow<VasarEntity?>



    @Query("UPDATE vasar SET isArchived = :status WHERE id = :vasarId")
    suspend fun updateVasarStatus(vasarId: Int, status: Boolean)





}
