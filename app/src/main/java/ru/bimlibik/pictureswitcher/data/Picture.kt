package ru.bimlibik.pictureswitcher.data

import android.os.Parcelable
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
open class Picture(
    @SerialName("id")
    @PrimaryKey
    var id: @RawValue String? = null,

    @SerialName("urls")
    var urls: @RawValue PictureUrl? = null,

    @SerialName("user")
    var author: @RawValue Author? = null

) : Parcelable, RealmObject() {

    override fun toString(): String = "Picture(id = $id, author = ${author?.name})"

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Picture) return false
        return id == other.id && urls == other.urls && author == other.author
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (urls?.hashCode() ?: 0)
        result = 31 * result + (author?.hashCode() ?: 0)
        return result
    }

    fun copy(id: String? = this.id, urls: PictureUrl? = this.urls, author: Author? = this.author) =
        Picture(id, urls, author)

    fun getAuthorProfileLink(): String {
        val author = this.author
        if (author == null) {
            return "https://unsplash.com/?utm_source=pictureswitcher&utm_medium=referral"
        }
        return author.getProfileLink()
    }
}