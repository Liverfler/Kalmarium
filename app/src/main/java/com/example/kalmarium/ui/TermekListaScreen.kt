package com.example.kalmarium.ui

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.kalmarium.data.TermekEntity
import com.example.kalmarium.data.EladasDao

@Composable
fun TermekListaScreen(
    termekLista: List<TermekEntity>,
    eladasDao: EladasDao,
    onBackClick: () -> Unit,
    onOpenKategoriaScreen: () -> Unit,
    onAddTermek: (String, String, Int) -> Unit,
    onUpdateAr: (Int, Int) -> Unit,
    onRenameKategoria: (String, String) -> Unit,
    onDeleteKategoria: (String) -> Unit
) {

    var selectedKategoria by remember { mutableStateOf<String?>(null) }
    var selectedTermek by remember { mutableStateOf<TermekEntity?>(null) }
    var ujAr by remember { mutableStateOf("") }

    // ÚJ TERMÉK
    var ujTermekNev by remember { mutableStateOf("") }
    var ujTermekKategoria by remember { mutableStateOf("") }
    var ujTermekAr by remember { mutableStateOf("") }
    var showAddForm by remember { mutableStateOf(false) }

    // KATEGÓRIA DIALOG
    var showDialog by remember { mutableStateOf(false) }
    var dialogKategoria by remember { mutableStateOf("") }
    var ujKategoriaNev by remember { mutableStateOf("") }

    val kategoriak = termekLista.map { it.kategoria }.distinct()

    Column(modifier = Modifier.padding(16.dp)) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Button(
                onClick = onBackClick,
                modifier = Modifier.weight(1f)
            ) {
                Text("Vissza")
            }

            Button(
                onClick = onOpenKategoriaScreen,
                modifier = Modifier.weight(1f)
            ) {
                Text("Kategóriák\nszerkesztése")
            }
        }




        Spacer(modifier = Modifier.height(16.dp))

        when {

            // ==============================
            // 1️⃣ ÚJ TERMÉK
            // ==============================
            showAddForm -> {

                Text("Új termék", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = ujTermekNev,
                    onValueChange = { ujTermekNev = it },
                    label = { Text("Név") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                val javasoltKategoriak = kategoriak.filter {
                    it.startsWith(ujTermekKategoria, ignoreCase = true)
                }

                OutlinedTextField(
                    value = ujTermekKategoria,
                    onValueChange = { ujTermekKategoria = it },
                    label = { Text("Kategória") },
                    modifier = Modifier.fillMaxWidth()
                )

                if (ujTermekKategoria.isNotBlank() && javasoltKategoriak.isNotEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                    ) {
                        Column {
                            javasoltKategoriak.forEach { kategoria ->
                                TextButton(
                                    onClick = { ujTermekKategoria = kategoria },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(kategoria)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = ujTermekAr,
                    onValueChange = { ujTermekAr = it },
                    label = { Text("Ár (Ft)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val ar = ujTermekAr.toIntOrNull()
                        if (ujTermekNev.isNotBlank() &&
                            ujTermekKategoria.isNotBlank() &&
                            ar != null
                        ) {
                            onAddTermek(ujTermekNev, ujTermekKategoria, ar)

                            ujTermekNev = ""
                            ujTermekKategoria = ""
                            ujTermekAr = ""
                            showAddForm = false
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Mentés")
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = { showAddForm = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Mégse")
                }
            }

            // ==============================
            // 2️⃣ KATEGÓRIÁK
            // ==============================
            selectedKategoria == null && selectedTermek == null -> {

                Text("Kategóriák", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { showAddForm = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("+ Új termék")
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn {
                    items(kategoriak) { kategoria ->

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .combinedClickable(
                                    onClick = {
                                        selectedKategoria = kategoria
                                    },
                                    onLongClick = {
                                        dialogKategoria = kategoria
                                        ujKategoriaNev = kategoria
                                        showDialog = true
                                    }
                                )
                        ) {
                            Button(
                                onClick = { selectedKategoria = kategoria },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(kategoria)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            // ==============================
            // 3️⃣ TERMÉKEK
            // ==============================
            selectedKategoria != null && selectedTermek == null -> {

                Button(
                    onClick = { selectedKategoria = null },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("← Vissza kategóriákhoz")
                }

                Spacer(modifier = Modifier.height(16.dp))

                val termekek = termekLista.filter {
                    it.kategoria == selectedKategoria
                }

                LazyColumn {
                    items(termekek) { termek ->
                        Button(
                            onClick = {
                                selectedTermek = termek
                                ujAr = termek.ar.toString()
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("${termek.nev} - ${termek.ar} Ft")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            // ==============================
            // 4️⃣ TERMÉK RÉSZLETEK
            // ==============================
            selectedTermek != null -> {

                val termek = selectedTermek!!

                val darab by eladasDao
                    .getOsszesEladottDarab(termek.nev)
                    .collectAsState(initial = 0)

                val bevetel by eladasDao
                    .getOsszesBevetelTermek(termek.nev)
                    .collectAsState(initial = 0)

                Button(
                    onClick = { selectedTermek = null },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("← Vissza")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(termek.nev, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))

                Text("Eladva: $darab db")
                Text("Összbevétel: $bevetel Ft")

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = ujAr,
                    onValueChange = { ujAr = it },
                    label = { Text("Ár (Ft)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        ujAr.toIntOrNull()?.let {
                            onUpdateAr(termek.id, it)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ár mentése")
                }
            }
        }
    }

    // ==============================
    // KATEGÓRIA SZERKESZTŐ DIALOG
    // ==============================
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Kategória szerkesztése") },
            text = {
                Column {
                    OutlinedTextField(
                        value = ujKategoriaNev,
                        onValueChange = { ujKategoriaNev = it },
                        label = { Text("Új név") }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (ujKategoriaNev.isNotBlank()) {
                                onRenameKategoria(dialogKategoria, ujKategoriaNev)
                                showDialog = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Átnevezés")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            onDeleteKategoria(dialogKategoria)
                            showDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Törlés")
                    }
                }
            },
            confirmButton = {}
        )
    }
}
