package ru.bimlibik.pictureswitcher.workers

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.coroutineScope
import ru.bimlibik.pictureswitcher.utils.getBitmap
import java.io.IOException

class WallpaperWorker(
    context: Context, workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = coroutineScope {
        try {
            val wallpaperManager = WallpaperManager.getInstance(applicationContext)
            val url = inputData.getString(IMAGE_URL)
            val bitmap: Bitmap? = applicationContext.getBitmap(url)

            if (bitmap == null) {
                Result.failure()
            } else {
                wallpaperManager.setBitmap(bitmap)
                Result.success()
            }
        } catch (e: IOException) {
            Result.failure()
        }
    }

    companion object {
        const val IMAGE_URL = "large image url"
    }
}