package com.example.kalmarium.ui.screen.eladasok

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kalmarium.data.EladasEntity
import com.example.kalmarium.data.VasarEntity
import com.example.kalmarium.data.repository.EladasRepository
import com.example.kalmarium.data.repository.VasarRepository
import android.content.Intent
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton



@Composable
fun PdfExportScreen(
    onBackClick: () -> Unit,
    viewModel: PdfExportViewModel,
    vasar: VasarEntity,
    eladasLista: List<EladasEntity>,
    bevetel: Int,
    koltseg: Int
) {

    val context = LocalContext.current
    val exportedUri by viewModel.exportedUri.collectAsState()
    var showOpenDialog by remember { mutableStateOf(false) }

    LaunchedEffect(exportedUri) {
        if (exportedUri != null) {
            showOpenDialog = true
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Button(
            onClick = {
                viewModel.exportAktivVasar(
                    context = context,
                    vasar = vasar,
                    eladasLista = eladasLista,
                    bevetel = bevetel,   // <-- EZ HIÁNYZOTT
                    koltseg = koltseg
                )

            }
        ) {
            Text("Aktív vásár PDF export")
        }

        exportedUri?.let {
            Text("PDF sikeresen létrehozva!")
        }
    }
    if (showOpenDialog && exportedUri != null) {

        AlertDialog(
            onDismissRequest = { showOpenDialog = false },
            title = { Text("PDF elkészült") },
            text = { Text("Szeretnéd megnyitni a PDF fájlt?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showOpenDialog = false

                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            setDataAndType(exportedUri, "application/pdf")
                            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                        }

                        try {
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(
                                context,
                                "Nincs PDF megnyitó alkalmazás!",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                ) {
                    Text("Megnyitás")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showOpenDialog = false }
                ) {
                    Text("Mégse")
                }
            }
        )
    }

}

