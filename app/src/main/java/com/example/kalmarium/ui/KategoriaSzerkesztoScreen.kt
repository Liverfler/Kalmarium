package com.example.kalmarium.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun KategoriaSzerkesztoScreen(
    kategoriak: List<String>,
    onBackClick: () -> Unit,
    onRenameKategoria: (String, String) -> Unit,
    onDeleteKategoria: (String) -> Unit
) {

    var showDialog by remember { mutableStateOf(false) }
    var selectedKategoria by remember { mutableStateOf("") }
    var ujNev by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("Kategóriák szerkesztése", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {

            items(kategoriak) { kategoria ->

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(kategoria)

                    Button(
                        onClick = {
                            selectedKategoria = kategoria
                            ujNev = kategoria
                            showDialog = true
                        }
                    ) {
                        Text("Szerkesztés")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Vissza")
        }
    }

    // ==========================
    // DIALOG
    // ==========================

    if (showDialog) {

        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Kategória módosítása") },
            text = {
                Column {

                    OutlinedTextField(
                        value = ujNev,
                        onValueChange = { ujNev = it },
                        label = { Text("Új név") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (ujNev.isNotBlank()) {
                                onRenameKategoria(selectedKategoria, ujNev)
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
                            onDeleteKategoria(selectedKategoria)
                            showDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
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
