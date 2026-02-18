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
import com.example.kalmarium.data.KategoriaEntity

@Composable
fun KategoriaNewDialog(
    onDismiss: () -> Unit,
    onSave: (KategoriaEntity) -> Unit
) {

    var nev by remember { mutableStateOf("") }
    var szin by remember { mutableStateOf(0xFF9E9E9E) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Új kategória") },
        text = {
            Column {

                OutlinedTextField(
                    value = nev,
                    onValueChange = { nev = it },
                    label = { Text("Kategória név") }
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
                    if (nev.isNotBlank()) {
                        onSave(
                            KategoriaEntity(
                                nev = nev,
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
