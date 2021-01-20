package ru.bimlibik.pictureswitcher.data

interface PicturesDataSource {

    suspend fun getPictures(query: String?, page: Int): Result<List<Picture>>

}