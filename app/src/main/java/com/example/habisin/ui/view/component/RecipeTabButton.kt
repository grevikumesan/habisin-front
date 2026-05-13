package com.example.habisin.ui.view.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RecipeTabButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    // Warna Kuning Pucat untuk status terpilih (hover/selected)
    val selectedColor = Color(0xFFFFF9C4)
    val unselectedColor = Color.White
    val textColor = if (isSelected) Color(0xFF1B4332) else Color.Black

    Box(
        modifier = Modifier
            .padding(4.dp)
            .height(45.dp)
            .fillMaxWidth()
            .background(
                color = if (isSelected) selectedColor else unselectedColor,
                shape = CircleShape
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Companion.Center
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Preview
@Composable
fun RecipeTabButtonPreview() {
    Row(modifier = Modifier.width(300.dp).background(Color.Gray).padding(10.dp)) {
        Box(modifier = Modifier.weight(1f)) {
            RecipeTabButton("Ingredient", true) {}
        }
        Box(modifier = Modifier.weight(1f)) {
            RecipeTabButton("Direction", false) {}
        }
    }
}