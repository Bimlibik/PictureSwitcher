package ru.bimlibik.pictureswitcher.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.bimlibik.pictureswitcher.data.Result.*
import ru.bimlibik.pictureswitcher.utils.DEFAULT_PAGE

class PicturesRepository(
    private val picturesRemoteDataSource: PicturesDataSource.Remote,
    private val picturesLocalDataSource: PicturesDataSource.Local,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IPicturesRepository {

    private val cache = mutableListOf<Picture>()

    override suspend fun getPictures(query: String?, page: Int): Result<List<Picture>> =
        withContext(ioDispatcher) {
            val result = picturesRemoteDataSource.getPictures(query, page)
            if (result is Success) {
                refreshCache(result.data, page == DEFAULT_PAGE)
                return@withContext Success(getCache())
            } else {
                return@withContext result
            }
        }

    override fun getFavorites(): LiveData<Result<List<Picture>>> =
        picturesLocalDataSource.getFavoritePictures().map { Success(it) }

    override suspend fun updateFavorite(picture: Picture): Boolean =
        withContext(ioDispatcher) {
            picturesLocalDataSource.updateFavorite(picture)
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