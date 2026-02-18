package com.example.kalmarium.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.kalmarium.ui.theme.blackGlow

@Composable
fun MenuDialog(
    onDismiss: () -> Unit,
    onMainClick: () -> Unit,
    onUjVasarClick: () -> Unit,
    onVasarSzerkesztesClick: () -> Unit,
    onCsoportosClick: () -> Unit,
    onEladasokClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onKeszletClick: () -> Unit,
    onTermekekClick: () -> Unit,

    onPdfClick: () -> Unit


) {

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Menü",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                HorizontalDivider(
                    thickness = 2.dp,
                    color = MaterialTheme.colorScheme.outline
                )

                Spacer(modifier = Modifier.height(8.dp))


                Button(
                    onClick = {
                        onMainClick()
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text(
                        text =" \uD83C\uDFE0 Főoldal",
                        style = MaterialTheme.typography.blackGlow
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))
                HorizontalDivider(
                    thickness = 2.dp,
                    color = MaterialTheme.colorScheme.outline
                )
                Spacer(modifier = Modifier.height(6.dp))

                Button(
                    onClick = {
                        onUjVasarClick()
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text(
                        text ="➕ Új vásár",
                        style = MaterialTheme.typography.blackGlow
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Button(
                    onClick = {
                        onVasarSzerkesztesClick()
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text(
                        text ="\uD83D\uDCC5 Vásárok",
                        style = MaterialTheme.typography.blackGlow
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))
                HorizontalDivider(
                    thickness = 2.dp,
                    color = MaterialTheme.colorScheme.outline
                )
                Spacer(modifier = Modifier.height(6.dp))


                Button(
                    onClick = onTermekekClick,
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text(
                        text ="\uD83D\uDCE6 Termékek",
                        style = MaterialTheme.typography.blackGlow
                    )
                }


                Button(
                    onClick = {
                        onCsoportosClick()
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text(
                        text ="\uD83D\uDDC2\uFE0F Gyors bevitel",
                        style = MaterialTheme.typography.blackGlow
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Button(
                    onClick = {
                        onKeszletClick()
                    },
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text(
                        text ="\uD83D\uDCCA Készletezés",
                        style = MaterialTheme.typography.blackGlow
                    )
                }

                HorizontalDivider(
                    thickness = 2.dp,
                    color = MaterialTheme.colorScheme.outline
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        onEladasokClick()
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text(
                        text ="\uD83D\uDCB0 Eladások",
                        style = MaterialTheme.typography.blackGlow
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Button(
                    onClick = {
                        onPdfClick()
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text(
                        text ="\uD83D\uDCC4 Statisztika",
                        style = MaterialTheme.typography.blackGlow
                    )
                }

                Button(
                    onClick = {
                        onDismiss()
                        onSettingsClick()
                    },
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text(
                        text ="⚙️ Beállítások",
                        style = MaterialTheme.typography.blackGlow
                    )
                }


            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Bezár")
            }
        }
    )
}
