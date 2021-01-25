package ru.bimlibik.pictureswitcher.data.local

import android.util.Log
import io.realm.Realm
import io.realm.kotlin.where
import ru.bimlibik.pictureswitcher.data.Picture
import ru.bimlibik.pictureswitcher.data.PicturesDataSource
import ru.bimlibik.pictureswitcher.data.Result
import ru.bimlibik.pictureswitcher.data.Result.*
import java.lang.Exception

class PicturesLocalDataSource : PicturesDataSource.Local {

    override fun getFavoritePictures(): Result<List<Picture>> {
        val realm = Realm.getDefaultInstance()
        val pictures: List<Picture> = realm.copyFromRealm(realm.where<Picture>().findAll())
        realm.close()
        return Success(pictures)
    }

    override fun updateFavorite(picture: Picture): Boolean {
        val realm = Realm.getDefaultInstance()
        var isFavorite = false

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
        realm.close()
        return isFavorite
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

    companion object {
        const val TAG = "LocalDataSource"
    }

}