package com.example.kalmarium.data.repository

import android.content.Context
import com.example.kalmarium.data.*
import com.example.kalmarium.data.BackupManager

class VasarRepository(
    private val vasarDao: VasarDao,
    private val termekDao: TermekDao,
    private val eladasDao: EladasDao,
    private val context: Context
) {

    suspend fun insertVasar(vasar: VasarEntity) {
        vasarDao.insert(vasar)
        saveBackup()
    }

    suspend fun deleteVasar(vasar: VasarEntity) {
        vasarDao.delete(vasar)
        saveBackup()
    }

    fun getLatestFive() = vasarDao.getLatestFive()

    suspend fun autoRestoreIfNeeded() {

        val vasarok = vasarDao.getAllOnce()

        if (vasarok.isEmpty() &&
            BackupManager.backupExists(context)
        ) {
            BackupManager.restoreBackup(context)?.let { data ->

                data.vasarok.orEmpty().forEach {
                    vasarDao.insert(it.copy(id = 0))
                }

                data.termekek.orEmpty().forEach {
                    termekDao.insert(it.copy(id = 0))
                }

                data.eladasok.orEmpty().forEach {
                    eladasDao.insert(it.copy(id = 0))
                }
            }
        }
    }


    private suspend fun saveBackup() {
        val vasarok = vasarDao.getAllOnce()
        val termekek = termekDao.getAllOnce()
        val eladasok = eladasDao.getAllOnce()

        BackupManager.saveBackup(
            context,
            vasarok,
            termekek,
            eladasok
        )
    }
}
