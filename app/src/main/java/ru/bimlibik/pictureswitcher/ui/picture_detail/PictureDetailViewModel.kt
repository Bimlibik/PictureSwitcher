package ru.bimlibik.pictureswitcher.ui.picture_detail

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.bimlibik.pictureswitcher.data.IPicturesRepository
import ru.bimlibik.pictureswitcher.data.Picture

class PictureDetailViewModel(private val repository: IPicturesRepository) : ViewModel() {

    private val _picture = MutableLiveData<Picture>()

    private val _pictureUrl: LiveData<String?> = _picture.map { picture ->
        picture.urls?.small
    }
    val pictureUrl: LiveData<String?> = _pictureUrl

    private val _author: LiveData<String?> = _picture.map { picture -> picture.author?.name }
    val author: LiveData<String?> = _author

    fun start(picture: Picture) {
        _picture.value = picture
    }

    fun updateFavorite() {
        _picture.value?.let { picture ->
            viewModelScope.launch {
                repository.updateFavorite(picture)
            }

        }
    }


}