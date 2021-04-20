package ru.bimlibik.pictureswitcher.data

import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow

interface IPicturesRepository {

     fun getPictures(query: String?, page: Int): Observable<List<Picture>>

     suspend fun updateFavorite(picture: Picture): Boolean

     fun getFavorites(): Flow<Result<List<Picture>>>

     fun isFavorite(picture: Picture): Flow<Boolean>

     fun open()

     fun close()
}