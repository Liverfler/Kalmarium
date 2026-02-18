package com.example.kalmarium.ui.screen.eladasok

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kalmarium.data.KategoriaEntity
import com.example.kalmarium.data.TermekEntity
import com.example.kalmarium.data.repository.EladasRepository
import com.example.kalmarium.data.repository.KategoriaRepository
import com.example.kalmarium.data.repository.TermekRepository
import com.example.kalmarium.data.repository.VasarRepository
import com.example.kalmarium.ui.dialog.KategoriaEditDialog
import com.example.kalmarium.ui.dialog.TermekEditDialog
import com.example.kalmarium.ui.dialog.KategoriaNewDialog
import com.example.kalmarium.ui.dialog.TermekNewDialog
import com.example.kalmarium.ui.theme.AppButton
import com.example.kalmarium.ui.theme.blackGlow


@Composable
fun EladasScreen(
    vasarId: Int,
    termekRepository: TermekRepository,
    eladasRepository: EladasRepository,
    kategoriaRepository: KategoriaRepository,
    onBackClick: () -> Unit,
    vasarRepository: VasarRepository

    ) {

    val viewModel: EladasViewModel = viewModel(
        factory = EladasViewModelFactory(
            vasarId = vasarId,
            termekRepository = termekRepository,
            eladasRepository = eladasRepository,
            vasarRepository = vasarRepository,
            kategoriaRepository = kategoriaRepository
        )
    )

    val termekLista by viewModel.termekLista.collectAsState()
    val kategoriaLista by viewModel.kategoriaLista.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    var selectedKategoria by remember { mutableStateOf<Int?>(null) }

    var editingKategoria by remember { mutableStateOf<KategoriaEntity?>(null) }
    var editingTermek by remember { mutableStateOf<TermekEntity?>(null) }

    val kategoriak = kategoriaLista.sortedBy { it.sorrend }
    var showNewKategoriaDialog by remember { mutableStateOf(false) }
    var showNewTermekDialog by remember { mutableStateOf(false) }
    val vasarNev by viewModel.vasarNev.collectAsState()
    var selectedTermek by remember { mutableStateOf<TermekEntity?>(null) }



    Scaffold(
        containerColor = Color.Transparent,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            Text(
                text = "Eladás – $vasarNev",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 🔥 STÁTUSZ SZÖVEG
            Text(
                text = when {
                    editingKategoria != null -> "Kategória szerkesztése"
                    editingTermek != null -> "Termék szerkesztése"
                    selectedKategoria == null -> "Válassz kategóriát..."
                    else -> "Válassz terméket..."
                }
                ,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (selectedKategoria == null) {

                Button(
                    onClick = { showNewKategoriaDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("+ Kategória létrehozása")
                }

                Spacer(modifier = Modifier.height(12.dp))


                LazyColumn {

                    itemsIndexed(
                        items = kategoriak,
                        key = { _, item -> item.id }
                    ) { index, kategoria ->

                        var menuExpanded by remember { mutableStateOf(false) }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Box {

                                IconButton(
                                    onClick = { menuExpanded = true }
                                ) {
                                    Icon(Icons.Default.Edit, null)
                                }

                                DropdownMenu(
                                    expanded = menuExpanded,
                                    onDismissRequest = { menuExpanded = false }
                                ) {

                                    DropdownMenuItem(
                                        text = { Text("Mozgatás fel") },
                                        leadingIcon = {
                                            Icon(Icons.Default.KeyboardArrowUp, null)
                                        },
                                        onClick = {
                                            if (index > 0)
                                                viewModel.reorderKategoriak(index, index - 1)
                                            menuExpanded = false
                                        }
                                    )

                                    DropdownMenuItem(
                                        text = { Text("Szerkesztés") },
                                        onClick = {
                                            editingKategoria = kategoria
                                            menuExpanded = false
                                        }
                                    )

                                    DropdownMenuItem(
                                        text = { Text("Mozgatás le") },
                                        leadingIcon = {
                                            Icon(Icons.Default.KeyboardArrowDown, null)
                                        },
                                        onClick = {
                                            if (index < kategoriak.lastIndex)
                                                viewModel.reorderKategoriak(index, index + 1)
                                            menuExpanded = false
                                        }
                                    )
                                }
                            }

                            val backgroundColor = Color(kategoria.szin)
                            val contentColor =
                                if (backgroundColor.luminance() > 0.5f)
                                    Color.Black
                                else
                                    Color.White

                            Button(
                                onClick = { selectedKategoria = kategoria.id },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = backgroundColor,
                                    contentColor = contentColor
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(kategoria.nev)
                            }

                        }

                    }
                }
            }

            else {

                val termekek = termekLista
                    .filter { it.kategoriaId == selectedKategoria }
                    .sortedBy { it.sorrend }

                Button(
                    onClick = { showNewTermekDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("+ Termék létrehozása")
                }

                Spacer(modifier = Modifier.height(12.dp))


                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {

                    itemsIndexed(
                        items = termekek,
                        key = { _, item -> item.id }
                    ) { index, termek ->

                        var menuExpanded by remember { mutableStateOf(false) }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Box {

                                IconButton(
                                    onClick = { menuExpanded = true }
                                ) {
                                    Icon(Icons.Default.Edit, null)
                                }

                                DropdownMenu(
                                    expanded = menuExpanded,
                                    onDismissRequest = { menuExpanded = false }
                                ) {

                                    DropdownMenuItem(
                                        text = { Text("Mozgatás fel") },
                                        leadingIcon = {
                                            Icon(Icons.Default.KeyboardArrowUp, null)
                                        },
                                        onClick = {
                                            if (index > 0)
                                                viewModel.reorderTermekek(
                                                    selectedKategoria!!,
                                                    index,
                                                    index - 1
                                                )
                                            menuExpanded = false
                                        }
                                    )

                                    DropdownMenuItem(
                                        text = { Text("Szerkesztés") },
                                        onClick = {
                                            editingTermek = termek
                                            menuExpanded = false
                                        }
                                    )

                                    DropdownMenuItem(
                                        text = { Text("Mozgatás le") },
                                        leadingIcon = {
                                            Icon(Icons.Default.KeyboardArrowDown, null)
                                        },
                                        onClick = {
                                            if (index < termekek.lastIndex)
                                                viewModel.reorderTermekek(
                                                    selectedKategoria!!,
                                                    index,
                                                    index + 1
                                                )
                                            menuExpanded = false
                                        }
                                    )
                                }
                            }

                            val backgroundColor = Color(termek.szin)
                            val contentColor =
                                if (backgroundColor.luminance() > 0.5f)
                                    Color.Black
                                else
                                    Color.White



                            Button(
                                onClick = {
                                    selectedTermek = termek
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = backgroundColor,
                                    contentColor = contentColor
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("${termek.nev} – ${termek.ar} Ft")
                            }
                        }

                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = { selectedKategoria = null },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Vissza kategóriákhoz")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onBackClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Kilépés")
            }
        }
    }

    // =============================
// KATEGÓRIA EDIT DIALOG
// =============================

    editingKategoria?.let { kat ->
        KategoriaEditDialog(
            kategoria = kat,
            onSave = {
                viewModel.updateKategoria(it)
                editingKategoria = null
            },
            onDelete = {
                viewModel.deleteKategoria(it)
                editingKategoria = null
            },
            onDismiss = {
                editingKategoria = null
            }
        )
    }

// =============================
// TERMÉK EDIT DIALOG
// =============================

    editingTermek?.let { termek ->

        TermekEditDialog(
            termek = termek,
            onDismiss = { editingTermek = null },
            onSave = {
                viewModel.updateTermek(it)
                editingTermek = null
            },
            onDelete = {
                viewModel.deleteTermek(it)
                editingTermek = null
            }
        )
    }

    if (showNewKategoriaDialog) {
        KategoriaNewDialog(
            onDismiss = { },
            onSave = {
                viewModel.insertKategoria(it)
            }
        )
    }

    if (showNewTermekDialog && selectedKategoria != null) {

        val kat = kategoriaLista.find { it.id == selectedKategoria }

        kat?.let {
            TermekNewDialog(
                kategoriaId = it.id,
                kategoriaNev = it.nev,
                onDismiss = { },
                onSave = {
                    viewModel.insertTermek(it)
                }
            )
        }

    }

    if (selectedTermek != null) {

        var arInput by remember {
            mutableStateOf(selectedTermek!!.ar.toString())
        }

        AlertDialog(
            onDismissRequest = { selectedTermek = null },

            title = {
                Text(selectedTermek!!.nev)
            },

            text = {
                Column {

                    OutlinedTextField(
                        value = arInput,
                        onValueChange = { arInput = it.filter { c -> c.isDigit() } },
                        label = { Text("Ár (Ft)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    AppButton(
                        onClick = {

                            val veglegesAr =
                                arInput.toIntOrNull() ?: selectedTermek!!.ar

                            viewModel.insertEladas(
                                termek = selectedTermek!!,
                                ar = veglegesAr
                            )

                            selectedTermek = null
                        },
                        modifier = Modifier.fillMaxWidth()
                            .height(90.dp)
                    ) {
                        Text(
                            text ="Eladás",
                            style = MaterialTheme.typography
                                .blackGlow
                                .copy(fontSize = 24.sp),
                            )
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    OutlinedButton(
                        onClick = { selectedTermek = null },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Mégse")
                    }
                }
            },

            confirmButton = {},
            dismissButton = {}
        )

    }






}
