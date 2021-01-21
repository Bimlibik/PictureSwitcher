package ru.bimlibik.pictureswitcher.ui.picture_detail

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import ru.bimlibik.pictureswitcher.R

object PictureDetailBindings {

    @BindingAdapter("app:picture_preview")
    @JvmStatic
    fun setPicture(imageView: ImageView, previewURL: String?) {
        Glide.with(imageView.context)
            .load(previewURL)
            .centerCrop()
            .placeholder(R.drawable.ic_image)
            .into(imageView)
    }
}