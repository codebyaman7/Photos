package com.gorilla.gallery.data.ml

import android.content.Context
import android.graphics.Bitmap

class ClipModelRunner(private val context: Context) {
    fun embedImage(bitmap: Bitmap): FloatArray = FloatArray(0)
    fun embedText(text: String): FloatArray = FloatArray(0)
}
