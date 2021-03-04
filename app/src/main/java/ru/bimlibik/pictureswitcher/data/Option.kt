package ru.bimlibik.pictureswitcher.data

import com.google.firebase.firestore.DocumentSnapshot


data class Option(
    var forceUpdate: Boolean = false,
    var category: String? = null,
    var query: String? = null,
    var lastVisiblePicture: DocumentSnapshot? = null
) {

    fun isLastPage(): Boolean {
        val id = lastVisiblePicture?.id

        if (id == null) {
            return false
        } else {
            if (id.toInt() <= 0) {
                return true
            }
            return false
        }
    }

    override fun toString(): String = "Query(forceUpdate = $forceUpdate, category = $category, " +
            "lastVisiblePictureId = ${lastVisiblePicture?.id})"
}