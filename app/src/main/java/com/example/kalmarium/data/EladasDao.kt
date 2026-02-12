package com.example.kalmarium.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EladasDao {

    @Insert
    suspend fun insert(eladas: EladasEntity)

    @Query("SELECT * FROM eladas WHERE vasarNev = :vasarNev")
    fun getByVasar(vasarNev: String): Flow<List<EladasEntity>>

    @Query("""
    SELECT IFNULL(SUM(t.ar * e.mennyiseg), 0)
    FROM eladas e
    INNER JOIN termek t ON e.termekNev = t.nev
    WHERE e.vasarNev = :vasarNev
""")
    fun getBevetelForVasar(vasarNev: String): Flow<Int>

    @Query("""
    SELECT IFNULL(SUM(mennyiseg), 0)
    FROM eladas
    WHERE termekNev = :termekNev
""")
    fun getOsszesEladottDarab(termekNev: String): Flow<Int>


    @Query("""
    SELECT IFNULL(SUM(t.ar * e.mennyiseg), 0)
    FROM eladas e
    INNER JOIN termek t ON e.termekNev = t.nev
    WHERE e.termekNev = :termekNev
""")
    fun getOsszesBevetelTermek(termekNev: String): Flow<Int>

    @Query("SELECT * FROM eladas WHERE vasarNev = :vasarNev")
    fun getEladasokVasarhoz(vasarNev: String): Flow<List<EladasEntity>>

    @Query("SELECT * FROM eladas")
    fun getAll(): Flow<List<EladasEntity>>

    @Query("SELECT * FROM eladas")
    suspend fun getAllOnce(): List<EladasEntity>

    @Delete
    suspend fun delete(eladas: EladasEntity)

    @Query("DELETE FROM eladas WHERE vasarNev = :vasarNev")
    suspend fun deleteAllForVasar(vasarNev: String)




}
