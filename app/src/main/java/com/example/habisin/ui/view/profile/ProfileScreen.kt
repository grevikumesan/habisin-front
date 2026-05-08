package com.example.habisin.ui.view.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.habisin.ui.component.GroupCard
import com.example.habisin.ui.component.SectionTitle
import com.example.habisin.ui.component.SettingRow
import com.example.habisin.ui.theme.HabisinCoral
import com.example.habisin.ui.theme.HabisinLime
import com.example.habisin.ui.theme.HabisinPeach
import com.example.habisin.ui.theme.HabisinTextDark
import com.example.habisin.ui.theme.HabisinTextMuted
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
    viewModel: ProfileViewModel = viewModel()
) {
    val state = viewModel.profileUiState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        // ── Account ──
        SectionTitle("Account")
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(HabisinPeach, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            when (state) {
                is ProfileUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                }
                is ProfileUiState.Error -> {
                    Text(state.message, color = HabisinTextDark)
                }
                is ProfileUiState.Success -> {
                    Column {
                        Text(state.username, color = HabisinTextDark, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(Modifier.height(2.dp))
                        Text(state.email, color = HabisinTextMuted, fontSize = 13.sp)
                    }
                }
            }
        }

        Spacer(Modifier.height(28.dp))

        // ── Preferences ──
        SectionTitle("Preferences")
        GroupCard(background = HabisinLime) {
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
        GroupCard(background = HabisinLime) {
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
                containerColor = HabisinCoral,
                contentColor   = Color.White
            )
        ) {
            Text("LOGOUT", fontWeight = FontWeight.Bold, fontSize = 16.sp, letterSpacing = 1.sp)
        }

        Spacer(Modifier.height(24.dp))
    }
}