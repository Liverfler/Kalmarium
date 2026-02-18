package com.example.kalmarium.ui.screen.termekek

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kalmarium.data.KategoriaEntity
import com.example.kalmarium.data.TermekEntity
import com.example.kalmarium.data.repository.KategoriaRepository
import com.example.kalmarium.data.repository.TermekRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TermekekViewModel(
    private val termekRepository: TermekRepository,
    private val kategoriaRepository: KategoriaRepository
) : ViewModel() {

    // =========================
    // LISTÁK
    // =========================

    val termekek: StateFlow<List<TermekEntity>> =
        termekRepository.getAll()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    val kategoriak: StateFlow<List<KategoriaEntity>> =
        kategoriaRepository.getAll()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    // =========================
    // INSERT
    // =========================

    fun insertKategoria(kategoria: KategoriaEntity) {
        viewModelScope.launch {
            kategoriaRepository.insert(kategoria)
        }
    }

    fun insertTermek(termek: TermekEntity) {
        viewModelScope.launch {
            termekRepository.insert(termek)
        }
    }

    // =========================
    // UPDATE
    // =========================

    fun updateKategoria(kategoria: KategoriaEntity) {
        viewModelScope.launch {
            kategoriaRepository.update(kategoria)
        }
    }

    fun updateTermek(termek: TermekEntity) {
        viewModelScope.launch {
            termekRepository.update(termek)
        }
    }

    // =========================
    // DELETE
    // =========================

    fun deleteKategoria(kategoria: KategoriaEntity) {
        viewModelScope.launch {
            kategoriaRepository.delete(kategoria)
        }
    }

    fun deleteTermek(termek: TermekEntity) {
        viewModelScope.launch {
            termekRepository.delete(termek)
        }
    }

    // =========================
    // KATEGÓRIA SORREND
    // =========================

    fun reorderKategoriak(from: Int, to: Int) {
        viewModelScope.launch {

            val current = kategoriak.value
                .sortedBy { it.sorrend }
                .toMutableList()

            if (from !in current.indices || to !in current.indices)
                return@launch

            val item = current.removeAt(from)
            current.add(to, item)

            termekRepository.updateKategoriakOrder(current)
        }
    }

    // =========================
    // TERMÉK SORREND
    // =========================

    fun reorderTermekek(
        kategoriaId: Int,
        from: Int,
        to: Int
    ) {
        viewModelScope.launch {

            val current = termekek.value
                .filter { it.kategoriaId == kategoriaId }
                .sortedBy { it.sorrend }
                .toMutableList()

            if (from !in current.indices || to !in current.indices)
                return@launch

            val item = current.removeAt(from)
            current.add(to, item)

            termekRepository.updateTermekOrder(current)
        }
    }
}
