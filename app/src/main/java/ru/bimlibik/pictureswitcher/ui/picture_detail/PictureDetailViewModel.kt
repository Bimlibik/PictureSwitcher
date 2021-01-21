package ru.bimlibik.pictureswitcher.ui.picture_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import ru.bimlibik.pictureswitcher.data.Picture

class PictureDetailViewModel : ViewModel() {

    private val _picture = MutableLiveData<Picture>()

    private val _pictureUrl: LiveData<String> = _picture.map { picture ->
        picture.urls.small
    }
    val pictureUrl: LiveData<String> = _pictureUrl

    private val _author: LiveData<String> = _picture.map { picture -> picture.author.name }
    val author: LiveData<String> = _author

    fun start(picture: Picture) {
        _picture.value = picture
    }


}