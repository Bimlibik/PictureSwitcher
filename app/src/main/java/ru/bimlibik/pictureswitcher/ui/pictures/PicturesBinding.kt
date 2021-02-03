package ru.bimlibik.pictureswitcher.ui.pictures

import android.graphics.drawable.GradientDrawable
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.bimlibik.pictureswitcher.data.Picture
import ru.bimlibik.pictureswitcher.utils.setSmallPicture

object PicturesBinding {

    @BindingAdapter("app:pictures")
    @JvmStatic
    fun setPictures(recycler: RecyclerView, pictures: List<Picture>?) {
        pictures?.let {
            (recycler.adapter as PicturesAdapter).submitList(pictures)
        }
    }

    @BindingAdapter("app:pictures_preview")
    @JvmStatic
    fun setPicture(imageView: ImageView, picture: Picture) {
        imageView.setSmallPicture(picture)
    }

    @BindingAdapter("app:custom_color")
    @JvmStatic
    fun setCustomToolbarColor(toolbar: Toolbar, color: Int) {
        val gradient = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(color, android.R.color.transparent)
        )
        toolbar.background = gradient
    }
}