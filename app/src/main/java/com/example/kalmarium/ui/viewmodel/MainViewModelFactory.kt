package com.example.kalmarium.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kalmarium.data.repository.*

class MainViewModelFactory(
    private val vasarRepository: VasarRepository,
    private val termekRepository: TermekRepository,
    private val eladasRepository: EladasRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(
            vasarRepository,
            termekRepository,
            eladasRepository
        ) as T
    }
}
