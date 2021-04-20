package ru.bimlibik.pictureswitcher.data

import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.bimlibik.pictureswitcher.data.Result.Success
import ru.bimlibik.pictureswitcher.utils.DEFAULT_PAGE
import timber.log.Timber

class PicturesRepository(
    private val picturesRemoteDataSource: PicturesDataSource.Remote,
    private val picturesLocalDataSource: PicturesDataSource.Local,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IPicturesRepository {

    private val cache = mutableListOf<Picture>()

    override fun getPictures(query: String?, page: Int): Observable<List<Picture>> =
        picturesRemoteDataSource.getPictures(query, page)
            .map { result ->
                refreshCache(result, page == DEFAULT_PAGE)
                getCache()
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
        picturesRemoteDataSource.login("qwerty") { result ->
            if (result is Success) {
                Timber.i("Login successfully")
            } else {
                Timber.i("Login error - $result")
            }
        }
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