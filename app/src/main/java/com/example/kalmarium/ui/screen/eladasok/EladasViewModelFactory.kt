package com.example.kalmarium.ui.screen.eladasok

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kalmarium.data.repository.EladasRepository
import com.example.kalmarium.data.repository.KategoriaRepository
import com.example.kalmarium.data.repository.TermekRepository
import com.example.kalmarium.data.repository.VasarRepository

class EladasViewModelFactory(
    private val vasarId: Int,
    private val termekRepository: TermekRepository,
    private val eladasRepository: EladasRepository,
    private val kategoriaRepository: KategoriaRepository,
    private val vasarRepository: VasarRepository

) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(EladasViewModel::class.java)) {
            return EladasViewModel(
                vasarId,
                termekRepository = termekRepository,
                eladasRepository = eladasRepository,
                kategoriaRepository = kategoriaRepository,
                vasarRepository = vasarRepository
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
