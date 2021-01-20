package ru.bimlibik.pictureswitcher.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Author(

    @SerialName("id")
    val id: String,

    @SerialName("name")
    val name: String
)