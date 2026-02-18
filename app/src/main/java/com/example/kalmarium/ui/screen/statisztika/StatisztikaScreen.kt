package com.example.kalmarium.ui.screen.statisztika

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.kalmarium.utils.PdfExportManager
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisztikaScreen(
    viewModel: StatisztikaViewModel,
    onBackClick: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var selectedTab by remember { mutableStateOf("Népszerű termékek") }
    var expanded by remember { mutableStateOf(false) }

    var showFromPicker by remember { mutableStateOf(false) }
    var showToPicker by remember { mutableStateOf(false) }

    var exportedUri by remember { mutableStateOf<Uri?>(null) }

    val fromPickerState = rememberDatePickerState()
    val toPickerState = rememberDatePickerState()

    val dateFormat = remember {
        SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // =======================
        // DROPDOWN
        // =======================

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {

            TextField(
                value = selectedTab,
                onValueChange = {},
                readOnly = true,
                label = { Text("Statisztika") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                listOf(
                    "Aktív vásár",
                    "Népszerű termékek",
                    "Népszerű vásárok"
                ).forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            selectedTab = option
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // =======================
        // DÁTUM SZŰRÉS
        // =======================

        if (selectedTab != "Aktív vásár") {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large
            ) {
                Column(Modifier.padding(16.dp)) {

                    Text(
                        "Dátum szűrés",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(Modifier.height(12.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = { showFromPicker = true },
                            modifier = Modifier.weight(1f)
                        ) { Text("Kezdő dátum") }

                        Button(
                            onClick = { showToPicker = true },
                            modifier = Modifier.weight(1f)
                        ) { Text("Záró dátum") }
                    }

                    Spacer(Modifier.height(12.dp))

                    Text(
                        "${uiState.fromDate?.let { dateFormat.format(Date(it)) } ?: "-"}  –  " +
                                "${uiState.toDate?.let { dateFormat.format(Date(it)) } ?: "-"}"
                    )
                }
            }

            Spacer(Modifier.height(20.dp))
        }

        // =======================
        // TARTALOM
        // =======================

        when (selectedTab) {

            "Népszerű termékek" -> {

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(uiState.termekStat) { index, stat ->

                        val medal = when (index) {
                            0 -> "🥇"
                            1 -> "🥈"
                            2 -> "🥉"
                            else -> ""
                        }

                        Card {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                // BAL OLDAL (medál + név + kategória)
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    if (medal.isNotEmpty()) {
                                        Text(text = medal)
                                        Spacer(Modifier.width(6.dp))
                                    }

                                    Text(
                                        text = stat.termekNev,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    Spacer(Modifier.width(6.dp))

                                    Text(
                                        text = "(${stat.kategoria})",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                // JOBB OLDAL (darab + bevétel)
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("${stat.osszesDarab} db")

                                    Spacer(Modifier.width(8.dp))

                                    Text(
                                        "${stat.osszesBevetel} Ft",
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }

                    }
                }

                Spacer(Modifier.height(16.dp))

                BottomButtons(
                    onBackClick = onBackClick,
                    onPdfClick = {
                        val lines = uiState.termekStat.map {
                            "${it.termekNev} – ${it.osszesDarab} db – ${it.osszesBevetel} Ft"
                        }

                        exportedUri = PdfExportManager.exportSimpleListPdf(
                            context,
                            "Nepszeru_Termekek",
                            lines
                        )
                    }
                )
            }

            "Népszerű vásárok" -> {

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(uiState.vasarStat) { index, stat ->

                        val medal = when (index) {
                            0 -> "🥇"
                            1 -> "🥈"
                            2 -> "🥉"
                            else -> ""
                        }

                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(Modifier.padding(5.dp)) {
                                Text(
                                    "$medal ${stat.nev} - ${stat.hely}",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    )
                                )

                                Text("${stat.bevetel} Ft - ${stat.datum}")
                            }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                BottomButtons(
                    onBackClick = onBackClick,
                    onPdfClick = {
                        val lines = uiState.vasarStat.map {
                            "${it.nev} – ${it.hely} – ${it.datum} – ${it.bevetel} Ft"
                        }

                        exportedUri = PdfExportManager.exportSimpleListPdf(
                            context,
                            "Nepszeru_Vasarok",
                            lines
                        )
                    }
                )
            }

            "Aktív vásár" -> {

                val vasar = uiState.activeVasar

                if (vasar == null) {

                    Text("Nincs aktív vásár.")
                    Spacer(Modifier.weight(1f))

                    BottomButtons(
                        onBackClick = onBackClick,
                        onPdfClick = {}
                    )

                } else {

                    val profit = vasar.bevetel - vasar.koltseg
                    val profitColor =
                        if (profit >= 0)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.error

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        // ===== HEADER =====

                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = MaterialTheme.shapes.large,
                                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {

                                    Text(
                                        text = vasar.nev,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Text(
                                        text = "${vasar.hely} • ${vasar.datum}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )

                                    Spacer(Modifier.height(5.dp))

                                    Text(
                                        text = "${vasar.bevetel} Ft",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Text(
                                        text = "Bevétel",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )

                                    Spacer(Modifier.height(5.dp))

                                    Row(
                                        horizontalArrangement = Arrangement.SpaceEvenly,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text("Költség")
                                            Text("${vasar.koltseg} Ft")
                                        }

                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text("Profit")
                                            Text(
                                                "$profit Ft",
                                                color = profitColor,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // ===== ELADÁSOK =====

                        items(uiState.activeEladasok) { eladas ->

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                Column {
                                    Text(
                                        eladas.termekNev,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        "${eladas.mennyiseg} db",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Text(
                                    "${eladas.mennyiseg * eladas.eladasiAr} Ft",
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Divider()
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    BottomButtons(
                        onBackClick = onBackClick,
                        onPdfClick = {

                            exportedUri = PdfExportManager.exportVasarToPdf(
                                context = context,
                                vasarNev = vasar.nev,
                                datum = vasar.datum,
                                hely = vasar.hely,
                                bevetel = vasar.bevetel,
                                koltseg = vasar.koltseg,
                                eladasLista = uiState.activeEladasok
                            )
                        }
                    )

                }
            }


        }
    }

    // =======================
    // PDF MEGNYITÁS DIALOG
    // =======================

    if (exportedUri != null) {

        AlertDialog(
            onDismissRequest = { exportedUri = null },
            title = { Text("PDF mentve") },
            text = { Text("Meg szeretnéd nyitni?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            setDataAndType(exportedUri, "application/pdf")
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }

                        try {
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(
                                context,
                                "Nincs PDF megjelenítő alkalmazás",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        exportedUri = null
                    }
                ) { Text("Megnyitás") }
            },
            dismissButton = {
                TextButton(
                    onClick = { exportedUri = null }
                ) { Text("Mégse") }
            }
        )
    }

    // =======================
    // DATE PICKER DIALOGOK
    // =======================

    if (showFromPicker) {
        DatePickerDialog(
            onDismissRequest = { showFromPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.setFromDate(fromPickerState.selectedDateMillis)
                    showFromPicker = false
                }) { Text("OK") }
            }
        ) { DatePicker(state = fromPickerState) }
    }

    if (showToPicker) {
        DatePickerDialog(
            onDismissRequest = { showToPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.setToDate(toPickerState.selectedDateMillis)
                    showToPicker = false
                }) { Text("OK") }
            }
        ) { DatePicker(state = toPickerState) }
    }
}

@Composable
private fun BottomButtons(
    onBackClick: () -> Unit,
    onPdfClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Button(
            onClick = onPdfClick,
            modifier = Modifier.weight(1f)
        ) {
            Text("📄 PDF export")
        }

        OutlinedButton(
            onClick = onBackClick,
            modifier = Modifier.weight(1f)
        ) {
            Text("Vissza")
        }
    }
}
