package com.example.kalmarium.ui.screen.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kalmarium.data.VasarEntity
import com.example.kalmarium.data.EladasEntity
import com.example.kalmarium.ui.dialog.VasarEditDialog
import com.example.kalmarium.ui.theme.AppButton
import com.example.kalmarium.ui.theme.AppSecondaryButton
import com.example.kalmarium.ui.theme.blackGlow
import com.example.kalmarium.ui.theme.solidGlow
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MainScreen(
    activeVasar: VasarEntity?,
    vasarLista: List<VasarEntity>,
    bevetel: Int,
    eladasLista: List<EladasEntity>,
    onUjVasarClick: () -> Unit,
    onVasarSelected: (VasarEntity) -> Unit,
    onEladasClick: (VasarEntity) -> Unit,
    onTetelesEladasClick: (VasarEntity) -> Unit,
    onVasarUpdate: (VasarEntity) -> Unit,
    onEladasokClick: (VasarEntity) -> Unit,

    onVasarDelete: (VasarEntity) -> Unit
) {

    var showVasarDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

    val timeFormatter = remember {
        SimpleDateFormat("HH:mm", Locale.getDefault())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(120.dp))



        Spacer(modifier = Modifier.height(8.dp))

        if (activeVasar == null) {
            Spacer(modifier = Modifier.height(80.dp))
            Text(
                text = "Nincs aktív vásár.",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            AppButton(
                onClick = { onUjVasarClick() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Vásár létrehozása")
            }

        } else {

            // 🔵 AKTÍV VÁSÁR GOMB (EDIT)

            Text(
                text = "Megnyitott vásár:",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )

            AppSecondaryButton(
                onClick = { showEditDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 90.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = activeVasar.nev,
                        style = MaterialTheme.typography
                            .solidGlow()
                            .copy(fontSize = 30.sp),
                    )

                    Text(
                        text = "${activeVasar.datum} • ${activeVasar.hely}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }



            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Bevétel: $bevetel Ft",
                style = MaterialTheme.typography
                    .solidGlow()
                    .copy(fontSize = 24.sp),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))


            Spacer(modifier = Modifier.height(16.dp))

            val rendezettEladasok =
                eladasLista.sortedByDescending { it.timestamp }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 180.dp)
            ) {
                items(rendezettEladasok) { eladas ->

                    val ido = timeFormatter.format(Date(eladas.timestamp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 0.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "$ido")
                        Text(
                            text = "${eladas.termekNev} - (${eladas.kategoria})",
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 20.dp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(text = "${eladas.eladasiAr} Ft")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                AppButton(
                    onClick = { onEladasClick(activeVasar) },
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp)
                ) {
                    Text(
                        text ="Eladás",
                        style = MaterialTheme.typography.blackGlow
                    )
                }

                AppButton(
                    onClick = { onTetelesEladasClick(activeVasar) },
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp)
                ) {
                    Text(
                        text = "Tételes eladás",
                        style = MaterialTheme.typography.blackGlow
                    )
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            AppSecondaryButton(
                onClick = {
                    onEladasokClick(activeVasar)
                },
                modifier = Modifier.fillMaxWidth()
                    .alpha(0.7f)
            ) {

                Text(
                    text ="Eladások törlése",
                    style = MaterialTheme.typography.solidGlow()
                )
            }
        }
    }

    // ===============================
    // Vásár választó dialog
    // ===============================

    if (showVasarDialog) {
        AlertDialog(
            onDismissRequest = { showVasarDialog = false },
            confirmButton = {},
            title = { Text("Vásár kiválasztása") },
            text = {
                LazyColumn {

                    item {
                        Button(
                            onClick = {
                                onUjVasarClick()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Text("Új vásár")
                        }
                    }

                    item { Spacer(modifier = Modifier.height(12.dp)) }

                    items(vasarLista) { vasar ->
                        Button(
                            onClick = {
                                onVasarSelected(vasar)
                                showVasarDialog = false
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Text("${vasar.nev} - ${vasar.datum}")
                        }
                    }
                }
            }
        )
    }

    // ===============================
    // Vásár szerkesztő dialog
    // ===============================

    if (showEditDialog && activeVasar != null) {

        VasarEditDialog(
            vasar = activeVasar,
            onSave = {
                onVasarUpdate(it)
                showEditDialog = false
            },
            onDelete = {
                onVasarDelete(it)
                showEditDialog = false
            },
            onDismiss = {
                showEditDialog = false
            }
        )
    }
}
