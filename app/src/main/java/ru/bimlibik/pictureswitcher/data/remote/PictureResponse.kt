package ru.bimlibik.pictureswitcher.data.remote

import com.google.firebase.firestore.DocumentSnapshot
import ru.bimlibik.pictureswitcher.data.Picture

data class PictureResponse(
    val pictures: List<Picture>,
    val lastVisiblePicture: DocumentSnapshot?
)