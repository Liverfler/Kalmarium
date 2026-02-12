package com.example.kalmarium

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.kalmarium.data.*
import com.example.kalmarium.data.repository.EladasRepository
import com.example.kalmarium.data.repository.TermekRepository
import com.example.kalmarium.data.repository.VasarRepository
import com.example.kalmarium.ui.*
import com.example.kalmarium.ui.theme.KalmariumTheme
import kotlinx.coroutines.launch

import com.example.kalmarium.ui.navigation.Screen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "kalmarium_db"
        )
            .fallbackToDestructiveMigration()
            .build()

        val vasarDao = db.vasarDao()
        val termekDao = db.termekDao()
        val eladasDao = db.eladasDao()

        val vasarRepository = VasarRepository(
            vasarDao,
            termekDao,
            eladasDao,
            applicationContext
        )

        val termekRepository = TermekRepository(
            termekDao,
            vasarDao,
            eladasDao,
            applicationContext
        )

        val eladasRepository = EladasRepository(
            eladasDao,
            vasarDao,
            termekDao,
            applicationContext
        )

        val viewModel = androidx.lifecycle.ViewModelProvider(
            this,
            com.example.kalmarium.ui.viewmodel.MainViewModelFactory(
                vasarRepository,
                termekRepository,
                eladasRepository
            )
        )[com.example.kalmarium.ui.viewmodel.MainViewModel::class.java]



        setContent {

            var currentScreen by remember { mutableStateOf<Screen>(Screen.Main) }
            var tetelesSiker by remember { mutableStateOf(false) }

            val latestVasarList by viewModel.latestVasarList.collectAsState(initial = emptyList())
            val termekList by viewModel.termekList.collectAsState(initial = emptyList())



            KalmariumTheme {

                Box(modifier = Modifier.fillMaxSize()) {

                    Image(
                        painter = painterResource(id = R.drawable.background),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                        alpha = 0.15f
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 40.dp)
                    ) {

                        when (val screen = currentScreen) {

                            // ===== FŐMENÜ =====
                            Screen.Main -> MainScreen(
                                latestVasarList = latestVasarList,
                                onTermekekClick = { currentScreen = Screen.Termekek },
                                onUjVasarClick = { currentScreen = Screen.UjVasar },
                                onVasarClick = { vasar ->
                                    currentScreen = Screen.VasarReszletek(vasar)
                                }
                            )

                            // ===== TERMÉKEK =====
                            Screen.Termekek -> {

                                val selectedTermek by viewModel.selectedTermek.collectAsState()
                                val eladottDarab by viewModel.eladottDarab.collectAsState()
                                val osszesBevetel by viewModel.osszesBevetel.collectAsState()

                                TermekListaScreen(
                                    termekLista = termekList,
                                    selectedTermek = selectedTermek,
                                    eladottDarab = eladottDarab,
                                    osszesBevetel = osszesBevetel,

                                    onSelectTermek = { termek ->
                                        viewModel.selectTermek(termek)
                                    },

                                    onClearSelectedTermek = {
                                        viewModel.clearSelectedTermek()
                                    },

                                    onBackClick = { currentScreen = Screen.Main },

                                    onOpenKategoriaScreen = {
                                        currentScreen = Screen.KategoriaSzerkeszto
                                    },

                                    onAddTermek = { nev, kategoria, ar ->
                                        viewModel.insertTermek(
                                            TermekEntity(
                                                nev = nev,
                                                kategoria = kategoria,
                                                ar = ar
                                            )
                                        )
                                    },

                                    onUpdateAr = { id, ujAr ->
                                        viewModel.updateAr(id, ujAr)
                                    },

                                    onRenameKategoria = { regiNev, ujNev ->
                                        viewModel.renameKategoria(regiNev, ujNev)
                                    },

                                    onDeleteKategoria = { kategoria ->
                                        viewModel.deleteKategoria(kategoria)
                                    }
                                )
                            }


                            // ===== VÁSÁR RÉSZLETEK =====
                            is Screen.VasarReszletek -> {

                                val vasar = screen.vasar

                                val bevetel by viewModel
                                    .getBevetelForVasar(vasar.nev)
                                    .collectAsState(initial = 0)

                                val eladasLista by viewModel
                                    .getEladasokVasarhoz(vasar.nev)
                                    .collectAsState(initial = emptyList())

                                VasarReszletekScreen(
                                    vasarNev = vasar.nev,
                                    vasarDatum = vasar.datum,
                                    vasarHely = vasar.hely,
                                    bevetel = bevetel,
                                    koltseg = vasar.koltseg,
                                    termekLista = termekList,
                                    eladasLista = eladasLista,

                                    onBackClick = { currentScreen = Screen.Main },

                                    onTetelesEladasClick = {
                                        currentScreen = Screen.TetelesEladas(vasar)
                                    },

                                    onEladasRogzit = { termek ->
                                        viewModel.insertEladas(
                                            EladasEntity(
                                                vasarNev = vasar.nev,
                                                termekNev = termek.nev,
                                                kategoria = termek.kategoria,
                                                mennyiseg = 1,
                                                timestamp = System.currentTimeMillis()
                                            )
                                        )
                                    },

                                    onDeleteEladasScreenClick = {
                                        currentScreen = Screen.EladasTorles(vasar)
                                    },

                                    onDeleteVasarClick = {
                                        viewModel.deleteVasar(vasar)
                                        currentScreen = Screen.Main
                                    },

                                    showTetelesSiker = tetelesSiker,
                                    onTetelesSikerConsumed = {
                                        tetelesSiker = false
                                    }
                                )
                            }


                            // ===== TÉTELES ELADÁS =====
                            is Screen.TetelesEladas -> {


                                val vasar = screen.vasar


                                TetelesEladasScreen(
                                    termekLista = termekList,
                                    onBackClick = {
                                        currentScreen = Screen.VasarReszletek(vasar)
                                    },
                                    onEladasVeglegesites = { kosar ->
                                        kosar.forEach { termek ->
                                            viewModel.insertEladas(
                                                EladasEntity(
                                                    vasarNev = vasar.nev,
                                                    termekNev = termek.nev,
                                                    kategoria = termek.kategoria,
                                                    mennyiseg = 1,
                                                    timestamp = System.currentTimeMillis()
                                                )
                                            )
                                        }

                                        tetelesSiker = true
                                        currentScreen = Screen.VasarReszletek(vasar)
                                    }

                                )
                            }

                            // ===== ELADÁS TÖRLÉS =====
                            is Screen.EladasTorles -> {

                                val vasar = screen.vasar

                                val eladasLista by viewModel
                                    .getEladasokVasarhoz(vasar.nev)
                                    .collectAsState(initial = emptyList())

                                EladasTorlesScreen(
                                    vasarNev = vasar.nev,
                                    eladasLista = eladasLista,
                                    onBackClick = {
                                        currentScreen = Screen.VasarReszletek(vasar)
                                    },
                                    onDeleteEladas = { eladas ->
                                        viewModel.deleteEladas(eladas)
                                    },
                                    onDeleteAll = {
                                        viewModel.deleteAllForVasar(vasar.nev)
                                    }


                                )
                            }

                            Screen.KategoriaSzerkeszto -> KategoriaSzerkesztoScreen(
                                kategoriak = termekList.map { it.kategoria }.distinct(),
                                onBackClick = { currentScreen = Screen.Termekek },
                                onRenameKategoria = { regiNev, ujNev ->
                                    viewModel.renameKategoria(regiNev, ujNev)
                                },
                                onDeleteKategoria = { kategoria ->
                                    viewModel.deleteKategoria(kategoria)
                                }
                            )


                            Screen.UjVasar -> NewVasarScreen(
                                onBackClick = { currentScreen = Screen.Main },
                                onSave = { ujVasar ->
                                    viewModel.insertVasar(ujVasar)
                                    currentScreen = Screen.Main
                                }

                            )

                        }
                    }
                }
            }
        }

    }
}


