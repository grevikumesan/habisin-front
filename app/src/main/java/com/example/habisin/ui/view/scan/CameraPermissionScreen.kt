package com.example.habisin.ui.view.scan

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun CameraPermissionScreen(
    onPermissionDenied: () -> Unit,
    content: @Composable () -> Unit
) {

    val context = LocalContext.current

    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->

            hasPermission = granted

            if (!granted) {
                onPermissionDenied()
            }
        }

    // Trigger permission request once
    LaunchedEffect(Unit) {
        if (!hasPermission) {
            launcher.launch(android.Manifest.permission.CAMERA)
        }
    }

    // 👇 THIS is the important part (forces correct recomposition flow)
    when {
        hasPermission -> {
            content()
        }

        else -> {
            CircularProgressIndicator()
        }
    }
}
