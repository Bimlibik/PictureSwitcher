package ru.bimlibik.pictureswitcher.data

import ru.bimlibik.pictureswitcher.utils.DEFAULT_PAGE


data class Query(
    var forceUpdate: Boolean = false,
    var category: String? = null,
    var page: Int = DEFAULT_PAGE
) {
    val nextPage: Int
        get() = ++page
}