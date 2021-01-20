package ru.bimlibik.pictureswitcher.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Picture(
    @SerialName("id")
    val id: String,

    @SerialName("urls")
    val urls: PictureUrl,

    @SerialName("user")
    val author: Author
)