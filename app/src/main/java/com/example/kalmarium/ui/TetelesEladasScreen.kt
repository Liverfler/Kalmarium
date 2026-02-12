package com.example.kalmarium.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kalmarium.data.TermekEntity

@Composable
fun TetelesEladasScreen(
    termekLista: List<TermekEntity>,
    onBackClick: () -> Unit,
    onEladasVeglegesites: (List<TermekEntity>) -> Unit
) {

    var kosar by remember { mutableStateOf<List<TermekEntity>>(emptyList()) }
    var selectedKategoria by remember { mutableStateOf<String?>(null) }
    var termekValasztas by remember { mutableStateOf(false) }

    val kategoriak = termekLista.map { it.kategoria }.distinct()
    val osszeg = kosar.sumOf { it.ar }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // ===============================
        // KOSÁR LISTA
        // ===============================

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {

            items(kosar) { termek ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Text("${termek.nev} - ${termek.ar} Ft")

                        TextButton(
                            onClick = {
                                kosar = kosar - termek
                            }
                        ) {
                            Text("Törlés")
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { termekValasztas = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("➕ Termék hozzáadása")
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Összesen: $osszeg Ft",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Button(
                onClick = onBackClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text("Sztornó")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    if (kosar.isNotEmpty()) {
                        onEladasVeglegesites(kosar)
                        kosar = emptyList()
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Eladás befejezése")
            }
        }
    }

    // ===============================
    // TERMÉK VÁLASZTÁS DIALOG
    // ===============================

    if (termekValasztas) {

        AlertDialog(
            onDismissRequest = {
                selectedKategoria = null
                termekValasztas = false
            },
            title = { Text("Termék kiválasztása") },
            text = {

                Column {

                    if (selectedKategoria == null) {

                        kategoriak.forEach { kategoria ->

                            Button(
                                onClick = { selectedKategoria = kategoria },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(kategoria)
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                        }

                    } else {

                        val termekek = termekLista.filter {
                            it.kategoria == selectedKategoria
                        }

                        termekek.forEach { termek ->

                            Button(
                                onClick = {
                                    kosar = kosar + termek
                                    selectedKategoria = null
                                    termekValasztas = false
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("${termek.nev} - ${termek.ar} Ft")
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(
                    onClick = {
                        selectedKategoria = null
                        termekValasztas = false
                    }
                ) {
                    Text("Mégse")
                }
            }
        )
    }
}
