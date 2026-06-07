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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.habisin.ui.theme.HabisinTheme

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
        placeholder = { Text(text = placeholder, color = HabisinTheme.colors.fieldHint) },
        leadingIcon = leadingIcon,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor   = HabisinTheme.colors.fieldBg,
            unfocusedContainerColor = HabisinTheme.colors.fieldBg,
            focusedBorderColor      = Color.Transparent,   // Tanpa garis pinggir
            unfocusedBorderColor    = Color.Transparent,
            focusedTextColor        = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor      = MaterialTheme.colorScheme.onSurface,
            cursorColor             = MaterialTheme.colorScheme.primary
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