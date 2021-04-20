package ru.bimlibik.pictureswitcher.data.remote

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.bimlibik.pictureswitcher.data.Picture
import ru.bimlibik.pictureswitcher.data.PicturesDataSource
import ru.bimlibik.pictureswitcher.data.Result
import ru.bimlibik.pictureswitcher.data.Result.*

class PicturesRemoteDataSource(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val pictures: CollectionReference = Firebase.firestore.collection(FirestoreSchema.FAV_PICTURES),
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : PicturesDataSource.Remote {

    private lateinit var userDocument: DocumentReference

    override suspend fun getPictures(query: String?, page: Int): Result<List<Picture>> {
        if (query == null) {
            return getAllPictures(page)
        }
        return searchPictures(query, page)
    }

    override fun login(deviceId: String, callback: (Result<Boolean>) -> Unit) {
        firebaseAuth
            .signInWithCustomToken(deviceId)
            .addOnSuccessListener {
                initCollectionReference(deviceId)
                callback(Success(true))
            }
            .addOnFailureListener { callback(Error(it)) }
    }

    override fun logout() = firebaseAuth.signOut()

    override suspend fun updateFavorite(picture: Picture, callback: (Result<Boolean>) -> Unit) {
        withContext(ioDispatcher) {
            firebaseAuth.uid?.let { uid ->
                pictures
//                    .whereEqualTo(FirestoreSchema.FavPictures.USER_ID, uid)
//                    .get()
//                    .addOnSuccessListener {
//                        val remotePicture = it.toObjects(Picture::class.java)
//                    }
//                    .addOnFailureListener {
//                        savePicture(picture, callback)
//                    }
            } ?: callback(Error(Exception("Auth error.")))
        }
    }

    private fun savePicture(picture: Picture, callback: (Result<Boolean>) -> Unit) {
        pictures
            .document()
            .set(picture)
            .addOnSuccessListener { callback(Success(true)) }
            .addOnFailureListener { callback(Error(it)) }
    }

    private fun deletePicture(picture: Picture, callback: (Result<Boolean>) -> Unit) {
//        val updates =
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

    private fun initCollectionReference(uid: String) {
        userDocument = pictures.document(uid)
        userDocument.get()
            .addOnSuccessListener { document ->
                if (!document.exists()) {
                    userDocument.set(mapOf(FirestoreSchema.FavPictures.USER_ID to uid))
                }
            }
            .addOnFailureListener {
                Log.i("TAG2", "initCollectionReference: $it")
            }
    }
}