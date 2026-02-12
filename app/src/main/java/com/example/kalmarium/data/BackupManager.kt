package com.example.kalmarium.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.File

object BackupManager {

    private const val FILE_NAME = "batyus_backup.json"

    fun saveBackup(
        context: Context,
        vasarok: List<VasarEntity>,
        termekek: List<TermekEntity>,
        eladasok: List<EladasEntity>
    ) {
        val backup = AppBackup(vasarok, termekek, eladasok)
        val json = Gson().toJson(backup)

        val file = File(context.filesDir, FILE_NAME)
        file.writeText(json)
    }

    fun restoreBackup(context: Context): AppBackup? {
        return try {
            val file = File(context.filesDir, FILE_NAME)
            if (!file.exists()) return null

            val json = file.readText()

            GsonBuilder()
                .serializeNulls()
                .create()
                .fromJson(json, AppBackup::class.java)

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    fun backupExists(context: Context): Boolean {
        val file = File(context.filesDir, FILE_NAME)
        return file.exists()
    }
}
