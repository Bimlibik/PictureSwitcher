package ru.bimlibik.pictureswitcher.ui.picture_detail

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import ru.bimlibik.pictureswitcher.utils.setPreview

object PictureDetailBindings {

    @BindingAdapter("app:picture_preview")
    @JvmStatic
    fun setPicture(imageView: ImageView, url: String) {
        imageView.setPreview(url)
    }
}