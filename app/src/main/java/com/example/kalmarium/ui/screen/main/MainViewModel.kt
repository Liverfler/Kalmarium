package com.example.kalmarium.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kalmarium.data.VasarEntity
import com.example.kalmarium.data.repository.EladasRepository
import com.example.kalmarium.data.repository.VasarRepository
import com.example.kalmarium.data.repository.UserSettingsRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(
    private val vasarRepository: VasarRepository,
    private val eladasRepository: EladasRepository,
    private val userSettingsRepository: UserSettingsRepository
) : ViewModel() {

    private val vasarFlow = vasarRepository.getAll()
    private val eladasFlow = eladasRepository.getAll()
    private val settingsFlow = userSettingsRepository.observeSettings()


    val uiState: StateFlow<MainUiState> =
        combine(
            vasarFlow,
            eladasFlow,
            settingsFlow
        ) { vasarLista, eladasLista, settings ->

            val activeVasar =
                settings?.activeVasarId?.let { id ->
                    vasarLista.find { it.id == id }
                } ?: vasarLista.firstOrNull()

            val eladasokAktiv =
                if (activeVasar != null)
                    eladasLista.filter { it.vasarId == activeVasar.id }

                else emptyList()


            val bevetel =
                eladasokAktiv.sumOf { it.eladasiAr * it.mennyiseg }


            if (activeVasar != null && activeVasar.bevetel != bevetel) {
                viewModelScope.launch {
                    vasarRepository.updateVasar(
                        activeVasar.copy(bevetel = bevetel)
                    )
                }
            }



            MainUiState(
                activeVasar = activeVasar,
                vasarLista = vasarLista,
                eladasokAktiv = eladasokAktiv,
                bevetel = bevetel
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            MainUiState()
        )

    fun setActiveVasar(vasar: VasarEntity) {
        viewModelScope.launch {
            userSettingsRepository.saveActiveVasarId(vasar.id)
        }
    }


    fun createNewVasar(vasar: VasarEntity) {
        viewModelScope.launch {
            val newId = vasarRepository.insertVasar(vasar)
            userSettingsRepository.saveActiveVasarId(newId.toInt())
        }
    }

    fun updateVasar(vasar: VasarEntity) {
        viewModelScope.launch {
            vasarRepository.updateVasar(vasar)
        }
    }

    fun deleteVasar(vasar: VasarEntity) {
        viewModelScope.launch {

            vasarRepository.deleteVasar(vasar)

            val current = userSettingsRepository.getSettingsOnce()

            if (current?.activeVasarId == vasar.id) {
                userSettingsRepository.saveActiveVasarId(null)
            }
        }
    }



}
