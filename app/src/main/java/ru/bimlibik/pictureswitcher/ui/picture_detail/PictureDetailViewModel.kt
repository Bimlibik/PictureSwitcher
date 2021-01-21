package ru.bimlibik.pictureswitcher.ui.picture_detail

import android.util.Log
import androidx.lifecycle.*
import ru.bimlibik.pictureswitcher.data.Picture

class PictureDetailViewModel : ViewModel() {

    private val _picture = MutableLiveData<Picture>()

    private val _pictureUrl: LiveData<String> = _picture.map { picture -> picture.urls.regular }
    val pictureUrl: LiveData<String> = _pictureUrl

    private val _author: LiveData<String> = _picture.map { picture -> picture.author.name }
    val author: LiveData<String> = _author

    fun start(picture: Picture) {
        _picture.value = picture
        Log.i("TAG2", "start: picture - ${picture.urls.raw}")
    }


}