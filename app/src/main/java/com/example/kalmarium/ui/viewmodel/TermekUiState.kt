package com.example.kalmarium.ui.viewmodel

import com.example.kalmarium.data.TermekEntity

data class TermekUiState(
    val termekLista: List<TermekEntity> = emptyList(),
    val selectedTermek: TermekEntity? = null,
    val eladottDarab: Int = 0,
    val osszesBevetel: Int = 0
)
