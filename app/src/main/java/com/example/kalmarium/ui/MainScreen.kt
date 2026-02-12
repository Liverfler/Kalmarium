package com.example.kalmarium.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kalmarium.data.VasarEntity


@Composable
fun MainScreen(
    latestVasarList: List<VasarEntity>,
    onTermekekClick: () -> Unit,
    onUjVasarClick: () -> Unit,
    onVasarClick: (VasarEntity) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Legutóbbi vásárok:",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (latestVasarList.isEmpty()) {
            Text("Még nincs mentett vásár.")
        } else {
            latestVasarList.forEach { vasar ->
                Button(
                    onClick = { onVasarClick(vasar) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("${vasar.nev} - ${vasar.datum}")
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        // 🔥 Ez tolja lejjebb a gomb sort, de nem teljesen alulra
        Spacer(modifier = Modifier.weight(0.4f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Button(
                onClick = onTermekekClick,
                modifier = Modifier.weight(1f)
            ) {
                Text("Termékeim")
            }

            Button(
                onClick = onUjVasarClick,
                modifier = Modifier.weight(1f)
            ) {
                Text("Új vásár")
            }
        }
    }
}

