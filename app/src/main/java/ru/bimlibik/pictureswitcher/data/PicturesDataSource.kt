package ru.bimlibik.pictureswitcher.data

import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow

interface PicturesDataSource {

    interface Remote {

        fun getPictures(query: String?, page: Int): Observable<List<Picture>>

        fun login(deviceId: String, callback: (Result<Boolean>) -> Unit)

        fun logout()

        suspend fun updateFavorite(pictures: Picture, callback: (Result<Boolean>) -> Unit)
    }

    interface Local {

        fun updateFavorite(picture: Picture): Boolean

        fun getFavoritePictures(): Flow<Result<List<Picture>>>

        fun isFavorite(picture: Picture): Flow<Boolean>

        fun open()

        fun close()
    }

}