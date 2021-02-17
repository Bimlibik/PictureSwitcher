package ru.bimlibik.pictureswitcher.data

import android.os.Parcelable
import io.realm.RealmObject
import io.realm.annotations.RealmClass
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
@RealmClass(embedded = true)
open class Author(

    @SerialName("id")
    var id: String? = null,

    @SerialName("name")
    var name: String? = null,

    @SerialName("username")
    var username: String? = null

) : Parcelable, RealmObject() {

    override fun toString(): String = "Author(id = $id, name = $name)"

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Author) return false
        return id == other.id && name == other.name
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        return result
    }

    fun getProfileLink(): String {
        if (username == null) {
            return "https://unsplash.com/?utm_source=pictureswitcher&utm_medium=referral"
        }
        return "https://unsplash.com/@$username?utm_source=pictureswitcher&utm_medium=referral"
    }
}