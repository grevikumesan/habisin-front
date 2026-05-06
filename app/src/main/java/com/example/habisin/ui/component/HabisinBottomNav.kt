package com.example.habisin.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HabisinBottomNav(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    onPlusClick: () -> Unit
) {
    val darkGreen = Color(0xFF1B4332)
    val lightGreen = Color(0xFFB7E4C7)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
    ) {
        // Background Bar Hijau Tua
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(darkGreen)
                .padding(bottom = 8.dp, top = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavItem(Icons.Default.Home, "Home", currentRoute == "Home") { onNavigate("Home") }
            // Note: Menggunakan icon yang relevan karena Icons.Default.Kitchen tidak selalu ada di material standard
            // Jika error, gunakan Icons.Default.Menu atau icon lain yang ada di project kamu.
            NavItem(Icons.Default.ShoppingCart, "Fridge", currentRoute == "Fridge") { onNavigate("Fridge") }

            Spacer(modifier = Modifier.width(50.dp)) // Space untuk tombol Plus

            NavItem(Icons.Default.MenuBook, "Recipe", currentRoute == "Recipe") { onNavigate("Recipe") }
            NavItem(Icons.Default.Person, "Account", currentRoute == "Profile") { onNavigate("Profile") }
        }

        // Tombol Plus Melayang
        FloatingActionButton(
            onClick = onPlusClick,
            shape = CircleShape,
            containerColor = lightGreen,
            contentColor = darkGreen,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(60.dp)
                .offset(y = (-10).dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(35.dp))
        }
    }
}

// Preview untuk keseluruhan Bottom Navigation
@Preview(showBackground = true)
@Composable
fun HabisinBottomNavPreview() {
    HabisinBottomNav(
        currentRoute = "Home",
        onNavigate = {},
        onPlusClick = {}
    )
}