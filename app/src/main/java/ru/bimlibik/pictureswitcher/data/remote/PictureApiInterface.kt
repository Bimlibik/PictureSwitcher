package ru.bimlibik.pictureswitcher.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import ru.bimlibik.pictureswitcher.BuildConfig
import ru.bimlibik.pictureswitcher.data.Picture


interface PictureApiInterface {

    @GET("search/photos")
    suspend fun searchPictures(
        @Query("client_id") clientID: String = BuildConfig.ACCESS_KEY,
        @Query("query") query: String,
        @Query("page") page: Int,
    ): PictureResponse

    @GET("/topics/wallpapers/photos")
    suspend fun getPictures(
        @Query("client_id") clientID: String = BuildConfig.ACCESS_KEY,
        @Query("page") page: Int,
    ): List<Picture>


}