package com.example.kalmarium.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kalmarium.data.repository.VasarRepository
import com.example.kalmarium.data.repository.EladasRepository
import com.example.kalmarium.data.repository.UserSettingsRepository


class MainViewModelFactory(
    private val vasarRepository: VasarRepository,
    private val eladasRepository: EladasRepository,
    private val userSettingsRepository: UserSettingsRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(
            vasarRepository,
            eladasRepository,
            userSettingsRepository
        ) as T
    }
}

