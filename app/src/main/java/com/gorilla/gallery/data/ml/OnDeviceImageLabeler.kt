package com.gorilla.gallery.data.ml

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri

class OnDeviceImageLabeler(private val context: Context) {
    suspend fun label(uri: Uri): List<String> = emptyList()
}

suspend fun decodeSampledBitmap(
    context: Context,
    uri: Uri,
    maxDimension: Int,
): Bitmap? = null
