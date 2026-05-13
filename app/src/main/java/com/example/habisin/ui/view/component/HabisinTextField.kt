package com.example.habisin.ui.view.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HabisinTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = { Text(text = placeholder, color = Color.Gray) },
        leadingIcon = leadingIcon,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFF0F0F0),   // Warna abu-abu terang
            unfocusedContainerColor = Color(0xFFF0F0F0), // Warna abu-abu terang
            focusedBorderColor = Color.Transparent,      // Tanpa garis pinggir
            unfocusedBorderColor = Color.Transparent,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black
        ),
        singleLine = true
    )
}

// Tambahan Preview supaya bentuknya kelihatan di Android Studio
@Preview(showBackground = true)
@Composable
fun HabisinTextFieldPreview() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Preview Search Bar (Kosong):", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        // Contoh 1: Menampilkan Placeholder dan Icon Search
        HabisinTextField(
            value = "",
            onValueChange = {},
            placeholder = "Search Recipe",
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.Gray
                )
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("Preview Input Biasa (Terisi):", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        // Contoh 2: Saat user mengetik sesuatu (misal di halaman Login/Add)
        HabisinTextField(
            value = "Nasi Goreng",
            onValueChange = {},
            placeholder = "Enter text..."
        )
    }
}