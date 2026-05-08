package com.example.habisin.ui.view.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.habisin.data.local.AppTheme
import com.example.habisin.ui.theme.HabisinLime
import com.example.habisin.ui.theme.HabisinOlive
import com.example.habisin.ui.theme.HabisinTextDark
import com.example.habisin.ui.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppThemeScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = viewModel()
) {
    val current by viewModel.theme.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title          = { Text("App Theme", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(20.dp)
        ) {
            Text(
                "Pick how Habisin looks. \"System\" follows your device setting.",
                color    = HabisinTextDark,
                fontSize = 14.sp
            )
            Spacer(Modifier.height(20.dp))

            ThemeOption("Light",  current == AppTheme.LIGHT)  { viewModel.setTheme(AppTheme.LIGHT) }
            Spacer(Modifier.height(12.dp))
            ThemeOption("Dark",   current == AppTheme.DARK)   { viewModel.setTheme(AppTheme.DARK) }
            Spacer(Modifier.height(12.dp))
            ThemeOption("System", current == AppTheme.SYSTEM) { viewModel.setTheme(AppTheme.SYSTEM) }
        }
    }
}

@Composable
private fun ThemeOption(label: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (selected) HabisinLime else Color(0xFFF2F2F2))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label,
            color      = HabisinTextDark,
            fontSize   = 15.sp,
            fontWeight = FontWeight.Medium,
            modifier   = Modifier.weight(1f)
        )
        if (selected) {
            Icon(Icons.Default.Check, contentDescription = "Selected", tint = HabisinOlive)
        }
    }
}