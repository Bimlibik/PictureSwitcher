package ru.bimlibik.pictureswitcher.data

class PicturesRepository(
    private val picturesRemoteDataSource: PicturesDataSource
) : IPicturesRepository {

    override suspend fun getPictures(query: String?, page: Int): Result<List<Picture>> {
        return picturesRemoteDataSource.getPictures(query, page)
    }
}