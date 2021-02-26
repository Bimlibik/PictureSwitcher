package ru.bimlibik.pictureswitcher.ui.pictures

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.bimlibik.pictureswitcher.data.IPicturesRepository
import ru.bimlibik.pictureswitcher.data.Picture
import ru.bimlibik.pictureswitcher.data.Query
import ru.bimlibik.pictureswitcher.data.Result.Success
import ru.bimlibik.pictureswitcher.utils.Event
import timber.log.Timber

class PicturesViewModel(private val repository: IPicturesRepository) : ViewModel() {

    private val currentQuery = Query()

    private val _trigger = MutableLiveData(currentQuery)

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _pictures: LiveData<List<Picture>> = _trigger.switchMap { query ->
        if (query.forceUpdate) {
            _dataLoading.value = true
        }
        loadPictures(query.category, null)
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

    fun searchPictures(newCategory: String?) {
        updateQuery(Query(category = newCategory))
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

    private fun loadPictures(query: String?, lastItemKey: String?): LiveData<List<Picture>> {
        val result = MutableLiveData<List<Picture>>()

        viewModelScope.launch {
            repository.getPictures(query, lastItemKey) { remoteResult ->
                if (remoteResult is Success) {
                    Timber.i("Pictures uploaded successfully.")
                    result.value = remoteResult.data
                } else {
                    Timber.e("Error while loading pictures: $remoteResult")
                    result.value = emptyList()
                }
            }
            _dataLoading.value = false
        }
        return result
    }
}
