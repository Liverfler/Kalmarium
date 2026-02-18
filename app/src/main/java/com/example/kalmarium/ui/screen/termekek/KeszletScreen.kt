package com.example.kalmarium.ui.screen.termekek

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.kalmarium.ui.theme.AppButton

@Composable
fun KeszletScreen(
    viewModel: KeszletViewModel
) {

    val termekek by viewModel.termekek.collectAsState()
    val kategoriak by viewModel.kategoriak.collectAsState()

    var selectedKategoria by remember { mutableStateOf<String?>(null) }
    var expanded by remember { mutableStateOf(false) }

    var localState by remember(termekek) {
        mutableStateOf(termekek.map { it.copy() })
    }

    val filtered = localState.filter {
        selectedKategoria == null || it.kategoria == selectedKategoria
    }

    Column(modifier = Modifier.fillMaxSize()) {

        // ---------------- KATEGÓRIA VÁLASZTÓ ----------------

        Box(modifier = Modifier.padding(16.dp)) {

            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(selectedKategoria ?: "Kategória kiválasztása")
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                kategoriak.forEach { kategoria ->
                    DropdownMenuItem(
                        text = { Text(kategoria) },
                        onClick = {
                            selectedKategoria = kategoria
                            expanded = false
                        }
                    )
                }
            }
        }

        // ---------------- LISTA ----------------

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            items(filtered, key = { it.id }) { termek ->

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(termek.nev)

                            if (termek.keszlet == 0) {
                                Text(
                                    "Nincs készleten",
                                    color = Color.Red,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            IconButton(
                                onClick = {
                                    localState = localState.map {
                                        if (it.id == termek.id)
                                            it.copy(
                                                keszlet = (it.keszlet - 1)
                                                    .coerceAtLeast(0)
                                            )
                                        else it
                                    }
                                }
                            ) {
                                Text("-")
                            }

                            OutlinedTextField(
                                value = termek.keszlet.toString(),
                                onValueChange = { value ->
                                    val uj = value.filter { it.isDigit() }
                                    val ujInt = uj.toIntOrNull() ?: 0

                                    localState = localState.map {
                                        if (it.id == termek.id)
                                            it.copy(keszlet = ujInt)
                                        else it
                                    }
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number
                                ),
                                modifier = Modifier.width(80.dp)
                            )

                            IconButton(
                                onClick = {
                                    localState = localState.map {
                                        if (it.id == termek.id)
                                            it.copy(keszlet = it.keszlet + 1)
                                        else it
                                    }
                                }
                            ) {
                                Text("+")
                            }
                        }
                    }
                }
            }
        }

        // ---------------- MENTÉS ----------------

        AppButton(
            onClick = {
                viewModel.saveAll(localState)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Mentés")
        }
    }
}
