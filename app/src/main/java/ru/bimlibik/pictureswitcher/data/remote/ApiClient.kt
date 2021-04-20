package ru.bimlibik.pictureswitcher.data.remote

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory

object ApiClient {

    private const val BASE_URL = "https://api.unsplash.com/"
    private lateinit var retrofit: Retrofit

    @ExperimentalSerializationApi
    val client: PictureApiInterface
        get() {
            val contentType = MediaType.get("application/json")
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(Json{ ignoreUnknownKeys = true }.asConverterFactory(contentType))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
            return retrofit.create(PictureApiInterface::class.java)
        }
}