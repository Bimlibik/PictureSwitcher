package ru.bimlibik.pictureswitcher.utils

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.bumptech.glide.Glide
import java.io.IOException

private const val TAG = "GlideExt"

fun Context.getBitmap(url: String?): Bitmap? {
    if (url == null) return null
    val displayMetrics = this.resources.displayMetrics
    return try {
        Glide.with(this)
            .asBitmap()
            .load(url)
            .submit(displayMetrics.widthPixels, displayMetrics.heightPixels)
            .get()
    } catch (e: IOException) {
        Log.i(TAG, "Error while loading picture^ $e")
        null
    }
}