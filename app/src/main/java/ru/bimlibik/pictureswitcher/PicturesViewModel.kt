package ru.bimlibik.pictureswitcher

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.bimlibik.pictureswitcher.data.Picture
import ru.bimlibik.pictureswitcher.data.remote.ApiClient

class PicturesViewModel : ViewModel() {

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
        val category: String = triple.second ?: DEFAULT_CATEGORY
        val page: Int = triple.third ?: DEFAULT_PAGE
        loadPictures(category, page)
    }

    val pictures: LiveData<List<Picture>> = _pictures

    val empty: LiveData<Boolean> = Transformations.map(_pictures) {
        it == null || it.isEmpty()
    }

    private val _showScrim = MutableLiveData(false)
    val showScrim: LiveData<Boolean> = _showScrim



    private fun loadPictures(query: String, page: Int): LiveData<List<Picture>> {
        val result = MutableLiveData<List<Picture>>()
        viewModelScope.launch {
//            TODO("Load pictures from network")
            val remoteResult = ApiClient.client.getPictures(page = page)
            if (remoteResult == null) {
                result.value = emptyList()
            } else {
                result.value = remoteResult
            }
        }
        return result
    }
}

private const val DEFAULT_CATEGORY = ""
private const val DEFAULT_PAGE = 1
private const val MAX_PAGE = 25