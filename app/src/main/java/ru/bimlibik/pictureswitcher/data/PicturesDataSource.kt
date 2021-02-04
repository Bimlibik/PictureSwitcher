package ru.bimlibik.pictureswitcher.data

import androidx.lifecycle.LiveData

interface PicturesDataSource {

    interface Remote {
        suspend fun getPictures(query: String?, page: Int): Result<List<Picture>>
    }

    interface Local {

        fun updateFavorite(picture: Picture): Boolean

        fun getFavoritePictures(): LiveData<List<Picture>>

        fun isFavorite(picture: Picture): LiveData<Boolean>

        fun open()

        fun close()
    }

}