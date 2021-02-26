package ru.bimlibik.pictureswitcher.data

import ru.bimlibik.pictureswitcher.utils.NO_KEY


data class Query(
    var forceUpdate: Boolean = false,
    var category: String? = null,
    var key: String? = null
) {
    val lastItemKey: String?
        get() {
            val newKey = key

            if (newKey == null) {
                return null
            } else {
                if (newKey.toInt() <= 0) {
                    return NO_KEY
                }
                return (newKey.toInt() - 1).toString()
            }
        }

    val isLastPage: Boolean
        get() = lastItemKey == NO_KEY
}