package ru.bimlibik.pictureswitcher.ui.pictures

import android.widget.ImageView
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
    fun setPicture(imageView: ImageView, url: String) {
        imageView.setSmallPicture(url)
    }
}