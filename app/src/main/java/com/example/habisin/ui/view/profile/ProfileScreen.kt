package com.example.habisin.ui.view.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.habisin.ui.view.component.GroupCard
import com.example.habisin.ui.view.component.SectionTitle
import com.example.habisin.ui.view.component.SettingRow
import com.example.habisin.ui.theme.HabisinTheme
import com.example.habisin.ui.uistate.ProfileUiState
import com.example.habisin.ui.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    onLoggedOut: () -> Unit,
    onNavigateToLanguage: () -> Unit,
    onNavigateToTheme: () -> Unit,
    onNavigateToNotification: () -> Unit,
    onNavigateToFaq: () -> Unit,
    onNavigateToAbout: () -> Unit,
    onNavigateToSubscription: () -> Unit = {},
    viewModel: ProfileViewModel = viewModel()
) {
    val state = viewModel.profileUiState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        // ── Account ──
        SectionTitle("Account")
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(HabisinTheme.colors.peachCard, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            when (state) {
                is ProfileUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                }
                is ProfileUiState.Error -> {
                    Text(state.message, color = HabisinTheme.colors.onPeachCard)
                }
                is ProfileUiState.Success -> {
                    Column {
                        Text(state.username, color = HabisinTheme.colors.onPeachCard, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(Modifier.height(2.dp))
                        Text(state.email, color = HabisinTheme.colors.onPeachCard.copy(alpha = 0.7f), fontSize = 13.sp)
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // ── Upgrade To Pro ──
        UpgradeToProCard(onClick = onNavigateToSubscription)

        Spacer(Modifier.height(28.dp))

        // ── Preferences ──
        SectionTitle("Preferences")
        GroupCard(background = HabisinTheme.colors.limeCard) {
            SettingRow(
                icon     = Icons.Default.Language,
                title    = "App Language",
                subtitle = "Select the language of the app",
                onClick  = onNavigateToLanguage
            )
            SettingRow(
                icon     = Icons.Default.Palette,
                title    = "App Theme",
                subtitle = "Adjust the app theme",
                onClick  = onNavigateToTheme
            )
            SettingRow(
                icon     = Icons.Default.Notifications,
                title    = "Notification",
                subtitle = "Change notification system",
                onClick  = onNavigateToNotification
            )
        }

        Spacer(Modifier.height(28.dp))

        // ── Help ──
        SectionTitle("Help")
        GroupCard(background = HabisinTheme.colors.limeCard) {
            SettingRow(
                icon    = Icons.AutoMirrored.Filled.HelpOutline,
                title   = "FAQ",
                onClick = onNavigateToFaq
            )
            SettingRow(
                icon    = Icons.Default.Info,
                title   = "About Habisin!",
                onClick = onNavigateToAbout
            )
        }

        Spacer(Modifier.height(32.dp))

        // ── Logout ──
        Button(
            onClick  = { viewModel.logout(onLoggedOut) },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape  = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = HabisinTheme.colors.action,
                contentColor   = HabisinTheme.colors.onAction
            )
        ) {
            Text("LOGOUT", fontWeight = FontWeight.Bold, fontSize = 16.sp, letterSpacing = 1.sp)
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun UpgradeToProCard(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(HabisinTheme.colors.limeCard)
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(HabisinTheme.colors.action),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector        = Icons.Default.WorkspacePremium,
                contentDescription = null,
                tint               = HabisinTheme.colors.onAction,
                modifier           = Modifier.size(24.dp)
            )
        }
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                "Upgrade To Pro",
                color = HabisinTheme.colors.onLimeCard,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                "Buka semua resep eksklusif & masak tanpa batas",
                color = HabisinTheme.colors.onLimeCard.copy(alpha = 0.75f),
                fontSize = 12.sp
            )
        }
        Icon(
            imageVector        = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint               = HabisinTheme.colors.onLimeCard
        )
    }
}