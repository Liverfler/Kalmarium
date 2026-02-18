package com.example.kalmarium.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.kalmarium.data.TermekEntity

@Composable
fun TermekNewDialog(
    kategoriaId: Int,
    kategoriaNev: String,
    onDismiss: () -> Unit,
    onSave: (TermekEntity) -> Unit
) {

    var nev by remember { mutableStateOf("") }
    var ar by remember { mutableStateOf("") }
    var szin by remember { mutableStateOf(0xFF2196F3) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Új termék") },
        text = {
            Column {

                OutlinedTextField(
                    value = nev,
                    onValueChange = { nev = it },
                    label = { Text("Termék név") }
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = ar,
                    onValueChange = { ar = it },
                    label = { Text("Ár") }
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Szín")

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

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
                    val arInt = ar.toIntOrNull()
                    if (nev.isNotBlank() && arInt != null) {
                        onSave(
                            TermekEntity(
                                nev = nev,
                                kategoriaId = kategoriaId,
                                kategoria = kategoriaNev,
                                ar = arInt,
                                szin = szin
                            )
                        )
                    }
                }
            ) {
                Text("Mentés")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Mégse")
            }
        }
    )
}
