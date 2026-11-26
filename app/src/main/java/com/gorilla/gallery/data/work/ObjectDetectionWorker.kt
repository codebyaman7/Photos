package com.gorilla.gallery.data.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class ObjectDetectionWorker(
    appContext: Context,
    params: WorkerParameters,
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result = Result.success()

    companion object {
        fun enqueue(context: Context) {
            // Disabled
        }
    }
}
