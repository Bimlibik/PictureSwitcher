package ru.bimlibik.pictureswitcher

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

object PicturesBinding {

    @BindingAdapter("app:pictures")
    @JvmStatic
    fun setPictures(recycler: RecyclerView, pictures: List<Picture>?) {
        pictures?.let {
            (recycler.adapter as PicturesAdapter).submitList(pictures)
        }
    }

    @BindingAdapter("app:preview")
    @JvmStatic
    fun setPicture(imageView: ImageView, previewURL: String) {
        val displayMetrics = imageView.context.resources.displayMetrics
        val width = displayMetrics.widthPixels / 2
        val height = imageView.height
        Glide.with(imageView.context)
            .load(previewURL)
            .placeholder(R.drawable.ic_image)
            .override(width, height)
            .centerCrop()
            .into(imageView)
    }
}