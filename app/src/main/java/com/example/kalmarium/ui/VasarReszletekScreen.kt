package com.example.kalmarium.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.kalmarium.data.EladasEntity
import com.example.kalmarium.data.TermekEntity
import com.example.kalmarium.utils.PdfExportManager
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun VasarReszletekScreen(
    vasarNev: String,
    bevetel: Int,
    koltseg: Int,
    vasarDatum: String,
    vasarHely: String,
    termekLista: List<TermekEntity>,
    eladasLista: List<EladasEntity>,
    onBackClick: () -> Unit,
    onTetelesEladasClick: () -> Unit,
    onEladasRogzit: (TermekEntity) -> Unit,
    onDeleteEladasScreenClick: () -> Unit,
    onDeleteVasarClick: () -> Unit,
    showTetelesSiker: Boolean,
    onTetelesSikerConsumed: () -> Unit
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    var showExportDialog by remember { mutableStateOf(false) }
    var exportedPdfUri by remember { mutableStateOf<Uri?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    var selectedKategoria by remember { mutableStateOf<String?>(null) }
    var eladvaUzenet by remember { mutableStateOf<String?>(null) }
    var showEladasok by remember { mutableStateOf(false) }

    val profit = bevetel - koltseg

    val profitUzenet = when {
        profit < 0 -> "Még a költségekre se elég"
        profit < 10000 -> "Ettől még nem kerestél jobban mint egy napszámos!"
        profit < 20000 -> "Ez miatt se kér kölcsön a Lölő tőled."
        profit < 30000 -> "Talán ma már nem halsz éhen"
        profit < 50000 -> "Már majdnem meg van egy KATA befizetés"
        profit < 75000 -> "Megérős volt felkelni reggel?"
        profit < 100000 -> "Jóvanmáá!"
        else -> "Pakoljá össze azt mennyé haza!"
    }

    LaunchedEffect(showTetelesSiker) {
        if (showTetelesSiker) {
            snackbarHostState.showSnackbar("Sikeres tételes eladás")
            onTetelesSikerConsumed()
        }
    }

    val kategoriak = termekLista.map { it.kategoria }.distinct()

    val dateFormat = remember {
        SimpleDateFormat("HH:mm", Locale.getDefault())
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            Column(modifier = Modifier.weight(1f)) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text("Vásár: $vasarNev")

                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Text(
                            text = "Nyomtatható PDF:",
                            style = MaterialTheme.typography.bodySmall
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = {
                                val uri = PdfExportManager.exportVasarToPdf(
                                    context,
                                    vasarNev,
                                    vasarDatum,
                                    vasarHely,
                                    bevetel,
                                    koltseg,
                                    eladasLista,
                                    termekLista
                                )

                                if (uri != null) {
                                    exportedPdfUri = uri
                                    showExportDialog = true
                                }
                            }
                        ) {
                            Icon(Icons.Default.PictureAsPdf, contentDescription = "PDF")
                        }
                    }
                }


                Text("Bevétel: $bevetel Ft")
                Text("Költség: $koltseg Ft")
                Text("Profit: $profit Ft")

                Spacer(modifier = Modifier.height(16.dp))

                if (!showEladasok) {

                    if (selectedKategoria == null) {

                        Text("Eladott termék hozzáadása:")

                        LazyColumn {
                            items(kategoriak) { kategoria ->
                                Button(
                                    onClick = { selectedKategoria = kategoria },
                                    modifier = Modifier.fillMaxWidth()
                                ) { Text(kategoria) }
                            }
                        }

                    } else {

                        Button(
                            onClick = { selectedKategoria = null },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("← Vissza kategóriákhoz")
                        }

                        val termekek = termekLista.filter {
                            it.kategoria == selectedKategoria
                        }

                        LazyColumn {
                            items(termekek) { termek ->
                                Button(
                                    onClick = {
                                        onEladasRogzit(termek)
                                        eladvaUzenet =
                                            "${termek.nev} eladva ${termek.ar} Ft-ért\n$profitUzenet"
                                        selectedKategoria = null
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("${termek.nev} - ${termek.ar} Ft")
                                }
                            }
                        }
                    }

                } else {

                    Text("Eladások:")

                    LazyColumn {
                        items(eladasLista.sortedByDescending { it.timestamp }) { eladas ->
                            val ido = dateFormat.format(Date(eladas.timestamp))
                            val ar = termekLista
                                .firstOrNull { it.nev == eladas.termekNev }
                                ?.ar ?: 0

                            Text("$ido - ${eladas.termekNev} - $ar Ft")
                        }
                    }
                }

                if (eladvaUzenet != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(eladvaUzenet!!)
                    LaunchedEffect(eladvaUzenet) {
                        delay(4000)
                        eladvaUzenet = null
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ===== 1. SOR =====
            Row(modifier = Modifier.fillMaxWidth()) {

                Button(
                    onClick = onBackClick,
                    modifier = Modifier.weight(1f)
                ) { Text("Főmenü") }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = { showEladasok = true },
                    modifier = Modifier.weight(1f)
                ) { Text("Eladások") }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = onTetelesEladasClick,
                    modifier = Modifier.weight(1f)
                ) { Text("Tételes eladás") }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ===== 2. SOR =====
            Row(modifier = Modifier.fillMaxWidth()) {

                Button(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) { Text("Vásár törlése") }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = onDeleteEladasScreenClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) { Text("Eladás törlése") }
            }
        }
    }

    // ===== PDF DIALOG =====
    if (showExportDialog) {
        AlertDialog(
            onDismissRequest = { showExportDialog = false },
            title = { Text("Exportálás kész.") },
            text = { Text("A PDF mentve a Letöltések mappába.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        exportedPdfUri?.let { uri ->
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                setDataAndType(uri, "application/pdf")
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                            context.startActivity(intent)
                        }
                    }
                ) { Text("PDF megnyitása") }
            },
            dismissButton = {
                TextButton(
                    onClick = { showExportDialog = false }
                ) { Text("Bezár") }
            }
        )
    }

    // ===== VÁSÁR TÖRLÉS DIALOG =====
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Biztos törlöd?") },
            text = { Text("Ez a vásár és az összes eladása törlődni fog!") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDeleteVasarClick()
                    }
                ) { Text("Igen, törlöm") }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) { Text("Mégse") }
            }
        )
    }
}
