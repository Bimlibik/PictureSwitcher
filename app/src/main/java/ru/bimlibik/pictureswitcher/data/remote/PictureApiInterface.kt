package ru.bimlibik.pictureswitcher.data.remote

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query
import ru.bimlibik.pictureswitcher.BuildConfig
import ru.bimlibik.pictureswitcher.data.Picture


interface PictureApiInterface {

    @GET("search/photos")
    fun searchPictures(
        @Query("client_id") clientID: String = BuildConfig.ACCESS_KEY,
        @Query("query") query: String,
        @Query("page") page: Int,
    ): Observable<PictureResponse>

    @GET("/topics/wallpapers/photos")
    fun getPictures(
        @Query("client_id") clientID: String = BuildConfig.ACCESS_KEY,
        @Query("page") page: Int,
    ): Observable<List<Picture>>


}