package com.example.habisin.ui.view.profile

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.habisin.R
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.habisin.data.local.SettingsManager
import com.example.habisin.ui.theme.HabisinTheme
import com.example.habisin.ui.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = viewModel()
) {
    val context = LocalContext.current
    val enabled by viewModel.notifEnabled.collectAsState()
    val threshold by viewModel.notifThreshold.collectAsState()

    // Android 13+ runtime permission for posting notifications.
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        viewModel.setNotifEnabled(granted)
    }

    fun needsNotifPermission(): Boolean =
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
        ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) !=
        android.content.pm.PackageManager.PERMISSION_GRANTED

    fun enableWithPermission() {
        if (needsNotifPermission()) permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        else viewModel.setNotifEnabled(true)
    }

    // It's enabled by default, so on first open we may still lack the runtime permission
    // (Android 13+). Request it once so notifications can actually post.
    LaunchedEffect(Unit) {
        if (enabled && needsNotifPermission()) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title          = { Text("Notification", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                stringResource(R.string.notif_desc),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp
            )

            // ── Enable toggle ──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        stringResource(R.string.notif_reminder_title),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        stringResource(R.string.notif_reminder_subtitle),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp
                    )
                }
                Switch(
                    checked = enabled,
                    onCheckedChange = { checked ->
                        if (checked) enableWithPermission() else viewModel.setNotifEnabled(false)
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = HabisinTheme.colors.onAction,
                        checkedTrackColor = HabisinTheme.colors.action
                    )
                )
            }

            // ── Threshold stepper (only when enabled) ──
            if (enabled) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        stringResource(R.string.notif_threshold_q),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            stringResource(R.string.notif_threshold_value, threshold),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 13.sp
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { viewModel.setNotifThreshold(threshold - 1) },
                                enabled = threshold > SettingsManager.MIN_THRESHOLD_DAYS
                            ) {
                                Icon(Icons.Default.Remove, contentDescription = "Kurangi",
                                    tint = MaterialTheme.colorScheme.onSurface)
                            }
                            Text(
                                "$threshold",
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            IconButton(
                                onClick = { viewModel.setNotifThreshold(threshold + 1) },
                                enabled = threshold < SettingsManager.MAX_THRESHOLD_DAYS
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Tambah",
                                    tint = MaterialTheme.colorScheme.onSurface)
                            }
                        }
                    }
                }

                // ── Test button ──
                OutlinedButton(
                    onClick = {
                        if (needsNotifPermission()) permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        else viewModel.sendTestNotification()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = HabisinTheme.colors.action),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, HabisinTheme.colors.action),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(stringResource(R.string.notif_test), fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
