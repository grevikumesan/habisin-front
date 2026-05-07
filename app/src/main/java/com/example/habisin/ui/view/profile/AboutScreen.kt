package com.example.habisin.ui.view.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.habisin.ui.components.LogoPlaceholder
import com.example.habisin.ui.theme.HabisinTextDark
import com.example.habisin.ui.theme.HabisinTextMuted

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title          = { Text("About Habisin!", fontWeight = FontWeight.SemiBold) },
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
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LogoPlaceholder()

            Spacer(Modifier.height(16.dp))

            Text("Habisin!", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = HabisinTextDark)
            Spacer(Modifier.height(4.dp))
            Text("Segera Dihabiskan", color = HabisinTextMuted, fontSize = 13.sp)

            Spacer(Modifier.height(20.dp))

            Text(
                "Habisin is a kitchen companion app that helps you keep track of what's in your fridge, " +
                        "discover recipes that use what you already have, and reduce food waste at home.",
                color     = HabisinTextDark,
                fontSize  = 14.sp
            )

            Spacer(Modifier.height(20.dp))

            InfoLine("Version", "1.0.0")
            InfoLine("Build",   "May 2026")
            InfoLine("Made by", "The Habisin Team")

            Spacer(Modifier.height(24.dp))

            Text("© 2026 Habisin. All rights reserved.", color = HabisinTextMuted, fontSize = 12.sp)
        }
    }
}

@Composable
private fun InfoLine(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(label, color = HabisinTextMuted, fontSize = 13.sp, modifier = Modifier.weight(1f))
        Text(value, color = HabisinTextDark, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}