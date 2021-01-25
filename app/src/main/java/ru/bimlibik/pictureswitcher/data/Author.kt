package ru.bimlibik.pictureswitcher.data

import io.realm.RealmObject
import io.realm.annotations.RealmClass
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@RealmClass(embedded = true)
open class Author(

    @SerialName("id")
    var id: String? = null,

    @SerialName("name")
    var name: String? = null

) : RealmObject() {

    override fun toString(): String = "Author(id = $id, name = $name)"
}