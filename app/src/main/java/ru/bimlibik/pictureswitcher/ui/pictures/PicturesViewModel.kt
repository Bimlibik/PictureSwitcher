package ru.bimlibik.pictureswitcher.ui.pictures

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.bimlibik.pictureswitcher.R
import ru.bimlibik.pictureswitcher.data.IPicturesRepository
import ru.bimlibik.pictureswitcher.data.Picture
import ru.bimlibik.pictureswitcher.data.Query
import ru.bimlibik.pictureswitcher.data.Result
import ru.bimlibik.pictureswitcher.data.Result.Success
import ru.bimlibik.pictureswitcher.utils.Event
import ru.bimlibik.pictureswitcher.utils.FAVORITES

class PicturesViewModel(private val repository: IPicturesRepository) : ViewModel() {

    private val currentQuery = Query()

    private val _trigger = MutableLiveData(currentQuery)

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _pictures: LiveData<List<Picture>> = _trigger.switchMap { query ->
        if (query.forceUpdate) {
            _dataLoading.value = true
        }

        if (query.category == FAVORITES) {
            repository.getFavorites().distinctUntilChanged().switchMap { loadFromFavorites(it) }
        } else {
            loadPictures(query.category, query.page)
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
            R.id.menu_nav_home -> updateQuery()
            R.id.menu_nav_favorite -> updateQuery(Query(category = FAVORITES))
            else -> updateQuery(Query(category = query))
        }
    }

    fun showDetail(picture: Picture) {
        _pictureDetailEvent.value = Event(picture)
    }

    fun loadMore() {
        updateQuery(Query(category = currentQuery.category, page = currentQuery.nextPage))
    }

    fun refresh() {
        updateQuery(Query(forceUpdate = true, category = currentQuery.category))
    }

    private fun updateQuery(query: Query = Query()) {
        currentQuery.apply {
            forceUpdate = query.forceUpdate
            category = query.category
            page = query.page
        }
        _trigger.value = currentQuery
    }

    private fun loadFromFavorites(picturesResult: Result<List<Picture>>): LiveData<List<Picture>> {
        val result = MutableLiveData<List<Picture>>()

        if (picturesResult is Success) {
            result.value = picturesResult.data
        } else {
            result.value = emptyList()
        }

        _dataLoading.value = false
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
            _dataLoading.value = false
        }

        return result
    }
}
