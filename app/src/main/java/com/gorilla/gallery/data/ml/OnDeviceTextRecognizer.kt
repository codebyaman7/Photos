package com.gorilla.gallery.data.ml

import android.content.Context
import android.net.Uri

data class RecognizedTextBlock(
    val text: String,
    val boundingBox: android.graphics.Rect?,
)

class OnDeviceTextRecognizer(
    private val context: Context,
) {
    suspend fun recognizeText(uri: Uri): String = ""
}
