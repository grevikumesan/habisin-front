package com.example.habisin.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) Color.Gray else Color.White
        )
        Text(text = label, color = Color.White, fontSize = 10.sp)
    }
}

// Preview menggunakan background hijau tua agar warna putih/abu-abunya terlihat
@Preview(showBackground = true, backgroundColor = 0xFF1B4332)
@Composable
fun NavItemPreview() {
    Row {
        // Contoh ketika dipilih (selected)
        NavItem(icon = Icons.Default.Home, label = "Home", isSelected = true, onClick = {})
        // Contoh ketika tidak dipilih (unselected)
        NavItem(icon = Icons.Default.Home, label = "Home", isSelected = false, onClick = {})
    }
}