package com.example.habisin.ui.view.dashboard

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.habisin.ui.view.component.ExpiringSoonCard
import com.example.habisin.ui.view.component.SectionHeader
import com.example.habisin.ui.view.component.StatCard
import com.example.habisin.ui.theme.HabisinCoral
import com.example.habisin.ui.theme.HabisinOlive
import com.example.habisin.ui.theme.HabisinTeal
import com.example.habisin.ui.theme.HabisinTextDark
import com.example.habisin.ui.theme.HabisinTextMuted
import com.example.habisin.ui.uistate.DashboardUiState
import com.example.habisin.ui.viewmodel.DashboardViewModel

import com.example.habisin.ui.model.ProductModel

@Composable
fun DashboardScreen(
    onPlusClick: () -> Unit = {},
    onItemClick: (ProductModel) -> Unit = {},
    onViewAllClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    viewModel: DashboardViewModel = viewModel()
) {
    val state = viewModel.dashboardUiState

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let { viewModel.updateProfilePicture(it) }
    }

    when (state) {
        is DashboardUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is DashboardUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(state.message, color = HabisinTextDark)
            }
        }
        is DashboardUiState.Success -> {
            DashboardContent(
                state          = state,
                onProfileClick = {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                onItemClick    = onItemClick,
                onViewAllClick = onViewAllClick
            )
        }
    }
}

@Composable
private fun DashboardContent(
    state: DashboardUiState.Success,
    onProfileClick: () -> Unit,
    onItemClick: (ProductModel) -> Unit,
    onViewAllClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // ── Header: Welcome + Profile ──
        Row(
            modifier              = Modifier.fillMaxWidth(),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text       = "Welcome, ${state.username}",
                    color      = HabisinTextDark,
                    fontSize   = 26.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text     = "It's time to empty your fridge!",
                    color    = HabisinTextMuted,
                    fontSize = 13.sp
                )
            }

            Spacer(Modifier.width(16.dp))

            ProfileAvatar(
                imageUri = state.profilePictureUri,
                onClick  = onProfileClick
            )
        }

        Spacer(Modifier.height(20.dp))

        // ── Expiring Soon ──
        ExpiringSoonCard(
            items       = state.expiringItems,
            onItemClick = onItemClick
        )
        Spacer(Modifier.height(24.dp))

        // ── At a Glance ──
        SectionHeader(
            title         = "At a Glance",
            actionLabel   = "View All",
            onActionClick = onViewAllClick
        )

        Spacer(Modifier.height(12.dp))

        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                icon           = Icons.Default.Kitchen,
                iconTint       = HabisinTeal,
                iconBackground = Color.White,
                value          = state.totalItems.toString().padStart(2, '0').take(2)
                    .let { if (state.totalItems < 10) state.totalItems.toString() else it },
                label          = "TOTAL ITEMS",
                modifier       = Modifier.weight(1f)
            )
            StatCard(
                icon           = Icons.Default.AccessTime,
                iconTint       = HabisinCoral,
                iconBackground = Color.White,
                value          = state.expiringTotal.toString().padStart(2, '0'),    // ← ganti
                label          = "EXPIRING",
                modifier       = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(28.dp))

        // ── Recipe of the Day (placeholder) ──
        Text(
            text       = "Recipe of the Day",
            color      = HabisinTextDark,
            fontSize   = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(12.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFF2F2F2)),
            contentAlignment = Alignment.Center
        ) {
            Text("Coming soon", color = HabisinTextMuted, fontSize = 13.sp)
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun ProfileAvatar(imageUri: String?, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(HabisinOlive)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (imageUri != null) {
            AsyncImage(
                model              = imageUri,
                contentDescription = "Profile picture",
                contentScale       = ContentScale.Crop,
                modifier           = Modifier.fillMaxSize()
            )
        } else {
            Icon(
                imageVector        = Icons.Default.Person,
                contentDescription = "Profile picture placeholder",
                tint               = Color.White,
                modifier           = Modifier.size(28.dp)
            )
        }
    }
}