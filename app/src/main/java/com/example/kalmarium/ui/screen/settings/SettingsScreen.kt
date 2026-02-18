package com.example.kalmarium.ui.screen.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import com.example.kalmarium.ui.theme.*
import androidx.compose.material3.ExperimentalMaterial3Api

import androidx.compose.ui.graphics.Color


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBackClick: () -> Unit
) {

    var nameInput by remember { mutableStateOf(viewModel.userName) }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("Beállítások") }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            // -----------------------------
            // USERNAME SECTION
            // -----------------------------

            Text(
                text = "Felhasználónév",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = nameInput,
                onValueChange = { nameInput = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words
                )
            )



            Spacer(modifier = Modifier.height(32.dp))

            // -----------------------------
            // THEME SECTION
            // -----------------------------

            Text(
                text = "Téma kiválasztása",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            AppThemeType.entries.forEach { theme ->

                ThemeOptionItem(
                    theme = theme,
                    selected = theme == viewModel.currentTheme,
                    onClick = { viewModel.changeTheme(theme) }
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.weight(1f))

            AppButton(
                onClick = {
                    viewModel.changeUserName(nameInput)
                    onBackClick()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Mentés")
            }
        }
    }
}

@Composable
private fun ThemeOptionItem(
    theme: AppThemeType,
    selected: Boolean,
    onClick: () -> Unit
) {

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = if (selected)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (selected) 6.dp else 2.dp
        )
    ) {

        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(
                        color = Color(theme.primaryColor)
                    )
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = theme.displayName,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )

            if (selected) {
                Text(
                    text = "Aktív",
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
