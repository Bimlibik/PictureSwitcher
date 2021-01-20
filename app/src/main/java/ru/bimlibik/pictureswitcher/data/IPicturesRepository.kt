package ru.bimlibik.pictureswitcher.data

interface IPicturesRepository {

    suspend fun getPictures(query: String?, page: Int): Result<List<Picture>>
}