package ru.bimlibik.pictureswitcher.data.local

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import io.realm.Realm
import io.realm.kotlin.toFlow
import io.realm.kotlin.where
import kotlinx.coroutines.flow.collect
import ru.bimlibik.pictureswitcher.data.Picture
import ru.bimlibik.pictureswitcher.data.PicturesDataSource

class PicturesLocalDataSource : PicturesDataSource.Local {

    private lateinit var defaultRealm: Realm

    override fun getFavoritePictures(): LiveData<List<Picture>> =
        liveData {
            defaultRealm.where<Picture>()
                .findAllAsync()
                .toFlow()
                .collect { emit(it.toList()) }
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