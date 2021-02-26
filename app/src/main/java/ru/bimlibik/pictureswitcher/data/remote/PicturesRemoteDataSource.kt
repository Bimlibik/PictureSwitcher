package ru.bimlibik.pictureswitcher.data.remote

import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.bimlibik.pictureswitcher.data.Picture
import ru.bimlibik.pictureswitcher.data.PicturesDataSource
import ru.bimlibik.pictureswitcher.data.Result
import ru.bimlibik.pictureswitcher.data.Result.*
import ru.bimlibik.pictureswitcher.utils.ITEMS_PER_PAGE
import timber.log.Timber

class PicturesRemoteDataSource(
    private val picturesRef: DatabaseReference = Firebase.database.reference.child("pictures"),
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : PicturesDataSource.Remote {

    override suspend fun getAllPictures(
        lastItemKey: String?,
        callback: (Result<PictureResponse>) -> Unit
    ) {
        withContext(ioDispatcher) {
            val result = mutableListOf<Picture>()
            getQuery(lastItemKey).addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.mapNotNullTo(result) { it.getValue<Picture>() }
                    result.reverse()
                    val key = snapshot.children.first().key
                    Timber.i("Pictures uploaded successfully. lastItemKey = $lastItemKey, key = $key")
                    callback(Success(PictureResponse(key, result)))
                }

                override fun onCancelled(error: DatabaseError) {
                    Timber.e("Error while loading pictures: $error")
                    callback(Error(error.toException()))
                }

            })
        }
    }

    private fun getQuery(key: String?): Query {
        return if (key == null) {
            picturesRef
                .orderByKey()
                .limitToLast(ITEMS_PER_PAGE)
        } else {
            picturesRef
                .orderByKey()
                .endAt(key)
                .limitToLast(ITEMS_PER_PAGE)
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

//    private suspend fun getAllPictures(page: Int): Result<List<Picture>> =
//        withContext(ioDispatcher) {
//            return@withContext try {
//                Success(ApiClient.client.getPictures(page = page))
//            } catch (e: Exception) {
//                Error(e)
//            }
//        }

//     private suspend fun searchPictures(query: String, page: Int): Result<List<Picture>> =
//        withContext(ioDispatcher) {
//            return@withContext try {
//                Success(ApiClient.client.searchPictures(query = query, page = page).pictures)
//            } catch (e: Exception) {
//                Error(e)
//            }
//        }


}