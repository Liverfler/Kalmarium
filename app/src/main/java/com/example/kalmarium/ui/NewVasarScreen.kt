package com.example.kalmarium.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.kalmarium.data.VasarEntity
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewVasarScreen(
    onBackClick: () -> Unit,
    onSave: (VasarEntity) -> Unit
) {

    var vasarNev by remember { mutableStateOf("") }
    var vasarHely by remember { mutableStateOf("") }
    var koltseg by remember { mutableStateOf("") }

    val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
    val maiDatum = LocalDate.now().format(formatter)

    var datum by remember { mutableStateOf(maiDatum) }
    var datumHiba by remember { mutableStateOf(false) }

    fun formatDatum(input: String): String {
        val numbers = input.filter { it.isDigit() }.take(8)

        return buildString {
            for (i in numbers.indices) {
                append(numbers[i])
                if (i == 3 || i == 5) append(".")
            }
        }
    }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {

        Text("Új vásár")

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = vasarNev,
            onValueChange = { vasarNev = it },
            label = { Text("Vásár neve") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = vasarHely,
            onValueChange = { vasarHely = it },
            label = { Text("Helyszín") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = datum,
            onValueChange = {
                datum = formatDatum(it)
                datumHiba = false
            },
            label = { Text("Dátum (yyyy.MM.dd)") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            isError = datumHiba,
            modifier = Modifier.fillMaxWidth()
        )

        if (datumHiba) {
            Text(
                text = "Hibás dátumformátum!",
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = koltseg,
            onValueChange = { koltseg = it },
            label = { Text("Költség (Ft)") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {

                try {
                    LocalDate.parse(datum, formatter)

                    val koltsegInt = koltseg.toIntOrNull() ?: 0

                    val ujVasar = VasarEntity(
                        id = 0, // fontos: Room generálja
                        nev = vasarNev,
                        hely = vasarHely,
                        datum = datum,
                        koltseg = koltsegInt
                    )

                    onSave(ujVasar)
                    onBackClick()

                } catch (e: DateTimeParseException) {
                    datumHiba = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Mentés")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Vissza")
        }
    }
}
