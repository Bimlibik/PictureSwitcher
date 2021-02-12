package ru.bimlibik.pictureswitcher.data

import kotlinx.coroutines.flow.Flow

interface PicturesDataSource {

    interface Remote {
        suspend fun getPictures(query: String?, page: Int): Result<List<Picture>>
    }

    interface Local {

        fun updateFavorite(picture: Picture): Boolean

        fun getFavoritePictures(): Flow<Result<List<Picture>>>

        fun isFavorite(picture: Picture): Flow<Boolean>

        fun open()

        fun close()
    }

}