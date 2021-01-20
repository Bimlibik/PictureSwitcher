package ru.bimlibik.pictureswitcher.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.bimlibik.pictureswitcher.data.Picture


@Serializable
data class PictureResponse(

    @SerialName("total")
    val totalPictures: Int,

    @SerialName("total_pages")
    val totalPages: Int,

    @SerialName("results")
    val pictures: List<Picture>
)