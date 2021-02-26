package ru.bimlibik.pictureswitcher.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Tag(
    var title: String? = null
) : Parcelable