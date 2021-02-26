package ru.bimlibik.pictureswitcher.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.bimlibik.pictureswitcher.data.Picture


@Serializable
data class PictureResponse(

    val key: String?,

    @SerialName("results")
    val pictures: List<Picture>
)