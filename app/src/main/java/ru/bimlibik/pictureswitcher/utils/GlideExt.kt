package ru.bimlibik.pictureswitcher.utils

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import ru.bimlibik.pictureswitcher.R
import java.io.IOException

private const val TAG = "GlideExt"

fun Context.getBitmap(url: String?): Bitmap? {
    if (url == null) return null
    val displayMetrics = this.resources.displayMetrics
    return try {
        Glide.with(this)
            .asBitmap()
            .load(url)
            .centerCrop()
            .submit(displayMetrics.widthPixels, displayMetrics.heightPixels)
            .get()
    } catch (e: IOException) {
        Log.i(TAG, "Error while loading picture^ $e")
        null
    }
}

fun ImageView.setSmallPicture(url: String) {
    val displayMetrics = this.context.resources.displayMetrics
    val width = displayMetrics.widthPixels / 2
    val height = this.height
    Glide.with(this.context)
        .load(url)
        .placeholder(R.drawable.ic_image)
        .centerCrop()
        .override(width, height)
        .into(this)
}

fun ImageView.setPreview(url: String) {
    Glide.with(this.context)
        .load(url)
        .centerCrop()
        .placeholder(R.drawable.ic_image)
        .into(this)
}
