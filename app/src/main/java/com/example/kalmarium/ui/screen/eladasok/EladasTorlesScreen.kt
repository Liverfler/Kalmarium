package com.example.kalmarium.ui.screen.eladasok

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kalmarium.data.EladasEntity
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.Alignment


@Composable
fun EladasTorlesScreen(
    eladasLista: List<EladasEntity>,
    onBackClick: () -> Unit,
    onDeleteEladas: (EladasEntity) -> Unit,
) {

    val dateFormat = remember {
        SimpleDateFormat("HH:mm", Locale.getDefault())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Eladások",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (eladasLista.isEmpty()) {

            Text("Nincs törölhető eladás.")

        } else {

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {

                items(eladasLista) { eladas ->

                    var showDialog by remember { mutableStateOf(false) }
                    val time = dateFormat.format(Date(eladas.timestamp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 0.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = "$time - ${eladas.termekNev} - ${eladas.eladasiAr}",
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        TextButton(
                            onClick = { showDialog = true }
                        ) {
                            Text("Törlés")
                        }
                    }
                    if (showDialog) {
                        AlertDialog(
                            onDismissRequest = { showDialog = false },
                            title = { Text("Megerősítés") },
                            text = { Text("Biztosan törlöd ezt az eladást?") },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        onDeleteEladas(eladas)
                                        showDialog = false
                                    }
                                ) {
                                    Text("Igen")
                                }
                            },
                            dismissButton = {
                                TextButton(
                                    onClick = { showDialog = false }
                                ) {
                                    Text("Mégse")
                                }
                            }
                        )
                    }
                }


            }


            Spacer(modifier = Modifier.height(16.dp))

        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Vissza")
        }
    }
}
