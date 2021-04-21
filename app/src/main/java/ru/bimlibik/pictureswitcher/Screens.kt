package ru.bimlibik.pictureswitcher

import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.bimlibik.pictureswitcher.data.Picture
import ru.bimlibik.pictureswitcher.ui.author_profile.AuthorProfileFragment
import ru.bimlibik.pictureswitcher.ui.favorite_pictures.FavoritePicturesFragment
import ru.bimlibik.pictureswitcher.ui.picture_detail.PictureDetailFragment
import ru.bimlibik.pictureswitcher.ui.pictures.PicturesFragment

object Screens {

    fun picturesScreen() = FragmentScreen { PicturesFragment() }

    fun detailsScreen(picture: Picture) = FragmentScreen { PictureDetailFragment.newInstance(picture) }

    fun favoritesScreen() = FragmentScreen { FavoritePicturesFragment() }

    fun authorProfileScreen(link: String, authorName: String) = FragmentScreen {
        AuthorProfileFragment.newInstance(link, authorName)
    }
}