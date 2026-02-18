package com.example.kalmarium.ui.navigation

import VasarSzerkesztesViewModelFactory
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.kalmarium.data.*
import com.example.kalmarium.data.repository.*
import com.example.kalmarium.ui.components.MenuDialog
import com.example.kalmarium.ui.screen.eladasok.*
import com.example.kalmarium.ui.screen.main.*
import com.example.kalmarium.ui.screen.settings.SettingsScreen
import com.example.kalmarium.ui.screen.settings.SettingsViewModel
import com.example.kalmarium.ui.screen.statisztika.StatisztikaScreen
import com.example.kalmarium.ui.screen.statisztika.StatisztikaViewModel
import com.example.kalmarium.ui.screen.statisztika.StatisztikaViewModelFactory
import com.example.kalmarium.ui.screen.termekek.*
import com.example.kalmarium.ui.screen.vasarok.*
import com.example.kalmarium.ui.theme.blackGlow
import com.example.kalmarium.ui.viewmodel.VasarSzerkesztesViewModel
import com.example.kalmarium.utils.SnackbarManager
import kotlinx.coroutines.launch


@Composable
fun AppNavigation(
    navController: NavHostController,
    vasarRepository: VasarRepository,
    termekRepository: TermekRepository,
    eladasRepository: EladasRepository,
    kategoriaRepository: KategoriaRepository,
    userSettingsRepository: UserSettingsRepository,
    settingsViewModel: SettingsViewModel
) {
    var showMenuDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    val mainViewModel: MainViewModel = viewModel(
        factory = MainViewModelFactory(
            vasarRepository,
            eladasRepository,
            userSettingsRepository
        )
    )

    val mainUiState by mainViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        SnackbarManager.messages.collect {
            snackbarHostState.showSnackbar(it)
        }
    }

    Scaffold(
        containerColor = Color.Transparent,

        snackbarHost = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 120.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                SnackbarHost(hostState = snackbarHostState)
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        16.dp,
                    bottom = 64.dp
                    ),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = { showMenuDialog = true },
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    Text(
                        text ="☰ Menü",
                        style = MaterialTheme.typography.blackGlow
                    )
                }
            }
        }
    ) { padding ->



        NavHost(
            navController = navController,
            startDestination = "main",
            modifier = Modifier.padding(padding)
        ) {

            // ---------------- MAIN ----------------

            composable("main") {
                MainScreen(
                    activeVasar = mainUiState.activeVasar,
                    vasarLista = mainUiState.vasarLista,
                    bevetel = mainUiState.bevetel,
                    eladasLista = mainUiState.eladasokAktiv,
                    onUjVasarClick = { navController.navigate("ujvasar") },
                    onVasarSelected = { mainViewModel.setActiveVasar(it) },
                    onEladasClick = { vasar ->
                        mainViewModel.setActiveVasar(vasar)
                        navController.navigate("eladas/${vasar.id}")
                    },

                    onTetelesEladasClick = { vasar ->
                        mainViewModel.setActiveVasar(vasar)
                        navController.navigate("teteles/${vasar.id}")
                    },

                    onEladasokClick = { vasar ->
                        navController.navigate("eladastorles/${vasar.id}")
                    },

                    onVasarUpdate = { mainViewModel.updateVasar(it) },
                    onVasarDelete = { mainViewModel.deleteVasar(it) },


                )
            }


            // ---------------- ÚJ VÁSÁR ----------------

            composable("ujvasar") {
                val vm: NewVasarViewModel = viewModel(
                    factory = NewVasarViewModelFactory(vasarRepository)
                )

                NewVasarScreen(
                    onBackClick = { navController.popBackStack() },
                    onSave = {
                        vm.insertVasar(it)
                        mainViewModel.setActiveVasar(it)
                        navController.popBackStack()
                    }
                )
            }



            composable(
                route = "eladas/{vasarId}",
                arguments = listOf(
                    navArgument("vasarId") {
                        type = NavType.IntType
                    }
                )
            ) { backStackEntry ->



            val vasarId = backStackEntry.arguments?.getInt("vasarId") ?: return@composable


                EladasScreen(
                    vasarId = vasarId,
                    termekRepository = termekRepository,
                    eladasRepository = eladasRepository,
                    kategoriaRepository = kategoriaRepository,
                    vasarRepository = vasarRepository,
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable("vasarszerkesztes") {

                val vm: VasarSzerkesztesViewModel = viewModel(
                    factory = VasarSzerkesztesViewModelFactory(
                        vasarRepository,
                        userSettingsRepository
                    )
                )

                val honapok by vm.honapok.collectAsState()

                VasarSzerkesztesScreen(
                    honapok = honapok,
                    onOpenVasar = { id ->
                        vm.openVasar(id)
                    },
                    onUpdateVasar = { vm.updateVasar(it) },
                    onDeleteVasar = { vm.deleteVasar(it) },
                    onNavigateHome = {
                        navController.navigate("main") {
                            popUpTo("vasarszerkesztes") { inclusive = true }
                        }

                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                route = "teteles/{vasarId}",
                arguments = listOf(
                    navArgument("vasarId") {
                        type = NavType.IntType
                    }
                )
            ) { backStackEntry ->

                val vasarId =
                    backStackEntry.arguments?.getInt("vasarId")
                        ?: return@composable

                val vm: EladasViewModel = viewModel(
                    factory = EladasViewModelFactory(
                        vasarId = vasarId,
                        termekRepository = termekRepository,
                        eladasRepository = eladasRepository,
                        vasarRepository = vasarRepository,
                        kategoriaRepository = kategoriaRepository
                    )
                )

                val termekLista by vm.termekLista.collectAsState()

                TetelesEladasScreen(
                    termekLista = termekLista,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onEladasVeglegesites = { lista ->
                        vm.insertTetelesEladas(lista)
                    }
                )
            }


            composable("statisztika") {

                val vm: StatisztikaViewModel = viewModel(
                    factory = StatisztikaViewModelFactory(
                        vasarRepository,
                        eladasRepository,
                        userSettingsRepository
                    )
                )

                StatisztikaScreen(
                    viewModel = vm,
                    onBackClick = { navController.popBackStack() }
                )
            }




            composable("csoportos") {

                val kategoriak by kategoriaRepository
                    .getAll()
                    .collectAsState(initial = emptyList())

                val scope = rememberCoroutineScope()

                CsoportosTermekScreen(
                    kategoriak = kategoriak,

                    onBackClick = {
                        navController.popBackStack()
                    },

                    onSaveBulk = { kategoria, ar, termekNevek ->

                        scope.launch {

                            termekNevek.forEachIndexed { index, nev ->

                                termekRepository.insert(
                                    TermekEntity(
                                        nev = nev,
                                        kategoriaId = kategoria.id,
                                        kategoria = kategoria.nev,
                                        ar = ar,
                                        sorrend = index
                                    )
                                )
                            }
                        }
                    }
                )
            }



            composable("settings") {

                SettingsScreen(
                    viewModel = settingsViewModel,
                    onBackClick = { navController.popBackStack() }
                )

            }

            composable("keszlet") {

                val vm: KeszletViewModel = viewModel(
                    factory = KeszletViewModelFactory(
                        context = navController.context
                    )
                )

                KeszletScreen(
                    viewModel = vm
                )
            }

            composable("termekek") {

                val vm: TermekekViewModel = viewModel(
                    factory = TermekekViewModelFactory(
                        context = navController.context
                    )
                )

                TermekekScreen(
                    viewModel = vm,
                    onBackClick = { navController.popBackStack() }
                )
            }




            composable(
                route = "eladastorles/{vasarId}",
                arguments = listOf(
                    navArgument("vasarId") {
                        type = NavType.IntType
                    }
                )
            ) { backStackEntry ->

                val vasarId =
                    backStackEntry.arguments?.getInt("vasarId")
                        ?: return@composable


                val vm: EladasViewModel = viewModel(
                    factory = EladasViewModelFactory(
                        vasarId = vasarId,
                        termekRepository = termekRepository,
                        eladasRepository = eladasRepository,
                        vasarRepository = vasarRepository,
                        kategoriaRepository = kategoriaRepository
                    )
                )

                val eladasLista by vm.eladasLista.collectAsState()

                EladasTorlesScreen(
                    eladasLista = eladasLista,

                    onBackClick = {
                        navController.popBackStack()
                    },

                    onDeleteEladas = { eladas ->
                        vm.deleteEladas(eladas)
                    },


                )
            }










        }

        if (showMenuDialog) {
            MenuDialog(
                onDismiss = { showMenuDialog = false },

                onMainClick = {
                    navController.navigate("main") {
                        popUpTo("main") { inclusive = false }
                    }
                },

                onUjVasarClick = {
                    navController.navigate("ujvasar")

                },

                onVasarSzerkesztesClick = {
                    showMenuDialog = false // Zárjuk be a menüt a navigáció előtt
                    navController.navigate("vasarszerkesztes") {
                        popUpTo("main") { inclusive = true }
                    }
                },

                onCsoportosClick = {
                    navController.navigate("csoportos")
                },

                onEladasokClick = {
                    mainUiState.activeVasar?.let {
                        navController.navigate("eladastorles/${it.id}")

                    }
                },



                onPdfClick = {
                    showMenuDialog = false
                    navController.navigate("statisztika")
                },


                onKeszletClick = {
                    showMenuDialog = false
                    navController.navigate("keszlet")
                },


                onSettingsClick = {
                    showMenuDialog = false
                    navController.navigate("settings")
                },

                onTermekekClick = {
                    showMenuDialog = false
                    navController.navigate("termekek")
                },


                )
        }


    }


}

