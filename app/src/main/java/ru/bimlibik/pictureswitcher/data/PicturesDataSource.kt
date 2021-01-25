package ru.bimlibik.pictureswitcher.data

interface PicturesDataSource {

    interface Remote {
        suspend fun getPictures(query: String?, page: Int): Result<List<Picture>>
    }

    interface Local {


        fun updateFavorite(picture: Picture): Boolean

        fun getFavoritePictures(): Result<List<Picture>>
    }


}