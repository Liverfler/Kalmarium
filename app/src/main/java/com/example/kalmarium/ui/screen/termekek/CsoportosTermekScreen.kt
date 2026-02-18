package com.example.kalmarium.ui.screen.termekek

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.kalmarium.data.KategoriaEntity
import com.example.kalmarium.utils.SnackbarManager
import kotlinx.coroutines.launch
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.DropdownMenuItem





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CsoportosTermekScreen(
    kategoriak: List<KategoriaEntity>,
    onBackClick: () -> Unit,
    onSaveBulk: (KategoriaEntity, Int, List<String>) -> Unit
) {

    var kategoriaText by remember { mutableStateOf("") }
    var selectedKategoria by remember { mutableStateOf<KategoriaEntity?>(null) }
    var arText by remember { mutableStateOf("") }
    var termekListaText by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    val javasoltKategoriak = kategoriak.filter {
        it.nev.startsWith(kategoriaText, ignoreCase = true)
    }


    var expanded by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Text(
            "Csoportos termékfeltöltés",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {

            TextField(
                value = selectedKategoria?.nev ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Kategória") },
                placeholder = { Text("Válassz kategóriát") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                shape = MaterialTheme.shapes.large,
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {

                kategoriak.forEach { kat ->

                    val isSelected = kat == selectedKategoria

                    DropdownMenuItem(
                        text = {
                            Text(
                                text = kat.nev,
                                style = if (isSelected)
                                    MaterialTheme.typography.bodyLarge
                                else
                                    MaterialTheme.typography.bodyMedium
                            )
                        },
                        onClick = {
                            selectedKategoria = kat
                            expanded = false
                        },
                        colors = MenuDefaults.itemColors(
                            textColor = if (isSelected)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            }
        }



        if (kategoriaText.isNotBlank() && javasoltKategoriak.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            ) {
                Column {
                    javasoltKategoriak.forEach { kat ->
                        TextButton(
                            onClick = {
                                selectedKategoria = kat
                                kategoriaText = kat.nev
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(kat.nev)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = arText,
            onValueChange = { arText = it },
            label = { Text("Ár (Ft)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Terméknevek (soronként egy):")

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = termekListaText,
            onValueChange = { termekListaText = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {

                val ar = arText.toIntOrNull()
                val termekek = termekListaText
                    .lines()
                    .map { it.trim() }
                    .filter { it.isNotBlank() }

                if (selectedKategoria != null && ar != null && termekek.isNotEmpty()) {

                    onSaveBulk(selectedKategoria!!, ar, termekek)

                    scope.launch {
                        SnackbarManager.showMessage("${termekek.size} termék mentve")
                    }

                    onBackClick()

                } else {
                    scope.launch {
                        SnackbarManager.showMessage("Hiányos vagy hibás adat")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Mentés")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Mégse")
        }
    }
}

