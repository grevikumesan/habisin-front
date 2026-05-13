package com.example.habisin.util

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

// Fungsi dinamis mengubah Uri dari Galeri/Kamera menjadi File beneran
fun uriToFile(context: Context, uri: Uri): File? {
    val contentResolver = context.contentResolver
    val tempFile = File.createTempFile("upload", ".jpg", context.cacheDir)

    return try {
        val inputStream = contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(tempFile)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        tempFile
    } catch (e: Exception) {
        null
    }
}