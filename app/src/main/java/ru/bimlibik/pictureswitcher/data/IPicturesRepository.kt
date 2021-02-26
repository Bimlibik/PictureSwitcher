package ru.bimlibik.pictureswitcher.data

import kotlinx.coroutines.flow.Flow

interface IPicturesRepository {

    suspend fun getPictures(
        query: String?,
        lastItemKey: String?,
        callback: (Result<List<Picture>>) -> Unit
    )

    suspend fun updateFavorite(picture: Picture): Boolean

    fun getFavorites(): Flow<Result<List<Picture>>>

    fun isFavorite(picture: Picture): Flow<Boolean>

    fun open()

    fun close()
}