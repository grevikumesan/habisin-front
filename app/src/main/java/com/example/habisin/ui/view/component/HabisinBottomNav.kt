package com.example.habisin.ui.view.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
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
import com.example.habisin.ui.theme.HabisinTheme

@Composable
fun HabisinBottomNav(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    onPlusClick: () -> Unit
) {
    // Box wraps the bar's height (no leftover transparent strip). The FAB is offset
    // upward so it straddles the bar's top edge; only the FAB itself overflows above.
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .background(HabisinTheme.colors.navBar)
                .navigationBarsPadding()              // sit above the real system nav inset
                .padding(top = 16.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            NavIcon(Icons.Default.Home,    "Home",    currentRoute == "Home")    { onNavigate("Home") }
            NavIcon(Icons.Default.Kitchen, "Fridge",  currentRoute == "Fridge")  { onNavigate("Fridge") }

            Spacer(modifier = Modifier.width(72.dp))  // gap for the FAB

            NavIcon(Icons.AutoMirrored.Filled.MenuBook, "Recipe",  currentRoute == "Recipe")  { onNavigate("Recipe") }
            NavIcon(Icons.Default.Person,                "Profile", currentRoute == "Profile") { onNavigate("Profile") }
        }

        FloatingActionButton(
            onClick        = onPlusClick,
            shape          = CircleShape,
            containerColor = HabisinTheme.colors.fab,
            contentColor   = HabisinTheme.colors.onFab,
            modifier       = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-24).dp)                 // raise so it sits on the bar's top edge
                .size(60.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(32.dp))
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
        tint               = if (isSelected) HabisinTheme.colors.navSelected else HabisinTheme.colors.onNavBar,
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