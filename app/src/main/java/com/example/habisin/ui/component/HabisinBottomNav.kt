package com.example.habisin.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.habisin.ui.theme.HabisinLime
import com.example.habisin.ui.theme.HabisinOlive
import com.example.habisin.ui.theme.HabisinPeach
import com.example.habisin.ui.theme.HabisinWhite

@Composable
fun HabisinBottomNav(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    onPlusClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .background(HabisinOlive)
                .padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            NavIcon(Icons.Default.Home,     "Home",    currentRoute == "Home")    { onNavigate("Home") }
            NavIcon(Icons.Default.Kitchen,  "Fridge",  currentRoute == "Fridge")  { onNavigate("Fridge") }

            Spacer(modifier = Modifier.width(64.dp))

            NavIcon(Icons.Default.MenuBook, "Recipe",  currentRoute == "Recipe")  { onNavigate("Recipe") }
            NavIcon(Icons.Default.Person,   "Profile", currentRoute == "Profile") { onNavigate("Profile") }
        }

        FloatingActionButton(
            onClick        = onPlusClick,
            shape          = CircleShape,
            containerColor = HabisinLime,
            contentColor   = HabisinOlive,
            modifier       = Modifier
                .align(Alignment.TopCenter)
                .size(64.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(36.dp))
        }
    }
}

@Composable
private fun NavIcon(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Icon(
        imageVector        = icon,
        contentDescription = label,
        tint               = if (isSelected) HabisinPeach else HabisinWhite,
        modifier           = Modifier
            .size(28.dp)
            .clickable(onClick = onClick)
    )
}

@Preview
@Composable
fun HabisinBottomNavPreview() {
    HabisinBottomNav(
        currentRoute = "Home",
        onNavigate   = {},
        onPlusClick  = {}
    )
}