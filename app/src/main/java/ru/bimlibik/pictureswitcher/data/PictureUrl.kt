package ru.bimlibik.pictureswitcher.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PictureUrl(

    @SerialName("raw")
    val raw: String,

    @SerialName("full")
    val full: String,

    @SerialName("regular")
    val regular: String,

    @SerialName("small")
    val small: String,

    @SerialName("thumb")
    val thumb: String
)