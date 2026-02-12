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
import com.example.kalmarium.ui.*
import com.example.kalmarium.ui.theme.KalmariumTheme
import kotlinx.coroutines.launch

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

        // ===== AUTO RESTORE =====
        lifecycleScope.launch {
            val vasarok = vasarDao.getAllOnce()

            if (vasarok.isEmpty() &&
                BackupManager.backupExists(applicationContext)
            ) {
                BackupManager.restoreBackup(applicationContext)?.let { data ->
                    data.vasarok.orEmpty().forEach {
                        vasarDao.insert(it.copy(id = 0))
                    }
                    data.termekek.orEmpty().forEach {
                        termekDao.insert(it.copy(id = 0))
                    }
                    data.eladasok.orEmpty().forEach {
                        eladasDao.insert(it.copy(id = 0))
                    }
                }
            }
        }

        setContent {

            var currentScreen by remember { mutableStateOf("main") }
            var selectedVasar by remember { mutableStateOf<VasarEntity?>(null) }
            var tetelesSiker by remember { mutableStateOf(false) }

            val latestVasarList by vasarDao
                .getLatestFive()
                .collectAsState(initial = emptyList())

            val termekList by termekDao
                .getAll()
                .collectAsState(initial = emptyList())

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

                        when (currentScreen) {

                            // ===== FŐMENÜ =====
                            "main" -> MainScreen(
                                latestVasarList = latestVasarList,
                                onTermekekClick = { currentScreen = "termekek" },
                                onUjVasarClick = { currentScreen = "ujvasar" },
                                onVasarClick = { vasar ->
                                    selectedVasar = vasar
                                    currentScreen = "vasarreszletek"
                                }
                            )

                            // ===== TERMÉKEK =====
                            "termekek" -> TermekListaScreen(
                                termekLista = termekList,
                                eladasDao = eladasDao,
                                onBackClick = { currentScreen = "main" },
                                onOpenKategoriaScreen = {
                                    currentScreen = "kategoriaSzerkeszto"
                                },
                                onAddTermek = { nev, kategoria, ar ->
                                    lifecycleScope.launch {
                                        termekDao.insert(
                                            TermekEntity(
                                                nev = nev,
                                                kategoria = kategoria,
                                                ar = ar
                                            )
                                        )
                                        saveBackup(vasarDao, termekDao, eladasDao)
                                    }
                                },
                                onUpdateAr = { id, ujAr ->
                                    lifecycleScope.launch {
                                        termekDao.updateAr(id, ujAr)
                                    }
                                },
                                onRenameKategoria = { regiNev, ujNev ->
                                    lifecycleScope.launch {
                                        termekDao.renameKategoria(regiNev, ujNev)
                                        saveBackup(vasarDao, termekDao, eladasDao)
                                    }
                                },
                                onDeleteKategoria = { kategoria ->
                                    lifecycleScope.launch {
                                        termekDao.deleteByKategoria(kategoria)
                                        saveBackup(vasarDao, termekDao, eladasDao)
                                    }
                                }
                            )

                            // ===== VÁSÁR RÉSZLETEK =====
                            "vasarreszletek" -> selectedVasar?.let { vasar ->

                                val bevetel by eladasDao
                                    .getBevetelForVasar(vasar.nev)
                                    .collectAsState(initial = 0)

                                val eladasLista by eladasDao
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

                                    onBackClick = { currentScreen = "main" },

                                    onTetelesEladasClick = {
                                        currentScreen = "teteleseladas"
                                    },

                                    onEladasRogzit = { termek ->
                                        lifecycleScope.launch {
                                            eladasDao.insert(
                                                EladasEntity(
                                                    vasarNev = vasar.nev,
                                                    termekNev = termek.nev,
                                                    kategoria = termek.kategoria,
                                                    mennyiseg = 1,
                                                    timestamp = System.currentTimeMillis()
                                                )
                                            )
                                            saveBackup(vasarDao, termekDao, eladasDao)
                                        }
                                    },

                                    onDeleteEladasScreenClick = {
                                        currentScreen = "eladastorles"
                                    },

                                    onDeleteVasarClick = {
                                        lifecycleScope.launch {

                                            vasarDao.delete(vasar)
                                            saveBackup(vasarDao, termekDao, eladasDao)
                                        }
                                        currentScreen = "main"
                                    },

                                    showTetelesSiker = tetelesSiker,
                                    onTetelesSikerConsumed = {
                                        tetelesSiker = false
                                    }
                                )
                            }

                            // ===== TÉTELES ELADÁS =====
                            "teteleseladas" -> selectedVasar?.let { vasar ->

                                TetelesEladasScreen(
                                    termekLista = termekList,
                                    onBackClick = {
                                        currentScreen = "vasarreszletek"
                                    },
                                    onEladasVeglegesites = { kosar ->
                                        lifecycleScope.launch {
                                            kosar.forEach { termek ->
                                                eladasDao.insert(
                                                    EladasEntity(
                                                        vasarNev = vasar.nev,
                                                        termekNev = termek.nev,
                                                        kategoria = termek.kategoria,
                                                        mennyiseg = 1,
                                                        timestamp = System.currentTimeMillis()
                                                    )
                                                )
                                            }
                                            saveBackup(vasarDao, termekDao, eladasDao)
                                        }
                                        tetelesSiker = true
                                        currentScreen = "vasarreszletek"
                                    }
                                )
                            }

                            // ===== ELADÁS TÖRLÉS =====
                            "eladastorles" -> selectedVasar?.let { vasar ->

                                val eladasLista by eladasDao
                                    .getEladasokVasarhoz(vasar.nev)
                                    .collectAsState(initial = emptyList())

                                EladasTorlesScreen(
                                    vasarNev = vasar.nev,
                                    eladasLista = eladasLista,
                                    onBackClick = {
                                        currentScreen = "vasarreszletek"
                                    },
                                    onDeleteEladas = { eladas ->
                                        lifecycleScope.launch {
                                            eladasDao.delete(eladas)
                                            saveBackup(vasarDao, termekDao, eladasDao)
                                        }
                                    },
                                    onDeleteAll = {
                                        lifecycleScope.launch {
                                            eladasDao.deleteAllForVasar(vasar.nev)
                                            saveBackup(vasarDao, termekDao, eladasDao)
                                        }
                                    }
                                )
                            }


                            "kategoriaSzerkeszto" -> KategoriaSzerkesztoScreen(
                                kategoriak = termekList.map { it.kategoria }.distinct(),
                                onBackClick = { currentScreen = "termekek" },
                                onRenameKategoria = { regiNev, ujNev ->
                                    lifecycleScope.launch {
                                        termekDao.renameKategoria(regiNev, ujNev)
                                        saveBackup(vasarDao, termekDao, eladasDao)
                                    }
                                },
                                onDeleteKategoria = { kategoria ->
                                    lifecycleScope.launch {
                                        termekDao.deleteByKategoria(kategoria)
                                        saveBackup(vasarDao, termekDao, eladasDao)
                                    }
                                }
                            )





                            // ===== ÚJ VÁSÁR =====
                            "ujvasar" -> NewVasarScreen(
                                onBackClick = { currentScreen = "main" },
                                onSave = { ujVasar ->
                                    lifecycleScope.launch {
                                        vasarDao.insert(ujVasar)
                                        saveBackup(vasarDao, termekDao, eladasDao)
                                    }
                                    currentScreen = "main"
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    private suspend fun saveBackup(
        vasarDao: VasarDao,
        termekDao: TermekDao,
        eladasDao: EladasDao
    ) {
        val vasarok = vasarDao.getAllOnce()
        val termekek = termekDao.getAllOnce()
        val eladasok = eladasDao.getAllOnce()

        BackupManager.saveBackup(
            applicationContext,
            vasarok,
            termekek,
            eladasok
        )
    }
}
