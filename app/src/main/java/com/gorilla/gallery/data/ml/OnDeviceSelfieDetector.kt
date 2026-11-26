package com.gorilla.gallery.data.ml

import android.content.Context
import android.net.Uri
import com.gorilla.gallery.data.db.FaceIndexResult

class OnDeviceSelfieDetector(
    private val context: Context,
) {
    suspend fun detect(uri: Uri): FaceIndexResult = FaceIndexResult(faceCount = 0, maxFaceRatio = 0f, centerOffset = 1f)
}
