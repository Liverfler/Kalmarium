package com.example.kalmarium.ui.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.kalmarium.data.VasarEntity

@Composable
fun VasarEditDialog(
    vasar: VasarEntity,
    onSave: (VasarEntity) -> Unit,
    onDelete: (VasarEntity) -> Unit,
    onDismiss: () -> Unit
) {

    var nev by remember { mutableStateOf(vasar.nev) }
    var hely by remember { mutableStateOf(vasar.hely) }
    var datum by remember { mutableStateOf(vasar.datum) }
    var koltseg by remember { mutableStateOf(vasar.koltseg.toString()) }

    var showDeleteConfirm by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Vásár szerkesztése") },
        text = {

            Column {

                // ===== NÉV =====
                OutlinedTextField(
                    value = nev,
                    onValueChange = { nev = it },
                    label = { Text("Vásár neve") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // ===== HELY =====
                OutlinedTextField(
                    value = hely,
                    onValueChange = { hely = it },
                    label = { Text("Helyszín") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // ===== DÁTUM =====
                OutlinedTextField(
                    value = datum,
                    onValueChange = { datum = it },
                    label = { Text("Dátum (yyyy-MM-dd)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // ===== KÖLTSÉG =====
                OutlinedTextField(
                    value = koltseg,
                    onValueChange = { koltseg = it },
                    label = { Text("Költség (Ft)") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                // ===== TÖRLÉS =====
                TextButton(
                    onClick = { showDeleteConfirm = true },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Vásár törlése")
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onSave(
                        vasar.copy(
                            nev = nev.trim(),
                            hely = hely.trim(),
                            datum = datum.trim(),
                            koltseg = koltseg.toIntOrNull() ?: 0
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
            title = "Biztos törlöd a vásárt?",
            message = "A vásár és az összes hozzá tartozó eladás törlődni fog.",
            confirmText = "Törlés",
            onConfirm = {
                onDelete(vasar)
                showDeleteConfirm = false
                onDismiss()
            },
            onDismiss = { showDeleteConfirm = false }
        )
    }
}
