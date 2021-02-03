package ru.bimlibik.pictureswitcher.ui.picture_detail

import androidx.lifecycle.*
import androidx.work.WorkInfo
import kotlinx.coroutines.launch
import ru.bimlibik.pictureswitcher.R
import ru.bimlibik.pictureswitcher.data.IPicturesRepository
import ru.bimlibik.pictureswitcher.data.Picture
import ru.bimlibik.pictureswitcher.utils.Event

class PictureDetailViewModel(private val repository: IPicturesRepository) : ViewModel() {

    private val _picture = MutableLiveData<Picture>()

    private val _showScrim = MutableLiveData(false)
    val showScrim: LiveData<Boolean> = _showScrim

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    private val _authorProfileEvent = MutableLiveData<Event<Unit>>()
    val authorProfileEvent: LiveData<Event<Unit>> = _authorProfileEvent

    private val _wallpaperEvent = MutableLiveData<Event<Unit>>()
    val wallpaperEvent: LiveData<Event<Unit>> = _wallpaperEvent

    fun start(picture: Picture) {
        _picture.value = picture
    }

    fun showAuthorProfile() {
        _authorProfileEvent.value = Event(Unit)
    }

    fun updateFavorite() {
        _picture.value?.let { picture ->
            viewModelScope.launch {
                val isFavorite = repository.updateFavorite(picture)
                showInfo(isFavorite)
            }
        }
    }

    fun setPictureAsWallpaper() {
        _wallpaperEvent.value = Event(Unit)
    }

    fun showProgress() {
        _showScrim.value = true
    }

    fun showInfo(info: WorkInfo.State) {
        when(info) {
            WorkInfo.State.SUCCEEDED -> {
                _showScrim.value = false
                _snackbarText.value = Event(R.string.snackbar_wallpaper_success)
            }
            WorkInfo.State.FAILED -> {
                _showScrim.value = false
                _snackbarText.value = Event(R.string.snackbar_wallpaper_error)
            }
            else -> return
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