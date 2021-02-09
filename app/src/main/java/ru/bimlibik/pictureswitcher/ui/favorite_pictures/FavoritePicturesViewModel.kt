package ru.bimlibik.pictureswitcher.ui.favorite_pictures

import androidx.lifecycle.*
import ru.bimlibik.pictureswitcher.data.IPicturesRepository
import ru.bimlibik.pictureswitcher.data.Picture
import ru.bimlibik.pictureswitcher.data.Result
import ru.bimlibik.pictureswitcher.utils.Event

class FavoritePicturesViewModel(private val repository: IPicturesRepository) : ViewModel() {

    private val _pictures: LiveData<List<Picture>> = repository.getFavorites()
        .distinctUntilChanged().switchMap { loadFromFavorites(it) }

    val pictures: LiveData<List<Picture>> = _pictures

    val empty: LiveData<Boolean> = Transformations.map(_pictures) {
        it == null || it.isEmpty()
    }

    private val _pictureDetailEvent = MutableLiveData<Event<Picture>>()
    val pictureDetailEvent: LiveData<Event<Picture>> = _pictureDetailEvent


    fun showDetail(picture: Picture) {
        _pictureDetailEvent.value = Event(picture)
    }

    private fun loadFromFavorites(picturesResult: Result<List<Picture>>): LiveData<List<Picture>> {
        val result = MutableLiveData<List<Picture>>()

        if (picturesResult is Result.Success) {
            result.value = picturesResult.data
        } else {
            result.value = emptyList()
        }
        return result
    }
}