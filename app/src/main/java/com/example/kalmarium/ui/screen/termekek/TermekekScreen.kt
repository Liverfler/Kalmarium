package com.example.kalmarium.ui.screen.termekek

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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kalmarium.data.KategoriaEntity
import com.example.kalmarium.data.TermekEntity
import com.example.kalmarium.ui.dialog.KategoriaEditDialog
import com.example.kalmarium.ui.dialog.KategoriaNewDialog
import com.example.kalmarium.ui.dialog.TermekEditDialog
import com.example.kalmarium.ui.dialog.TermekNewDialog

@Composable
fun TermekekScreen(
    viewModel: TermekekViewModel,
    onBackClick: () -> Unit
) {

    val termekLista by viewModel.termekek.collectAsState()
    val kategoriaLista by viewModel.kategoriak.collectAsState()

    var selectedKategoria by remember { mutableStateOf<Int?>(null) }

    var editingKategoria by remember { mutableStateOf<KategoriaEntity?>(null) }
    var editingTermek by remember { mutableStateOf<TermekEntity?>(null) }

    var showNewKategoriaDialog by remember { mutableStateOf(false) }
    var showNewTermekDialog by remember { mutableStateOf(false) }

    val kategoriak = kategoriaLista.sortedBy { it.sorrend }

    Scaffold(
        containerColor = Color.Transparent
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            Text(
                text = "Termékek kezelése",
                style = MaterialTheme.typography.titleLarge
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

                        Divider()
                    }
                }
            } else {

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
                                onClick = { editingTermek = termek },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = backgroundColor,
                                    contentColor = contentColor
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("${termek.nev} – ${termek.ar} Ft")
                            }
                        }

                        Divider()
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

    // DIALOGOK

    editingKategoria?.let {
        KategoriaEditDialog(
            kategoria = it,
            onSave = {
                viewModel.updateKategoria(it)
                editingKategoria = null
            },
            onDelete = {
                viewModel.deleteKategoria(it)
                editingKategoria = null
            },
            onDismiss = { editingKategoria = null }
        )
    }

    editingTermek?.let {
        TermekEditDialog(
            termek = it,
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
            onDismiss = { showNewKategoriaDialog = false },
            onSave = {
                viewModel.insertKategoria(it)
                showNewKategoriaDialog = false
            }
        )
    }

    if (showNewTermekDialog && selectedKategoria != null) {

        val kat = kategoriaLista.find { it.id == selectedKategoria }

        kat?.let {
            TermekNewDialog(
                kategoriaId = it.id,
                kategoriaNev = it.nev,
                onDismiss = { showNewTermekDialog = false },
                onSave = {
                    viewModel.insertTermek(it)
                    showNewTermekDialog = false
                }
            )
        }
    }
}
