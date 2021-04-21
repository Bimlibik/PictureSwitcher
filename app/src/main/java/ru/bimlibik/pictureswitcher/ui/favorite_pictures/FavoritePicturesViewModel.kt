package ru.bimlibik.pictureswitcher.ui.favorite_pictures

import androidx.lifecycle.*
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.map
import ru.bimlibik.pictureswitcher.Screens
import ru.bimlibik.pictureswitcher.data.IPicturesRepository
import ru.bimlibik.pictureswitcher.data.Picture
import ru.bimlibik.pictureswitcher.data.Result
import ru.bimlibik.pictureswitcher.utils.Event

class FavoritePicturesViewModel(
    repository: IPicturesRepository,
    private val router: Router
) : ViewModel() {

    private val _pictures: LiveData<List<Picture>> = repository.getFavorites()
        .map { computeResult(it) }.asLiveData()

    val pictures: LiveData<List<Picture>> = _pictures

    val empty: LiveData<Boolean> = Transformations.map(_pictures) {
        it == null || it.isEmpty()
    }

    private val _pictureDetailEvent = MutableLiveData<Event<Picture>>()
    val pictureDetailEvent: LiveData<Event<Picture>> = _pictureDetailEvent


    fun showDetail(picture: Picture) {
        _pictureDetailEvent.value = Event(picture)
        router.navigateTo(Screens.detailsScreen(picture))
    }

    fun goBack() {
        router.exit()
    }

    private fun computeResult(picturesResult: Result<List<Picture>>): List<Picture> {
        return if (picturesResult is Result.Success) {
            picturesResult.data
        } else {
            emptyList()
        }
    }
}