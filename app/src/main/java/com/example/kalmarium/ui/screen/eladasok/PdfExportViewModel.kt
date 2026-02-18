package com.example.kalmarium.ui.screen.eladasok

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kalmarium.data.EladasEntity
import com.example.kalmarium.data.VasarEntity
import com.example.kalmarium.data.repository.EladasRepository
import com.example.kalmarium.data.repository.VasarRepository
import com.example.kalmarium.utils.PdfExportManager
import kotlinx.coroutines.launch
import java.util.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import android.net.Uri

class PdfExportViewModel(
    private val eladasRepository: EladasRepository
) : ViewModel() {

    private val _exportedUri = MutableStateFlow<Uri?>(null)
    val exportedUri: StateFlow<Uri?> = _exportedUri

    fun exportAktivVasar(
        context: Context,
        vasar: VasarEntity,
        eladasLista: List<EladasEntity>,
        bevetel: Int,
        koltseg: Int
    ) {
        viewModelScope.launch {
            val uri = PdfExportManager.exportVasarToPdf(
                context = context,
                vasarNev = vasar.nev,
                datum = vasar.datum,
                hely = vasar.hely,
                bevetel = bevetel,
                koltseg = koltseg,
                eladasLista = eladasLista
            )

            _exportedUri.value = uri
        }
    }
}


