package com.example.kalmarium.ui.screen.eladasok

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kalmarium.data.repository.EladasRepository
import com.example.kalmarium.data.repository.VasarRepository

class PdfExportViewModelFactory(
    private val eladasRepository: EladasRepository,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(PdfExportViewModel::class.java)) {
            return PdfExportViewModel(
                eladasRepository
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
