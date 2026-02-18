package com.example.kalmarium.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.kalmarium.data.KategoriaEntity

@Composable
fun KategoriaEditDialog(
    kategoria: KategoriaEntity,
    onSave: (KategoriaEntity) -> Unit,
    onDelete: (KategoriaEntity) -> Unit,
    onDismiss: () -> Unit
) {

    var nev by remember { mutableStateOf(kategoria.nev) }
    var szin by remember { mutableStateOf(kategoria.szin) }

    var showDeleteConfirm by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Kategória szerkesztése") },
        text = {

            Column {

                // ===== NÉV =====
                OutlinedTextField(
                    value = nev,
                    onValueChange = { nev = it },
                    label = { Text("Kategória név") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // ===== SZÍN =====
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

                Spacer(modifier = Modifier.height(24.dp))

                // ===== TÖRLÉS GOMB =====
                TextButton(
                    onClick = { showDeleteConfirm = true },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Kategória törlése")
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onSave(
                        kategoria.copy(
                            nev = nev.trim(),
                            szin = szin
                        )
                    )
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

    // =============================
    // TÖRLÉS MEGERŐSÍTÉS
    // =============================
    if (showDeleteConfirm) {
        ConfirmDeleteDialog(
            title = "Biztos törlöd a kategóriát?",
            message = "A kategória és a hozzá tartozó termékek törlődni fognak.",
            confirmText = "Törlés",
            onConfirm = {
                onDelete(kategoria)
                showDeleteConfirm = false
                onDismiss()
            },
            onDismiss = { showDeleteConfirm = false }
        )
    }
}
