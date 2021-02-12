package ru.bimlibik.pictureswitcher.data.local

import android.util.Log
import io.realm.Realm
import io.realm.kotlin.toFlow
import io.realm.kotlin.where
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import ru.bimlibik.pictureswitcher.data.Picture
import ru.bimlibik.pictureswitcher.data.PicturesDataSource
import ru.bimlibik.pictureswitcher.data.Result
import ru.bimlibik.pictureswitcher.data.Result.Success

class PicturesLocalDataSource : PicturesDataSource.Local {

    private lateinit var defaultRealm: Realm

    /**
     * Changed livedata to flow because livedata was causing an error:
    "java.lang.IllegalStateException: Cancel call cannot happen without a maybeRun"
    More about issue here: https://stackoverflow.com/questions/61463006/java-lang-illegalstateexception-cancel-call-cannot-happen-without-a-mayberun

    need to test
     */
    override fun getFavoritePictures(): Flow<Result<List<Picture>>> =
        flow {
            defaultRealm.where<Picture>()
                .findAllAsync()
                .toFlow()
                .collect { emit(Success(it.toList())) }
        }

    override fun updateFavorite(picture: Picture): Boolean {
        var isFavorite = false

        openRealmInstance { realm ->
            realm.executeTransaction {
                val innerPicture = realm.where<Picture>().equalTo("id", picture.id).findFirst()
                isFavorite = if (innerPicture == null) {
                    savePicture(realm, picture)
                    true
                } else {
                    deletePicture(realm, picture)
                    false
                }
            }
        }

        return isFavorite
    }

    override fun isFavorite(picture: Picture): Flow<Boolean> =
        flow {
            defaultRealm.where<Picture>()
                .equalTo("id", picture.id)
                .findAllAsync()
                .toFlow()
                .collect { emit(!it.isEmpty()) }
        }

    override fun open() {
        defaultRealm = Realm.getDefaultInstance()
    }

    override fun close() {
        defaultRealm.close()
    }

    private fun savePicture(realm: Realm, picture: Picture) {
        try {
            realm.insertOrUpdate(picture)
        } catch (e: Exception) {
            Log.i(TAG, "Error while try to save picture: $e")
        }
    }

    private fun deletePicture(realm: Realm, picture: Picture) {
        val innerPicture = realm.where<Picture>().equalTo("id", picture.id).findFirst()
        innerPicture?.deleteFromRealm()
    }

    @Synchronized
    private fun <T> openRealmInstance(fn: (Realm) -> T): T {
        val realm = Realm.getDefaultInstance()
        realm.use {
            return fn(realm)
        }
    }

    companion object {
        const val TAG = "LocalDataSource"
    }

}