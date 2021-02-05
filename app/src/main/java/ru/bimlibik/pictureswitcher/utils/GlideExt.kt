package ru.bimlibik.pictureswitcher.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import ru.bimlibik.pictureswitcher.GlideApp
import ru.bimlibik.pictureswitcher.R
import ru.bimlibik.pictureswitcher.data.Picture
import java.io.IOException

private const val TAG = "GlideExt"

fun Context.getBitmap(url: String?): Bitmap? {
    if (url == null) return null
    val displayMetrics = this.resources.displayMetrics
    return try {
        GlideApp.with(this)
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

fun ImageView.setSmallPicture(picture: Picture) {
    val displayMetrics = this.context.resources.displayMetrics
    val width = displayMetrics.widthPixels / 2
    val height = this.height
    GlideApp.with(this.context)
        .asBitmap()
        .load(picture.urls?.small)
        .centerCrop()
        .override(width, height)
        .thumbnail(0.2f)
        .listener(getRequestListener(picture))
        .into(this)
}

fun ImageView.setPreview(url: String, errorLayout: LinearLayout) {
    GlideApp.with(this)
        .load(url)
        .centerCrop()
        .placeholder(getPlaceholder(this.context))
        .listener(getRequestListener(this, errorLayout))
        .thumbnail(0.2f)
        .into(this)
}

private fun getPlaceholder(context: Context): CircularProgressDrawable =
    CircularProgressDrawable(context).apply {
        strokeWidth = 5f
        centerRadius = 30f
        setColorSchemeColors(
            ContextCompat.getColor(context, R.color.colorAccent),
            ContextCompat.getColor(context, R.color.colorSecondaryAccent)
        )
        start()
    }


/**
 * The same function is used when loading images from Realm.
 * Since Realm uses LiveData, when trying to change the [color] field, an error occurs:
 * "Cannot modify managed objects outside of a write transaction".
 * Therefore, a try/catch block is used here.
 * If an error was caught, it means that the given [picture] is already in the database and
 * the [color] value has already been calculated for it.
 */
private fun getRequestListener(picture: Picture) = object : RequestListener<Bitmap> {

    override fun onLoadFailed(
        e: GlideException?,
        model: Any?,
        target: Target<Bitmap>?,
        isFirstResource: Boolean
    ): Boolean {
        Log.e(TAG, "Error while loading picture^ $e")
        return false
    }

    override fun onResourceReady(
        resource: Bitmap?,
        model: Any?,
        target: Target<Bitmap>?,
        dataSource: DataSource?,
        isFirstResource: Boolean
    ): Boolean {
        try {
            if (resource != null) {
                val palette: Palette = Palette.from(resource).generate()
                picture.color = palette.darkMutedSwatch?.rgb ?: android.R.color.transparent
            }
        } catch (e: IllegalStateException) {
            Log.i(TAG, "Picture color already exists.")
        }
        return false
    }
}

private fun getRequestListener(imageView: ImageView, errorLayout: LinearLayout) = object : RequestListener<Drawable> {

    override fun onLoadFailed(
        e: GlideException?,
        model: Any?,
        target: Target<Drawable>?,
        isFirstResource: Boolean
    ): Boolean {
        Log.e(TAG, "Error while loading picture^ $e")
        imageView.visibility = View.GONE
        errorLayout.visibility = View.VISIBLE
        return false
    }

    override fun onResourceReady(
        resource: Drawable?,
        model: Any?,
        target: Target<Drawable>?,
        dataSource: DataSource?,
        isFirstResource: Boolean
    ): Boolean {
        imageView.visibility = View.VISIBLE
        errorLayout.visibility = View.GONE
        return false
    }
}