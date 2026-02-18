package com.example.kalmarium.ui.screen.vasarok

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kalmarium.data.repository.VasarRepository

class NewVasarViewModelFactory(
    private val vasarRepository: VasarRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewVasarViewModel(vasarRepository) as T
    }
}
