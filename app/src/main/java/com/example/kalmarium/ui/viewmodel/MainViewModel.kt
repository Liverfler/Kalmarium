package com.example.kalmarium.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kalmarium.data.*
import com.example.kalmarium.data.repository.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(
    private val vasarRepository: VasarRepository,
    private val termekRepository: TermekRepository,
    private val eladasRepository: EladasRepository
) : ViewModel() {




    // --------- LISTÁK ---------

    val latestVasarList = vasarRepository.getLatestFive()

    val termekList = termekRepository.getAll()

    // --------- SELECTED TERMÉK (STATE) ---------

    private val _selectedTermek = MutableStateFlow<TermekEntity?>(null)
    val selectedTermek: StateFlow<TermekEntity?> = _selectedTermek



    // --------- TERMÉK STATISZTIKA (AUTOMATIKUSAN FRISSÜL) ---------

    val eladottDarab: StateFlow<Int> =
        _selectedTermek
            .filterNotNull()
            .flatMapLatest { termek ->
                eladasRepository.getOsszesEladottDarab(termek.nev)
            }
            .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    val osszesBevetel: StateFlow<Int> =
        _selectedTermek
            .filterNotNull()
            .flatMapLatest { termek ->
                eladasRepository.getOsszesBevetelTermek(termek.nev)
            }
            .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    // --------- VÁSÁR ---------

    fun insertVasar(vasar: VasarEntity) {
        viewModelScope.launch {
            vasarRepository.insertVasar(vasar)
        }
    }

    fun deleteVasar(vasar: VasarEntity) {
        viewModelScope.launch {
            vasarRepository.deleteVasar(vasar)
        }
    }

    // --------- TERMÉK ---------

    fun insertTermek(termek: TermekEntity) {
        viewModelScope.launch {
            termekRepository.insert(termek)
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


    fun deleteKategoria(kategoria: String) {
        viewModelScope.launch {
            termekRepository.deleteByKategoria(kategoria)
        }
    }

    // --------- ELADÁS ---------

    fun insertEladas(eladas: EladasEntity) {
        viewModelScope.launch {
            eladasRepository.insert(eladas)
        }
    }

    fun deleteEladas(eladas: EladasEntity) {
        viewModelScope.launch {
            eladasRepository.delete(eladas)
        }
    }

    fun deleteAllForVasar(vasarNev: String) {
        viewModelScope.launch {
            eladasRepository.deleteAllForVasar(vasarNev)
        }
    }

    fun getEladasokVasarhoz(vasarNev: String) =
        eladasRepository.getEladasokVasarhoz(vasarNev)

    fun getBevetelForVasar(vasarNev: String) =
        eladasRepository.getBevetelForVasar(vasarNev)

    fun autoRestoreIfNeeded() {
        viewModelScope.launch {
            vasarRepository.autoRestoreIfNeeded()
        }
    }

    private val _termekUiState = MutableStateFlow(TermekUiState())
    val termekUiState: StateFlow<TermekUiState> = _termekUiState

    init {
        viewModelScope.launch {
            termekRepository.getAll().collect { lista ->
                _termekUiState.update {
                    it.copy(termekLista = lista)
                }
            }
        }
    }

    fun selectTermek(termek: TermekEntity) {
        _termekUiState.update {
            it.copy(selectedTermek = termek)
        }

        observeTermekStats(termek.nev)
    }

    private fun observeTermekStats(termekNev: String) {
        viewModelScope.launch {
            combine(
                eladasRepository.getOsszesEladottDarab(termekNev),
                eladasRepository.getOsszesBevetelTermek(termekNev)
            ) { darab, bevetel ->
                darab to bevetel
            }.collect { (darab, bevetel) ->
                _termekUiState.update {
                    it.copy(
                        eladottDarab = darab,
                        osszesBevetel = bevetel
                    )
                }
            }
        }
    }

    fun clearSelectedTermek() {
        _termekUiState.update {
            it.copy(
                selectedTermek = null,
                eladottDarab = 0,
                osszesBevetel = 0
            )
        }
    }




}
