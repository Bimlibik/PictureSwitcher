package ru.bimlibik.pictureswitcher.ui.picture_detail

import android.widget.ImageView
import android.widget.LinearLayout
import androidx.databinding.BindingAdapter
import ru.bimlibik.pictureswitcher.utils.setPreview

object PictureDetailBindings {

    @BindingAdapter("app:picture_preview", "app:error")
    @JvmStatic
    fun setPicture(imageView: ImageView, url: String, errorLayout: LinearLayout) {
        imageView.setPreview(url, errorLayout)
    }

}