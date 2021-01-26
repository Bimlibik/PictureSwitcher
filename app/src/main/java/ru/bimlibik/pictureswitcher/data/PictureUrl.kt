package ru.bimlibik.pictureswitcher.data

import io.realm.RealmObject
import io.realm.annotations.RealmClass
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@RealmClass(embedded = true)
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

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is PictureUrl) return false

        return raw == other.raw
                && full == other.full
                && regular == other.regular
                && small == other.small
                && thumb == other.thumb
    }

    override fun hashCode(): Int {
        var result = raw?.hashCode() ?: 0
        result = 31 * result + (full?.hashCode() ?: 0)
        result = 31 * result + (regular?.hashCode() ?: 0)
        result = 31 * result + (small?.hashCode() ?: 0)
        result = 31 * result + (thumb?.hashCode() ?: 0)
        return result
    }
}