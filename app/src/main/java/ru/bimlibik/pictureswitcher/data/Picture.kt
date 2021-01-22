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
}