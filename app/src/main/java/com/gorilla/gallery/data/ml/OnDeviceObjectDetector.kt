package com.gorilla.gallery.data.ml

import android.content.Context
import android.net.Uri

data class DetectedObjectResult(
    val label: String,
    val confidence: Float,
    val boundingBox: android.graphics.Rect,
)

class OnDeviceObjectDetector(
    private val context: Context,
) {
    suspend fun detect(uri: Uri): List<DetectedObjectResult> = emptyList()
}
