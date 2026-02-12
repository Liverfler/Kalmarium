package com.example.kalmarium.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kalmarium.data.TermekEntity
import com.example.kalmarium.data.repository.TermekRepository
import com.example.kalmarium.data.repository.EladasRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TermekViewModel(
    private val termekRepository: TermekRepository,
    private val eladasRepository: EladasRepository
) : ViewModel() {

    val termekLista = termekRepository.getAll()

    private val _selectedTermek = MutableStateFlow<TermekEntity?>(null)
    val selectedTermek: StateFlow<TermekEntity?> = _selectedTermek

    val eladottDarab: StateFlow<Int> =
        _selectedTermek
            .flatMapLatest { termek ->
                termek?.let {
                    eladasRepository.getOsszesEladottDarab(it.nev)
                } ?: flowOf(0)
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val osszesBevetel: StateFlow<Int> =
        _selectedTermek
            .flatMapLatest { termek ->
                termek?.let {
                    eladasRepository.getOsszesBevetelTermek(it.nev)
                } ?: flowOf(0)
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun selectTermek(termek: TermekEntity) {
        _selectedTermek.value = termek
    }

    fun clearSelectedTermek() {
        _selectedTermek.value = null
    }

    fun addTermek(nev: String, kategoria: String, ar: Int) {
        viewModelScope.launch {
            termekRepository.insert(
                TermekEntity(
                    nev = nev,
                    kategoria = kategoria,
                    ar = ar
                )
            )

        }
    }

    fun updateAr(id: Int, ujAr: Int) {
        viewModelScope.launch {
            termekRepository.updateAr(id, ujAr)
        }
    }

    fun renameKategoria(regi: String, uj: String) {
        viewModelScope.launch {
            termekRepository.renameKategoria(regi, uj)
        }
    }

    fun deleteKategoria(kategoria: String) {
        viewModelScope.launch {
            termekRepository.deleteByKategoria(kategoria)
        }
    }
}
