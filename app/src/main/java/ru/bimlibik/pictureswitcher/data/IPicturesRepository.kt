package ru.bimlibik.pictureswitcher.data

import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.flow.Flow
import ru.bimlibik.pictureswitcher.data.remote.PictureResponse

interface IPicturesRepository {

    suspend fun getPictures(
        category: String?,
        query: String?,
        lastVisiblePicture: DocumentSnapshot?,
        callback: (Result<PictureResponse>) -> Unit
    )

    suspend fun updateFavorite(picture: Picture): Boolean

    fun getFavorites(): Flow<Result<List<Picture>>>

    fun isFavorite(picture: Picture): Flow<Boolean>

    fun open()

    fun close()
}