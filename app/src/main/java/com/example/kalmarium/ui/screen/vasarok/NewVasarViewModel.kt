package com.example.kalmarium.ui.screen.vasarok

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kalmarium.data.VasarEntity
import com.example.kalmarium.data.repository.VasarRepository
import kotlinx.coroutines.launch

class NewVasarViewModel(
    private val vasarRepository: VasarRepository
) : ViewModel() {

    fun insertVasar(vasar: VasarEntity) {
        viewModelScope.launch {
            vasarRepository.insertVasar(vasar)
        }
    }
}
