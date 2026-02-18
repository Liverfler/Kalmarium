package com.example.kalmarium.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.kalmarium.data.TermekEntity

@Composable
fun TermekEditDialog(
    termek: TermekEntity,
    onDismiss: () -> Unit,
    onSave: (TermekEntity) -> Unit,
    onDelete: (TermekEntity) -> Unit
) {

    var nev by remember { mutableStateOf(termek.nev) }
    var arText by remember { mutableStateOf(termek.ar.toString()) }
    var szin by remember { mutableStateOf(termek.szin) }

    var confirmDelete by remember { mutableStateOf(false) }

    if (confirmDelete) {
        AlertDialog(
            onDismissRequest = { confirmDelete = false },
            title = { Text("Termék törlése") },
            text = { Text("Biztosan törölni szeretnéd?") },
            confirmButton = {
                TextButton(onClick = {
                    onDelete(termek)
                    confirmDelete = false
                }) {
                    Text("Törlés")
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmDelete = false }) {
                    Text("Mégse")
                }
            }
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Termék szerkesztése") },
        text = {
            Column {

                OutlinedTextField(
                    value = nev,
                    onValueChange = { nev = it },
                    label = { Text("Terméknév") }
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = arText,
                    onValueChange = { arText = it.filter { c -> c.isDigit() } },
                    label = { Text("Ár") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Szín")

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ){

                    val colorOptions = listOf(
                        0xFF9E9E9E,
                        0xFF2196F3,
                        0xFFF44336,
                        0xFF4CAF50,
                        0xFFFF9800,
                        0xFF9C27B0
                    )

                    colorOptions.forEach { colorValue ->

                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color(colorValue), CircleShape)
                                .then(
                                    if (szin == colorValue) {
                                        Modifier.border(
                                            2.dp,
                                            Color.Black,
                                            CircleShape
                                        )
                                    } else Modifier
                                )
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    szin = colorValue
                                }

                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val ujAr = arText.toIntOrNull() ?: termek.ar

                    onSave(
                        termek.copy(
                            nev = nev,
                            ar = ujAr,
                            szin = szin
                        )
                    )
                }
            ) {
                Text("Mentés")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { confirmDelete = true }
            ) {
                Text("Törlés")
            }
        }
    )
}
