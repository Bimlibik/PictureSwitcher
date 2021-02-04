package ru.bimlibik.pictureswitcher.data

import androidx.lifecycle.LiveData

interface IPicturesRepository {

     suspend fun getPictures(query: String?, page: Int): Result<List<Picture>>

     suspend fun updateFavorite(picture: Picture): Boolean

     fun getFavorites(): LiveData<Result<List<Picture>>>

     fun isFavorite(picture: Picture): LiveData<Boolean>

     fun open()

     fun close()
}