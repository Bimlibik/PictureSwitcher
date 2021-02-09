package ru.bimlibik.pictureswitcher.ui.favorite_pictures

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.bimlibik.pictureswitcher.data.Picture

object FavoritePicturesBinding {

    @BindingAdapter("app:favorite_pictures")
    @JvmStatic
    fun setFavoritePictures(recycler: RecyclerView, pictures: List<Picture>?) {
        pictures?.let {
            (recycler.adapter as FavoritePicturesAdapter).submitList(pictures)
        }
    }
}