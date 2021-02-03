package ru.bimlibik.pictureswitcher.ui.pictures

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.bimlibik.pictureswitcher.R
import ru.bimlibik.pictureswitcher.data.IPicturesRepository
import ru.bimlibik.pictureswitcher.data.Picture
import ru.bimlibik.pictureswitcher.data.Result
import ru.bimlibik.pictureswitcher.data.Result.Success
import ru.bimlibik.pictureswitcher.utils.DEFAULT_PAGE
import ru.bimlibik.pictureswitcher.utils.Event
import ru.bimlibik.pictureswitcher.utils.FAVORITES

class PicturesViewModel(private val repository: IPicturesRepository) : ViewModel() {

    private val _forceUpdate = MutableLiveData(false)
    private val _category = MutableLiveData<String>()
    private val _page = MutableLiveData(DEFAULT_PAGE)

    private val _triggers =
        MediatorLiveData<Triple<Boolean?, String?, Int?>>().apply {
            addSource(_forceUpdate) { value = Triple(it, _category.value, _page.value) }
            addSource(_category) { value = Triple(_forceUpdate.value, it, _page.value) }
            addSource(_page) { value = Triple(_forceUpdate.value, _category.value, it) }
        }

    private val _pictures: LiveData<List<Picture>> = _triggers.switchMap { triple ->
        val forceUpdate: Boolean = triple.first ?: false
        val category: String? = triple.second
        val page: Int = triple.third ?: DEFAULT_PAGE

        if (category == FAVORITES) {
            repository.getFavorites().distinctUntilChanged().switchMap { loadFromFavorites(it) }
        } else {
            loadPictures(category, page)
        }
    }

    val pictures: LiveData<List<Picture>> = _pictures

    val empty: LiveData<Boolean> = Transformations.map(_pictures) {
        it == null || it.isEmpty()
    }

    private val _pictureDetailEvent = MutableLiveData<Event<Picture>>()
    val pictureDetailEvent: LiveData<Event<Picture>> = _pictureDetailEvent

    override fun onCleared() {
        super.onCleared()
        repository.close()
    }

    fun start() {
        repository.open()
    }

    fun searchPictures(itemId: Int, query: String) {
        when (itemId) {
            R.id.menu_nav_home -> _category.value = null
            R.id.menu_nav_favorite -> _category.value = FAVORITES
            else -> _category.value = query
        }
        _page.value = null
    }

    fun showDetail(picture: Picture) {
        _pictureDetailEvent.value = Event(picture)
    }

    fun loadMore() {
        val page = (_page.value ?: DEFAULT_PAGE) + 1
        _page.value = page
    }

    private fun loadFromFavorites(picturesResult: Result<List<Picture>>): LiveData<List<Picture>> {
        val result = MutableLiveData<List<Picture>>()

        if (picturesResult is Success) {
            result.value = picturesResult.data
        } else {
            result.value = emptyList()
        }

        return result
    }

    private fun loadPictures(query: String?, page: Int): LiveData<List<Picture>> {
        val result = MutableLiveData<List<Picture>>()

        viewModelScope.launch {
            val remoteResult = repository.getPictures(query, page)

            if (remoteResult is Success) {
                result.value = remoteResult.data
            } else {
                result.value = emptyList()
            }
        }

        return result
    }
}
