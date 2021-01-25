package ru.bimlibik.pictureswitcher.data

interface IPicturesRepository {

     suspend fun getPictures(query: String?, page: Int, fromFavorites: Boolean): Result<List<Picture>>

     suspend fun updateFavorite(picture: Picture): Boolean
}