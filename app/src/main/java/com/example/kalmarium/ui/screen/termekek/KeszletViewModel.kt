package com.example.kalmarium.ui.screen.termekek

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kalmarium.data.TermekEntity
import com.example.kalmarium.data.repository.TermekRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class KeszletViewModel(
    private val repository: TermekRepository
) : ViewModel() {

    val termekek = repository.getAll()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    val kategoriak = termekek
        .map { lista ->
            lista.map { it.kategoria }.distinct().sorted()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun saveAll(updatedList: List<TermekEntity>) {
        viewModelScope.launch {
            updatedList.forEach {
                repository.update(it)
            }
        }
    }
}
