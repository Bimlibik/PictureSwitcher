package ru.bimlibik.pictureswitcher

data class Picture(
    val id: Int,
    val previewUrl: String,
    val downloadUrl: String,
    val authorFirstName: String,
    val authorLastName: String,
    val isFavorites: Boolean
)