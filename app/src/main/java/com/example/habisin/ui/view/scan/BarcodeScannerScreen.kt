package com.example.habisin.ui.view.scan

import android.annotation.SuppressLint
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

@SuppressLint("UnsafeOptInUsageError")
@Composable
fun BarcodeScannerScreen(
    onBarcodeScanned: (String) -> Unit,
    onBack: () -> Unit
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    Scaffold(
        topBar = {
            IconButton(
                onClick = onBack
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        }
    ) { padding ->

        AndroidView(
            modifier = Modifier.fillMaxSize().padding(padding),
            factory = {

                val previewView =
                    PreviewView(context)

                val cameraProviderFuture =
                    ProcessCameraProvider.getInstance(context)

                cameraProviderFuture.addListener({

                    val cameraProvider =
                        cameraProviderFuture.get()

                    val preview =
                        Preview.Builder().build()

                    preview.setSurfaceProvider(
                        previewView.surfaceProvider
                    )

                    val scanner =
                        BarcodeScanning.getClient()

                    val imageAnalyzer =
                        ImageAnalysis.Builder()
                            .build()
                            .also { analysis ->

                                analysis.setAnalyzer(
                                    ContextCompat.getMainExecutor(context)
                                ) { imageProxy ->

                                    processImageProxy(
                                        scanner = scanner,
                                        imageProxy = imageProxy,
                                        onBarcodeScanned = onBarcodeScanned
                                    )
                                }
                            }

                    val cameraSelector =
                        CameraSelector.DEFAULT_BACK_CAMERA

                    cameraProvider.unbindAll()

                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalyzer
                    )

                }, ContextCompat.getMainExecutor(context))

                previewView
            }
        )
    }
}

@androidx.camera.core.ExperimentalGetImage
private fun processImageProxy(
    scanner: com.google.mlkit.vision.barcode.BarcodeScanner,
    imageProxy: ImageProxy,
    onBarcodeScanned: (String) -> Unit
) {

    val mediaImage = imageProxy.image

    if (mediaImage != null) {

        val image =
            InputImage.fromMediaImage(
                mediaImage,
                imageProxy.imageInfo.rotationDegrees
            )

        scanner.process(image)
            .addOnSuccessListener { barcodes ->

                for (barcode in barcodes) {

                    barcode.rawValue?.let { value ->

                        onBarcodeScanned(value)

                        imageProxy.close()

                        return@addOnSuccessListener
                    }
                }

                imageProxy.close()
            }

            .addOnFailureListener {

                imageProxy.close()
            }

    } else {

        imageProxy.close()
    }
}