package com.example.kalmarium.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface VasarDao {

    @Insert
    suspend fun insert(vasar: VasarEntity)

    @Delete
    suspend fun delete(vasar: VasarEntity)

    @Query("SELECT * FROM vasar ORDER BY id DESC")
    fun getAll(): Flow<List<VasarEntity>>

    @Query("SELECT * FROM vasar ORDER BY id DESC LIMIT 5")
    fun getLatestFive(): Flow<List<VasarEntity>>

    @Query("SELECT * FROM vasar")
    suspend fun getAllOnce(): List<VasarEntity>

}
