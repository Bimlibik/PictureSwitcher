package ru.bimlibik.pictureswitcher.ui.pictures

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.bimlibik.pictureswitcher.R
import ru.bimlibik.pictureswitcher.data.IPicturesRepository
import ru.bimlibik.pictureswitcher.data.Picture
import ru.bimlibik.pictureswitcher.data.Result.Success
import ru.bimlibik.pictureswitcher.utils.Event

class PicturesViewModel(private val repository: IPicturesRepository) : ViewModel() {

    private val loadedPictures = mutableListOf<Picture>()

    private val _forceUpdate = MutableLiveData(false)
    private val _category = MutableLiveData<String>()
    private val _page = MutableLiveData<Int>()

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
        loadPictures(category, page)
    }

    val pictures: LiveData<List<Picture>> = _pictures

    val empty: LiveData<Boolean> = Transformations.map(_pictures) {
        it == null || it.isEmpty()
    }

    private val _showScrim = MutableLiveData(false)
    val showScrim: LiveData<Boolean> = _showScrim

    private val _pictureDetailEvent = MutableLiveData<Event<Picture>>()
    val pictureDetailEvent: LiveData<Event<Picture>> = _pictureDetailEvent


    fun searchPictures(itemId: Int, query: String) {
        when(itemId) {
            R.id.menu_nav_home -> _category.value = null
            R.id.menu_nav_favorite -> _category.value = FAVORITES
            else -> _category.value = query
        }
    }

    fun showDetail(picture: Picture) {
        _pictureDetailEvent.value = Event(picture)
    }

    private fun loadPictures(query: String?, page: Int): LiveData<List<Picture>> {
        val result = MutableLiveData<List<Picture>>()
        viewModelScope.launch {
            val remoteResult = repository.getPictures(query, page, query == FAVORITES)
            if (remoteResult is Success) {
                result.value = remoteResult.data
            } else {
                result.value = emptyList()
            }
        }
        return result
    }
}

private const val DEFAULT_CATEGORY = "wallpapers"
private const val FAVORITES = "favorites"
private const val DEFAULT_PAGE = 1
private const val MAX_PAGE = 25