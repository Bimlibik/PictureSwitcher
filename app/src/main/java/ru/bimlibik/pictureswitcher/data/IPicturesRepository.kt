package ru.bimlibik.pictureswitcher.data

import kotlinx.coroutines.flow.Flow
import ru.bimlibik.pictureswitcher.data.remote.PictureResponse

interface IPicturesRepository {

    suspend fun getPictures(
        query: String?,
        lastItemKey: String?,
        callback: (Result<PictureResponse>) -> Unit
    )

    suspend fun updateFavorite(picture: Picture): Boolean

    fun getFavorites(): Flow<Result<List<Picture>>>

    fun isFavorite(picture: Picture): Flow<Boolean>

    fun open()

    fun close()
}