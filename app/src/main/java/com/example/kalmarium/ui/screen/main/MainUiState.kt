package com.example.kalmarium.ui.screen.main

import com.example.kalmarium.data.EladasEntity
import com.example.kalmarium.data.VasarEntity

data class MainUiState(
    val activeVasar: VasarEntity? = null,
    val vasarLista: List<VasarEntity> = emptyList(),
    val eladasokAktiv: List<EladasEntity> = emptyList(),
    val bevetel: Int = 0

)
