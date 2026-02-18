package com.example.kalmarium.ui



import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable

import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.kalmarium.ui.theme.KalmariumTheme
import com.example.kalmarium.data.repository.RepositoryProvider
import com.example.kalmarium.ui.navigation.AppNavigation
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Modifier
import com.example.kalmarium.R
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kalmarium.ui.screen.settings.SettingsViewModel
import com.example.kalmarium.ui.screen.settings.SettingsViewModelFactory



@Composable
fun AppRoot() {

    val context = LocalContext.current

    val vasarRepository = remember {
        RepositoryProvider.provideVasarRepository(context)
    }

    val termekRepository = remember {
        RepositoryProvider.provideTermekRepository(context)
    }

    val eladasRepository = remember {
        RepositoryProvider.provideEladasRepository(context)
    }

    val kategoriaRepository = remember {
        RepositoryProvider.provideKategoriaRepository(context)
    }

    val userSettingsRepository = remember {
        RepositoryProvider.provideUserSettingsRepository(context)
    }
    val settingsViewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModelFactory(userSettingsRepository)
    )





    val navController = rememberNavController()


    KalmariumTheme(
        themeType = settingsViewModel.currentTheme
    ) {


        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            // 🔵 Háttérkép
            Image(
                painter = painterResource(id = R.drawable.background),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.15f   // <-- halványítás mértéke
            )

            // 🔵 App tartalom
            AppNavigation(
                navController = navController,
                vasarRepository = vasarRepository,
                termekRepository = termekRepository,
                eladasRepository = eladasRepository,
                kategoriaRepository = kategoriaRepository,
                userSettingsRepository = userSettingsRepository,
                settingsViewModel = settingsViewModel
            )
        }
    }




    /*
    KalmariumTheme {
        AppNavigation(
            navController = navController,
            vasarRepository = vasarRepository,
            termekRepository = termekRepository,
            eladasRepository = eladasRepository,
            kategoriaRepository = kategoriaRepository,
            userSettingsRepository = userSettingsRepository
        )
    }
*/
}


