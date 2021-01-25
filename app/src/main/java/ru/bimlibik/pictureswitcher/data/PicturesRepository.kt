package ru.bimlibik.pictureswitcher.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PicturesRepository(
    private val picturesRemoteDataSource: PicturesDataSource.Remote,
    private val picturesLocalDataSource: PicturesDataSource.Local,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IPicturesRepository {

    override suspend fun getPictures(query: String?, page: Int): Result<List<Picture>> =
        withContext(ioDispatcher) {
            return@withContext picturesRemoteDataSource.getPictures(query, page)
        }

    override fun getFavorites(): LiveData<Result<List<Picture>>> =
        picturesLocalDataSource.getFavoritePictures().map { Result.Success(it) }

    override suspend fun updateFavorite(picture: Picture): Boolean =
        withContext(ioDispatcher) {
            picturesLocalDataSource.updateFavorite(picture)
        }

    override fun close() {
        picturesLocalDataSource.close()
    }

}