package ru.bimlibik.pictureswitcher.data

import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.flow.Flow
import ru.bimlibik.pictureswitcher.data.remote.PictureResponse

interface PicturesDataSource {

    interface Remote {

        suspend fun getPictures(query: String?, lastVisiblePicture: DocumentSnapshot?, callback: (Result<PictureResponse>) -> Unit)

    }

    interface Local {

        fun updateFavorite(picture: Picture): Boolean

        fun getFavoritePictures(): Flow<Result<List<Picture>>>

        fun isFavorite(picture: Picture): Flow<Boolean>

        fun open()

        fun close()
    }

}