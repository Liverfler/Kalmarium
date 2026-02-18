package com.example.kalmarium.ui.screen.statisztika

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kalmarium.data.repository.EladasRepository
import com.example.kalmarium.data.repository.UserSettingsRepository
import com.example.kalmarium.data.repository.VasarRepository

class StatisztikaViewModelFactory(
    private val vasarRepository: VasarRepository,
    private val eladasRepository: EladasRepository,
    private val userSettingsRepository: UserSettingsRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return StatisztikaViewModel(
            vasarRepository,
            eladasRepository,
            userSettingsRepository
        ) as T
    }
}
