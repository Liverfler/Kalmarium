package com.example.kalmarium.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EladasDao {

    @Insert
    suspend fun insert(eladas: EladasEntity)

    @Query("""
        SELECT IFNULL(SUM(eladasiAr * mennyiseg), 0)
        FROM eladas
        WHERE vasarId = :vasarId
    """)
    fun getBevetelForVasar(vasarId: Int): Flow<Int>

    @Query("""
        SELECT IFNULL(SUM(mennyiseg), 0)
        FROM eladas
        WHERE termekNev = :termekNev
    """)
    fun getOsszesEladottDarab(termekNev: String): Flow<Int>

    @Query("""
        SELECT IFNULL(SUM(eladasiAr * mennyiseg), 0)
        FROM eladas
        WHERE termekNev = :termekNev
    """)
    fun getOsszesBevetelTermek(termekNev: String): Flow<Int>

    @Query("SELECT * FROM eladas WHERE vasarId = :vasarId")
    fun getEladasokVasarhoz(vasarId: Int): Flow<List<EladasEntity>>

    @Query("SELECT * FROM eladas")
    fun getAll(): Flow<List<EladasEntity>>

    @Query("SELECT * FROM eladas")
    suspend fun getAllOnce(): List<EladasEntity>

    @Delete
    suspend fun delete(eladas: EladasEntity)

    @Query("DELETE FROM eladas WHERE vasarId = :vasarId")
    suspend fun deleteAllForVasar(vasarId: Int)
}

