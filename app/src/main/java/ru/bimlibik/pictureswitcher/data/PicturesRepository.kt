package ru.bimlibik.pictureswitcher.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PicturesRepository(
    private val picturesRemoteDataSource: PicturesDataSource.Remote,
    private val picturesLocalDataSource: PicturesDataSource.Local,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IPicturesRepository {

    override suspend fun getPictures(
        query: String?, page: Int, fromFavorites: Boolean
    ): Result<List<Picture>> {
        if (fromFavorites) {
            return picturesLocalDataSource.getFavoritePictures()
        }

        return picturesRemoteDataSource.getPictures(query, page)
    }

    override suspend fun updateFavorite(picture: Picture): Boolean = withContext(ioDispatcher) {
        picturesLocalDataSource.updateFavorite(picture)
    }

}