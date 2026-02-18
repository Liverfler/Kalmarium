package com.example.kalmarium.data.repository

import com.example.kalmarium.data.UserSettingsDao
import com.example.kalmarium.data.UserSettingsEntity
import kotlinx.coroutines.flow.Flow

class UserSettingsRepository(
    private val dao: UserSettingsDao
) {

    fun observeSettings(): Flow<UserSettingsEntity?> {
        return dao.observeSettings()
    }

    suspend fun saveUserName(name: String) {
        val current = dao.getSettingsOnce()

        dao.save(
            UserSettingsEntity(
                id = 1,
                userName = name,
                activeVasarId = current?.activeVasarId,
                themeColor = current?.themeColor ?: 0xFF2196F3.toInt()
            )
        )
    }

    suspend fun saveActiveVasarId(vasarId: Int?) {
        val current = dao.getSettingsOnce()

        dao.save(
            UserSettingsEntity(
                id = 1,
                userName = current?.userName ?: "",
                activeVasarId = vasarId,
                themeColor = current?.themeColor ?: 0xFF2196F3.toInt()
            )
        )
    }

    suspend fun saveThemeColor(color: Int) {
        val current = dao.getSettingsOnce()

        dao.save(
            UserSettingsEntity(
                id = 1,
                userName = current?.userName ?: "",
                activeVasarId = current?.activeVasarId,
                themeColor = color
            )
        )
    }

    suspend fun getSettingsOnce(): UserSettingsEntity? {
        return dao.getSettingsOnce()
    }

}
