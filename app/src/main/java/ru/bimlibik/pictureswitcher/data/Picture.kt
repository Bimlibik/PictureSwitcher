package ru.bimlibik.pictureswitcher.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Picture(
    @SerialName("id")
    val id: @RawValue String,

    @SerialName("urls")
    val urls: @RawValue PictureUrl,

    @SerialName("user")
    val author: @RawValue Author
) : Parcelable