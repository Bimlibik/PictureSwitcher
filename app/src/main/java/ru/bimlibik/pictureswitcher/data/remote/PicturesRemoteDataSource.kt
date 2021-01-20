package ru.bimlibik.pictureswitcher.data.remote

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.bimlibik.pictureswitcher.data.Picture
import ru.bimlibik.pictureswitcher.data.PicturesDataSource
import ru.bimlibik.pictureswitcher.data.Result
import ru.bimlibik.pictureswitcher.data.Result.*
import java.lang.Exception

class PicturesRemoteDataSource(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : PicturesDataSource {

    override suspend fun getPictures(query: String?, page: Int): Result<List<Picture>> {
        if (query == null) {
            return getAllPictures(page)
        }
        return searchPictures(query, page)
    }

     private suspend fun getAllPictures(page: Int): Result<List<Picture>> =
        withContext(ioDispatcher) {
            return@withContext try {
                Success(ApiClient.client.getPictures(page = page))
            } catch (e: Exception) {
                Error(e)
            }
        }

     private suspend fun searchPictures(query: String, page: Int): Result<List<Picture>> =
        withContext(ioDispatcher) {
            return@withContext try {
                Success(ApiClient.client.searchPictures(query = query, page = page).pictures)
            } catch (e: Exception) {
                Error(e)
            }
        }


}