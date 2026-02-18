package com.example.kalmarium.ui.screen.termekek

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kalmarium.data.DatabaseProvider
import com.example.kalmarium.data.repository.TermekRepository

class KeszletViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        val db = DatabaseProvider.getDatabase(context)

        val repository = TermekRepository(
            termekDao = db.termekDao(),
            vasarDao = db.vasarDao(),
            eladasDao = db.eladasDao(),
            kategoriaDao = db.kategoriaDao(),
            context = context
        )

        return KeszletViewModel(repository) as T
    }
}
