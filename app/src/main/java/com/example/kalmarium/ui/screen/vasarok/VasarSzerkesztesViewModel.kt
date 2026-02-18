package com.example.kalmarium.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kalmarium.data.VasarEntity
import com.example.kalmarium.data.repository.UserSettingsRepository
import com.example.kalmarium.data.repository.VasarRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class VasarSzerkesztesViewModel(
    private val vasarRepository: VasarRepository,
    private val userSettingsRepository: UserSettingsRepository
) : ViewModel() {

    data class HonapOsszesites(
        val ev: Int,
        val honap: Int,
        val osszBevetel: Int,
        val vasarok: List<VasarEntity>
    )

    val honapok: StateFlow<List<HonapOsszesites>> =
        vasarRepository.getAll()
            .map { list ->
                list
                    .sortedBy { it.datum }
                    .groupBy {
                        val ev = it.datum.substring(0, 4).toInt()
                        val honap = it.datum.substring(5, 7).toInt()
                        ev to honap
                    }
                    .map { (key, vasarok) ->
                        HonapOsszesites(
                            ev = key.first,
                            honap = key.second,
                            osszBevetel = vasarok.sumOf { it.bevetel },
                            vasarok = vasarok.sortedBy { it.datum }
                        )
                    }
                    .sortedWith(compareBy({ it.ev }, { it.honap }))
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun openVasar(vasarId: Int) {
        viewModelScope.launch {
            userSettingsRepository.saveActiveVasarId(vasarId)
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
        }
    }



}
