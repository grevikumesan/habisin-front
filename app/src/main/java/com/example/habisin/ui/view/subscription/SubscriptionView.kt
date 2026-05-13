package com.example.habisin.ui.view.subscription

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.habisin.ui.viewmodel.SubscriptionViewModel

private val HabisinDarkGreen = Color(0xFF1B4332)
private val HabisinMidGreen = Color(0xFF2D6A4F)
private val HabisinLightGreen = Color(0xFFD8F3DC)
private val HabisinAccent = Color(0xFFB7E4C7)

@Composable
fun SubscriptionView (
    onBack: () -> Unit,
    viewModel: SubscriptionViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    // Load status saat screen pertama dibuka
    LaunchedEffect(Unit) { viewModel.loadStatus() }

    // Kalau showWebView aktif, render WebView fullscreen
    if (state.showWebView && state.redirectUrl != null) {
        MidtransWebViewScreen(
            url = state.redirectUrl!!,
            onClose = { viewModel.onWebViewClosed() }
        )
        return
    }

    // ⬇️ BackHandler di sini — cuma aktif saat NGGAK di WebView
    BackHandler { onBack() }

    // Otherwise: tampilkan landing page subscription
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Back button
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp)
                .statusBarsPadding()
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = HabisinDarkGreen
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .padding(top = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ── Hero icon ──
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(HabisinLightGreen, RoundedCornerShape(50)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    tint = HabisinMidGreen,
                    modifier = Modifier.size(56.dp)
                )
            }

            Spacer(Modifier.height(24.dp))

            // ── Title ──
            if (state.isActive) {
                Text(
                    "Subscription Aktif",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = HabisinDarkGreen
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "Sisa ${state.daysRemaining} hari",
                    fontSize = 16.sp,
                    color = HabisinMidGreen
                )
                if (state.expiresAt != null) {
                    Text(
                        "Berakhir pada ${state.expiresAt!!.substring(0, 10)}",
                        fontSize = 13.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            } else {
                Text(
                    "Habisin Premium",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = HabisinDarkGreen
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "Akses penuh fitur Recipe AI",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Spacer(Modifier.height(32.dp))

            // ── Benefits list ──
            BenefitItem("Generate resep berdasarkan stok bahan kamu")
            BenefitItem("Akses semua resep premium")
            BenefitItem("Update resep otomatis dari AI")
            BenefitItem("Tanpa iklan, fokus masak")

            Spacer(Modifier.weight(1f))

            // ── Pricing ──
            if (!state.isActive) {
                Surface(
                    color = HabisinLightGreen,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Rp 15.000",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = HabisinDarkGreen
                        )
                        Text(
                            "per 30 hari",
                            fontSize = 14.sp,
                            color = HabisinMidGreen
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))
            }

            // ── CTA Button ──
            Button(
                onClick = {
                    if (state.isActive) {
                        // Already active — bisa perpanjang
                        viewModel.subscribe()
                    } else {
                        viewModel.subscribe()
                    }
                },
                enabled = !state.isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = HabisinDarkGreen),
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text(
                        if (state.isActive) "Perpanjang Subscription"
                        else "Berlangganan Sekarang",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // ── Error message ──
            if (state.errorMessage != null) {
                Spacer(Modifier.height(12.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        state.errorMessage!!,
                        color = Color.Red,
                        fontSize = 13.sp,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { viewModel.clearError() }) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Dismiss",
                            tint = Color.Red,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun BenefitItem(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.CheckCircle,
            contentDescription = null,
            tint = HabisinMidGreen,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(12.dp))
        Text(text, fontSize = 14.sp, color = HabisinDarkGreen)
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun MidtransWebViewScreen(
    url: String,
    onClose: () -> Unit
) {

    BackHandler { onClose() }

    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(url) {
        android.util.Log.d("WV", "Loading WebView with URL: $url")
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            factory = { context ->
                WebView(context).apply {
                    // ── Core settings ──
                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        databaseEnabled = true
                        loadWithOverviewMode = true
                        useWideViewPort = true
                        setSupportZoom(true)
                        builtInZoomControls = true
                        displayZoomControls = false
                        cacheMode = android.webkit.WebSettings.LOAD_DEFAULT
                        // Penting buat Midtrans yang sering mixed content
                        mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                        // User agent biar Midtrans tau ini browser beneran (bukan WebView default)
                        userAgentString = userAgentString
                            .replace("; wv", "")  // hapus ";wv" yang bikin Midtrans ngira ini bot
                    }

                    // ── Client untuk handle URL routing ──
                    webViewClient = object : WebViewClient() {
                        override fun shouldOverrideUrlLoading(
                            view: WebView?,
                            request: android.webkit.WebResourceRequest?
                        ): Boolean {
                            val targetUrl = request?.url?.toString() ?: return false
                            android.util.Log.d("WV", "Navigating to: $targetUrl")

                            // ⬇️ DETECT FINISH URL — Midtrans redirect ke example.com setelah selesai
                            if (targetUrl.contains("example.com") ||
                                targetUrl.contains("transaction_status=")) {
                                android.util.Log.d("WV", "Detected finish redirect, closing WebView")
                                onClose()  // ← langsung trigger close
                                return true  // ← block load supaya nggak error CLEARTEXT
                            }

                            // Handle deeplink (gojek://, shopeepay://, dll)
                            if (!targetUrl.startsWith("http")) {
                                try {
                                    val intent = android.content.Intent(
                                        android.content.Intent.ACTION_VIEW,
                                        android.net.Uri.parse(targetUrl)
                                    )
                                    intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
                                    view?.context?.startActivity(intent)
                                    return true
                                } catch (e: Exception) {
                                    android.util.Log.e("WV", "Failed to open deeplink: ${e.message}")
                                    return true
                                }
                            }
                            return false
                        }

                        override fun onPageStarted(
                            view: WebView?,
                            url: String?,
                            favicon: android.graphics.Bitmap?
                        ) {
                            android.util.Log.d("WV", "Page started: $url")
                            isLoading = true
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            android.util.Log.d("WV", "Page finished: $url")
                            isLoading = false
                        }

                        override fun onReceivedError(
                            view: WebView?,
                            request: android.webkit.WebResourceRequest?,
                            error: android.webkit.WebResourceError?
                        ) {
                            android.util.Log.e(
                                "WV",
                                "Error: ${error?.errorCode} ${error?.description} on ${request?.url}"
                            )
                        }
                    }

                    // ── Chrome client untuk console log dari halaman web ──
                    webChromeClient = object : android.webkit.WebChromeClient() {
                        override fun onConsoleMessage(
                            consoleMessage: android.webkit.ConsoleMessage?
                        ): Boolean {
                            android.util.Log.d(
                                "WV_CONSOLE",
                                "${consoleMessage?.messageLevel()}: ${consoleMessage?.message()}"
                            )
                            return true
                        }
                    }

                    loadUrl(url)
                }
            }
        )

        // Loading indicator
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = HabisinDarkGreen
            )
        }

        // Tombol close di atas
        IconButton(
            onClick = onClose,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp)
                .statusBarsPadding()
                .background(
                    Color.White.copy(alpha = 0.9f),
                    androidx.compose.foundation.shape.CircleShape
                )
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Close",
                tint = HabisinDarkGreen
            )
        }
    }
}