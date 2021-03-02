package ru.bimlibik.pictureswitcher.data

import android.os.Parcelable
import io.realm.RealmModel
import io.realm.annotations.Ignore
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import ru.bimlibik.pictureswitcher.utils.getSizeParams

@RealmClass
@Serializable
@Parcelize
open class Picture(
    @SerialName("id")
    @PrimaryKey
    var id: @RawValue String? = null,

    @SerialName("author")
    var author: @RawValue Author? = null,

    @Transient
    var color: @RawValue Int = android.R.color.transparent,

    @Transient
    var category: @RawValue String? = null,

    @Transient
    var blurHash: @RawValue String? = null,

    @Transient
    var url: @RawValue String? = null,

    @Transient
    var source: @RawValue String? = null,

    @Transient
    @Ignore
    var tags: @RawValue List<String> = emptyList()

) : Parcelable, RealmModel {

    override fun toString(): String = "Picture(id = $id, author = ${author?.name})"

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Picture) return false
        return id == other.id && author == other.author
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (author?.hashCode() ?: 0)
        return result
    }

    fun copy(
        id: String? = this.id,
        author: Author? = this.author,
        color: Int = this.color,
        category: String? = this.category,
        blurHash: String? = this.blurHash,
        url: String? = this.url,
        source: String? = this.source,
        tags: List<String> = this.tags
    ) =
        Picture(id, author, color, category, blurHash, url, source, tags)

    fun getAuthorProfileLink(): String {
        val author = this.author
        if (author == null) {
            return "https://unsplash.com/?utm_source=pictureswitcher&utm_medium=referral"
        }
        return author.getProfileLink()
    }

    fun getPictureLink(): String = url + getSizeParams()
}