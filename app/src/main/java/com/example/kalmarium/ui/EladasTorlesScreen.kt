package com.example.kalmarium.ui

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

@Composable
fun EladasTorlesScreen(
    vasarNev: String,
    eladasLista: List<EladasEntity>,
    onBackClick: () -> Unit,
    onDeleteEladas: (EladasEntity) -> Unit,
    onDeleteAll: () -> Unit
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
            text = "Eladások törlése",
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

                    val time = dateFormat.format(Date(eladas.timestamp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Text(
                            text = "${eladas.termekNev} - $time"
                        )

                        TextButton(
                            onClick = { onDeleteEladas(eladas) }
                        ) {
                            Text("Eladás törlése")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onDeleteAll,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Összes törlése")
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
}
