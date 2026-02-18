package com.example.kalmarium.ui.screen.vasarok

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kalmarium.data.VasarEntity
import com.example.kalmarium.ui.dialog.VasarEditDialog
import com.example.kalmarium.ui.theme.AppButton
import com.example.kalmarium.ui.theme.solidGlow
import com.example.kalmarium.ui.viewmodel.VasarSzerkesztesViewModel.HonapOsszesites

@Composable
fun VasarSzerkesztesScreen(
    honapok: List<HonapOsszesites>,
    onOpenVasar: (Int) -> Unit,
    onUpdateVasar: (VasarEntity) -> Unit,
    onDeleteVasar: (VasarEntity) -> Unit,
    onNavigateHome: () -> Unit,
    onBackClick: () -> Unit
) {

    var selectedHonap by remember { mutableStateOf<HonapOsszesites?>(null) }
    var selectedVasar by remember { mutableStateOf<VasarEntity?>(null) }


    Scaffold(
        containerColor = Color.Transparent,
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            items(honapok) { honap ->

                Card(
                    onClick = { selectedHonap = honap },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        // 🔵 FEJLÉC
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Text(
                                text = "${honap.ev} - ${honapNeve(honap.honap)}",
                                style = MaterialTheme.typography.solidGlow()
                            )

                            Text(
                                text = "${honap.osszBevetel} Ft",
                                style = MaterialTheme.typography.solidGlow()
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // 🔵 VÁSÁROK LISTÁZÁSA A FŐ KÁRTYÁN
                        honap.vasarok.forEach { vasar ->

                            val nap = if (vasar.datum.length >= 10)
                                vasar.datum.substring(8, 10)
                            else
                                ""

                            Text(
                                text = "$nap .- ${vasar.nev} - ${vasar.hely} - ${vasar.bevetel} Ft",
                                modifier = Modifier.padding(vertical = 4.dp),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }

    // 🔵 POPUP
    selectedHonap?.let { honap ->

        AlertDialog(
            onDismissRequest = { selectedHonap = null },
            confirmButton = {},
            title = {
                Text(
                    text = "${honap.ev} - ${honapNeve(honap.honap)}",
                    style = MaterialTheme.typography
                        .solidGlow()
                        .copy(fontSize = 20.sp)

                )
            },
            text = {

                LazyColumn {

                    items(honap.vasarok) { vasar ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                        ) {

                            Column(modifier = Modifier.padding(12.dp)) {

                                Text(
                                    text = "${vasar.nev} - ${vasar.hely}",
                                    style = MaterialTheme.typography
                                        .solidGlow()
                                        .copy(fontSize = 20.sp)
                                )

                                Text("${vasar.datum} - ${vasar.bevetel} Ft")


                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {

                                    AppButton(
                                        onClick = {
                                            onOpenVasar(vasar.id)
                                            selectedHonap = null
                                            onNavigateHome()
                                        },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text = "Megnyit",

                                        )
                                    }

                                    AppButton(
                                        onClick = {
                                            selectedVasar = vasar
                                        },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text = "Szerkeszt",

                                        )
                                    }
                                }

                            }
                        }
                    }
                }
            }
        )
    }

    selectedVasar?.let { vasar ->

        VasarEditDialog(
            vasar = vasar,
            onSave = { updatedVasar ->
                onUpdateVasar(updatedVasar)
                selectedVasar = null
                selectedHonap = null
            },
            onDelete = { vasarToDelete ->
                onDeleteVasar(vasarToDelete)
                selectedVasar = null
                selectedHonap = null
            },
            onDismiss = {
                selectedVasar = null
            }
        )
    }



}

// 🔵 HÓNAP NÉV
private fun honapNeve(honap: Int): String {
    return when (honap) {
        1 -> "Január"
        2 -> "Február"
        3 -> "Március"
        4 -> "Április"
        5 -> "Május"
        6 -> "Június"
        7 -> "Július"
        8 -> "Augusztus"
        9 -> "Szeptember"
        10 -> "Október"
        11 -> "November"
        12 -> "December"
        else -> ""
    }
}


