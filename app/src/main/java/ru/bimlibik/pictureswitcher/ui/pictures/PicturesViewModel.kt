package ru.bimlibik.pictureswitcher.ui.pictures

import androidx.lifecycle.*
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.launch
import ru.bimlibik.pictureswitcher.data.IPicturesRepository
import ru.bimlibik.pictureswitcher.data.Picture
import ru.bimlibik.pictureswitcher.data.Option
import ru.bimlibik.pictureswitcher.data.Result.Success
import ru.bimlibik.pictureswitcher.utils.Event
import timber.log.Timber

class PicturesViewModel(private val repository: IPicturesRepository) : ViewModel() {

    private val currentOption = Option()

    private val _trigger = MutableLiveData(currentOption)

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _pictures: LiveData<List<Picture>> = _trigger.switchMap { option ->
        if (option.forceUpdate) {
            _dataLoading.value = true
        }
        loadPictures(option.category, option.query, option.lastVisiblePicture)
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

    fun searchPictures(newQuery: String? = null, newCategory: String? = null) {
        updateOption(Option(query = newQuery, category = newCategory))
    }

    fun showDetail(picture: Picture) {
        _pictureDetailEvent.value = Event(picture)
    }

    fun loadMore() {
        if (currentOption.isLastPage()) {
            Timber.i("The user scrolled to the last page.")
            return
        }
        updateOption(
            Option(
                category = currentOption.category,
                query = currentOption.query,
                lastVisiblePicture = currentOption.lastVisiblePicture
            )
        )
    }

    /**
     * Swipe to refresh does not work correctly with firebase, therefore temporarily disabled.
     */
    fun refresh() {
        _dataLoading.value = false
//        updateQuery(Query(forceUpdate = true, category = currentQuery.category))
    }

    private fun updateOption(option: Option = Option()) {
        currentOption.apply {
            forceUpdate = option.forceUpdate
            category = option.category
            query = option.query
            lastVisiblePicture = option.lastVisiblePicture
        }
        _trigger.value = option
        Timber.i("Option updated: $currentOption")
    }

    private fun loadPictures(
        category:String?,
        query: String?,
        lastVisiblePicture: DocumentSnapshot?
    ): LiveData<List<Picture>> {
        val result = MutableLiveData<List<Picture>>()

        viewModelScope.launch {
            repository.getPictures(category, query, lastVisiblePicture) { remoteResult ->
                if (remoteResult is Success) {
                    Timber.i("Pictures uploaded successfully. Remote result size - ${remoteResult.data.pictures.size}")
                    result.value = remoteResult.data.pictures
                    currentOption.lastVisiblePicture = remoteResult.data.lastVisiblePicture
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
