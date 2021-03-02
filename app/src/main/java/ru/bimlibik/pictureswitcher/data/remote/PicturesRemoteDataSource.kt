package ru.bimlibik.pictureswitcher.data.remote

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.bimlibik.pictureswitcher.data.Picture
import ru.bimlibik.pictureswitcher.data.PicturesDataSource
import ru.bimlibik.pictureswitcher.data.Result
import ru.bimlibik.pictureswitcher.data.Result.Error
import ru.bimlibik.pictureswitcher.data.Result.Success
import ru.bimlibik.pictureswitcher.utils.ITEMS_PER_PAGE
import timber.log.Timber

class PicturesRemoteDataSource(
    private val pictures: CollectionReference = Firebase.firestore.collection(FirestoreSchema.PICTURES),
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : PicturesDataSource.Remote {


    override suspend fun getPictures(
        query: String?,
        lastVisiblePicture: DocumentSnapshot?,
        callback: (Result<PictureResponse>) -> Unit
    ) {
        if (query == null) {
            getAllPictures(lastVisiblePicture, callback)
        } else {
            searchPictures(query, lastVisiblePicture, callback)
        }
    }

    private suspend fun getAllPictures(
        lastVisiblePicture: DocumentSnapshot?,
        callback: (Result<PictureResponse>) -> Unit
    ) {
        withContext(ioDispatcher) {
            val result = mutableListOf<Picture>()
            getQuery(lastVisiblePicture)
                .get()
                .addOnSuccessListener { remoteResult ->
                    remoteResult.mapNotNullTo(result) { it.toObject(Picture::class.java) }
                    val newLastPicture = getLastPicture(remoteResult)
                    Timber.i("Pictures successfully loaded, result size = ${result.size}, lastVisiblePicture = ${newLastPicture?.id}")
                    callback(Success(PictureResponse(result, newLastPicture)))
                }
                .addOnFailureListener {
                    Timber.i("Error while loading pictures: $it")
                    callback(Error(it))
                }
        }
    }

    private suspend fun searchPictures(
        query: String,
        lastVisiblePicture: DocumentSnapshot?,
        callback: (Result<PictureResponse>) -> Unit
    ) {
        withContext(ioDispatcher) {
            val result = mutableListOf<Picture>()
            getQuery(query, lastVisiblePicture)
                .get()
                .addOnSuccessListener { remoteResult ->
                    Timber.i("Pictures were found successfully. Query - $query")
                    remoteResult.mapNotNullTo(result) { it.toObject(Picture::class.java) }
                    val newLastPicture = getLastPicture(remoteResult)
                    callback(Success(PictureResponse(result, newLastPicture)))
                }
                .addOnFailureListener {
                    Timber.i("Error when trying to find pictures by query = $query: $it")
                    callback(Error(it))
                }
        }
    }

    private fun getLastPicture(remoteResult: QuerySnapshot): DocumentSnapshot? {
        return if (remoteResult.documents.isEmpty()) {
            null
        } else {
            remoteResult.documents[remoteResult.size() - 1]
        }
    }

    private fun getQuery(query: String, lastVisiblePicture: DocumentSnapshot?): Query {
        return if (lastVisiblePicture == null) {
            pictures
//                .orderBy("created", Query.Direction.DESCENDING)
                .whereArrayContains(FirestoreSchema.Pictures.TAGS, query)
                .limit(ITEMS_PER_PAGE)
        } else {
            pictures
//                .orderBy("created", Query.Direction.DESCENDING)
                .whereArrayContains(FirestoreSchema.Pictures.TAGS, query)
                .startAfter(lastVisiblePicture)
                .limit(ITEMS_PER_PAGE)
        }
    }

    private fun getQuery(lastVisiblePicture: DocumentSnapshot?): Query {
        return if (lastVisiblePicture == null) {
            pictures
                .orderBy(FirestoreSchema.Pictures.CREATED_AT, Query.Direction.DESCENDING)
                .limit(ITEMS_PER_PAGE)
        } else {
            pictures
                .orderBy(FirestoreSchema.Pictures.CREATED_AT, Query.Direction.DESCENDING)
                .startAfter(lastVisiblePicture)
                .limit(ITEMS_PER_PAGE)
        }
    }

}