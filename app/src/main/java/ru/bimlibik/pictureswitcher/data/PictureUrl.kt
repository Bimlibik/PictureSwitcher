package ru.bimlibik.pictureswitcher.data

import io.realm.RealmObject
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
open class PictureUrl(

    @SerialName("raw")
    var raw: String? = null,

    @SerialName("full")
    var full: String? = null,

    @SerialName("regular")
    var regular: String? = null,

    @SerialName("small")
    var small: String? = null,

    @SerialName("thumb")
    var thumb: String? = null

) : RealmObject() {

    override fun toString(): String =
        "PictureUrl(raw = $raw, full = $full, regular = $regular, small = $small, thumb = $thumb)"
}