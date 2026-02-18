package com.example.kalmarium.ui.screen.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kalmarium.data.repository.UserSettingsRepository
import com.example.kalmarium.ui.theme.AppThemeType
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val repository: UserSettingsRepository
) : ViewModel() {

    var currentTheme by mutableStateOf(AppThemeType.BLUE)
        private set

    var userName by mutableStateOf("")
        private set

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            val settings = repository.getSettingsOnce()

            settings?.let {
                userName = it.userName
                currentTheme = AppThemeType.fromColor(it.themeColor)
            }
        }
    }

    fun changeTheme(theme: AppThemeType) {
        currentTheme = theme

        viewModelScope.launch {
            repository.saveThemeColor(theme.primaryColor)
        }
    }

    fun changeUserName(name: String) {
        userName = name

        viewModelScope.launch {
            repository.saveUserName(name)
        }
    }
}
