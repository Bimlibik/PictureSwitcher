package ru.bimlibik.pictureswitcher.data

import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.bimlibik.pictureswitcher.data.Result.*
import ru.bimlibik.pictureswitcher.data.remote.PictureResponse
import timber.log.Timber

class PicturesRepository(
    private val picturesRemoteDataSource: PicturesDataSource.Remote,
    private val picturesLocalDataSource: PicturesDataSource.Local,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IPicturesRepository {

    private val cache = mutableListOf<Picture>()

    override suspend fun getPictures(
        query: String?,
        lastVisiblePicture: DocumentSnapshot?,
        callback: (Result<PictureResponse>) -> Unit
    ) {
        withContext(ioDispatcher) {
            picturesRemoteDataSource.getPictures(query, lastVisiblePicture) { result ->
                if (result is Success) {
                    Timber.i("Pictures successfully uploaded from remote data source.")
                    refreshCache(result.data.pictures, lastVisiblePicture == null)
                    callback(Success(PictureResponse(getCache(), result.data.lastVisiblePicture)))
                } else {
                    Timber.e("Error while loading pictures from remote data source: $result")
                    callback(result)
                }
            }
        }
    }

    override fun getFavorites(): Flow<Result<List<Picture>>> =
        picturesLocalDataSource.getFavoritePictures()

    override fun isFavorite(picture: Picture): Flow<Boolean> =
        picturesLocalDataSource.isFavorite(picture)

    override suspend fun updateFavorite(picture: Picture): Boolean =
        withContext(ioDispatcher) {
            picturesLocalDataSource.updateFavorite(picture)
        }

    override fun open() {
        picturesLocalDataSource.open()
    }

    override fun close() {
        picturesLocalDataSource.close()
    }

    private fun getCache(): List<Picture> {
        val newItems = mutableListOf<Picture>()
        cache.forEach { picture ->
            newItems.add(picture.copy())
        }
        return newItems
    }

    private fun refreshCache(pictures: List<Picture>, isNew: Boolean) {
        if (isNew) cache.clear()

        if (cache.isEmpty()) {
            cache.addAll(pictures)
            return
        }

        for (picture in pictures) {
            if (cache.contains(picture)) continue
            cache.add(picture)
        }
    }

}