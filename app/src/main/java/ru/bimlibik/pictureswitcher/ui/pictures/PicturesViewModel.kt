package ru.bimlibik.pictureswitcher.ui.pictures

import androidx.lifecycle.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.bimlibik.pictureswitcher.data.IPicturesRepository
import ru.bimlibik.pictureswitcher.data.Picture
import ru.bimlibik.pictureswitcher.data.Query
import ru.bimlibik.pictureswitcher.utils.Event
import timber.log.Timber

class PicturesViewModel(private val repository: IPicturesRepository) : ViewModel() {

    private val disposableBag = CompositeDisposable()

    private val currentQuery = Query()

    private val _trigger = MutableLiveData(currentQuery)

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _pictures: LiveData<List<Picture>> = _trigger.switchMap { query ->
        if (query.forceUpdate) {
            _dataLoading.value = true
        }
        loadPictures(query.category, query.page)
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
        disposableBag.clear()
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

    private fun loadPictures(query: String?, page: Int): LiveData<List<Picture>> {
        val result = MutableLiveData<List<Picture>>()

        val disposable = repository.getPictures(query, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { remoteResult -> result.value = remoteResult },
                { error ->
                    result.value = emptyList()
                    Timber.e("Error while loading pictures $error")
                },
                { _dataLoading.value = false }
            )
        disposableBag.add(disposable)
        return result
    }
}
