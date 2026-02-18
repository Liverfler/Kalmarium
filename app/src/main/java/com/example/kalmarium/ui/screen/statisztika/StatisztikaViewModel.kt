package com.example.kalmarium.ui.screen.statisztika

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kalmarium.data.EladasEntity
import com.example.kalmarium.data.VasarEntity
import com.example.kalmarium.data.repository.EladasRepository
import com.example.kalmarium.data.repository.UserSettingsRepository
import com.example.kalmarium.data.repository.VasarRepository
import kotlinx.coroutines.flow.*

data class StatisztikaUiState(
    val activeVasar: VasarEntity? = null,
    val activeEladasok: List<EladasEntity> = emptyList(),
    val termekStat: List<TermekStatUi> = emptyList(),
    val vasarStat: List<VasarStatUi> = emptyList(),
    val fromDate: Long? = null,
    val toDate: Long? = null
)

class StatisztikaViewModel(
    vasarRepository: VasarRepository,
    eladasRepository: EladasRepository,
    userSettingsRepository: UserSettingsRepository
) : ViewModel() {

    private val _fromDate = MutableStateFlow<Long?>(null)
    private val _toDate = MutableStateFlow<Long?>(null)



    fun setFromDate(value: Long?) { _fromDate.value = value }
    fun setToDate(value: Long?) { _toDate.value = value }

    val uiState = combine(
        vasarRepository.getAll(),
        eladasRepository.getAll(),
        userSettingsRepository.observeSettings(),
        _fromDate,
        _toDate
    ) { vasarLista, eladasLista, settings, from, to ->

        // ===============================
        // AKTÍV VÁSÁR (NEM DÁTUMOZOTT!)
        // ===============================

        val legelsoDatum =
            eladasLista.minOfOrNull { it.timestamp }

        val legutolsoDatum =
            eladasLista.maxOfOrNull { it.timestamp }

        val effectiveFrom = from ?: legelsoDatum
        val effectiveTo = to ?: legutolsoDatum


        val activeVasar =
            settings?.activeVasarId?.let { id ->
                vasarLista.find { it.id == id }
            } ?: vasarLista.firstOrNull()

        val activeEladasok =
            activeVasar?.let { vasar ->
                eladasLista.filter { it.vasarId == vasar.id }
            } ?: emptyList()


        // ===============================
        // DÁTUM SZŰRÉS (CSAK STATISZTIKÁHOZ)
        // ===============================

        val szurtEladasok =
            eladasLista.filter { eladas ->

                val megfelelFrom =
                    effectiveFrom?.let { eladas.timestamp >= it } ?: true

                val megfelelTo =
                    effectiveTo?.let { eladas.timestamp <= it } ?: true

                megfelelFrom && megfelelTo
            }



        // ===============================
        // NÉPSZERŰ TERMÉKEK
        // ===============================

        val termekStat =
            szurtEladasok
                .groupBy { it.termekNev to it.kategoria }
                .map { (key, lista) ->

            val (nev, kategoria) = key

            TermekStatUi(
                termekNev = nev,
                kategoria = kategoria,
                osszesDarab = lista.sumOf { it.mennyiseg },
                osszesBevetel = lista.sumOf { it.mennyiseg * it.eladasiAr }
            )
        }
        .sortedByDescending { it.osszesDarab }


        // ===============================
        // NÉPSZERŰ VÁSÁROK
        // ===============================

        val vasarStat =
            szurtEladasok
                .groupBy { it.vasarId }
                .mapNotNull { (vasarId, lista) ->

                    val vasar = vasarLista.find { it.id == vasarId }
                    vasar?.let {
                        VasarStatUi(
                            vasarId = it.id,
                            nev = it.nev,
                            hely = it.hely,
                            datum = it.datum,
                            bevetel = lista.sumOf { el -> el.eladasiAr * el.mennyiseg }
                        )
                    }
                }
                .sortedByDescending { it.bevetel }


        StatisztikaUiState(
            activeVasar = activeVasar,
            activeEladasok = activeEladasok,
            termekStat = termekStat,
            vasarStat = vasarStat,
            fromDate = effectiveFrom,
            toDate = effectiveTo

        )

    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        StatisztikaUiState()
    )

}
