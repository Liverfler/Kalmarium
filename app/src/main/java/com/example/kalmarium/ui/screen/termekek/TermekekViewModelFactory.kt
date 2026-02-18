package com.example.kalmarium.ui.screen.termekek

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kalmarium.data.DatabaseProvider
import com.example.kalmarium.data.repository.KategoriaRepository
import com.example.kalmarium.data.repository.TermekRepository

class TermekekViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(TermekekViewModel::class.java)) {

            val db = DatabaseProvider.getDatabase(context)

            val termekRepository = TermekRepository(
                termekDao = db.termekDao(),
                vasarDao = db.vasarDao(),
                eladasDao = db.eladasDao(),
                kategoriaDao = db.kategoriaDao(),
                context = context
            )

            val kategoriaRepository = KategoriaRepository(
                db.kategoriaDao()
            )

            return TermekekViewModel(
                termekRepository = termekRepository,
                kategoriaRepository = kategoriaRepository
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
