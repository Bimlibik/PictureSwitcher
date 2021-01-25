package ru.bimlibik.pictureswitcher.ui.picture_detail

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.bimlibik.pictureswitcher.R
import ru.bimlibik.pictureswitcher.data.IPicturesRepository
import ru.bimlibik.pictureswitcher.data.Picture
import ru.bimlibik.pictureswitcher.utils.Event

class PictureDetailViewModel(private val repository: IPicturesRepository) : ViewModel() {

    private val _picture = MutableLiveData<Picture>()

    private val _pictureUrl: LiveData<String?> = _picture.map { picture ->
        picture.urls?.small
    }
    val pictureUrl: LiveData<String?> = _pictureUrl

    private val _author: LiveData<String?> = _picture.map { picture -> picture.author?.name }
    val author: LiveData<String?> = _author

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    fun start(picture: Picture) {
        _picture.value = picture
    }

    fun updateFavorite() {
        _picture.value?.let { picture ->
            viewModelScope.launch {
                val isFavorite = repository.updateFavorite(picture)
                showInfo(isFavorite)
            }
        }
    }

    private fun showInfo(isFavorite: Boolean) {
        if (isFavorite) {
            _snackbarText.value = Event(R.string.snackbar_added_to_favorites)
        } else {
            _snackbarText.value = Event(R.string.snackbar_removed_from_favorites)
        }
    }

}