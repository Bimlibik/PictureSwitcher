package ru.bimlibik.pictureswitcher

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.bimlibik.pictureswitcher.data.IPicturesRepository
import ru.bimlibik.pictureswitcher.data.PicturesDataSource
import ru.bimlibik.pictureswitcher.data.PicturesRepository
import ru.bimlibik.pictureswitcher.data.local.PicturesLocalDataSource
import ru.bimlibik.pictureswitcher.data.remote.PicturesRemoteDataSource
import ru.bimlibik.pictureswitcher.ui.favorite_pictures.FavoritePicturesViewModel
import ru.bimlibik.pictureswitcher.ui.picture_detail.PictureDetailViewModel
import ru.bimlibik.pictureswitcher.ui.pictures.PicturesViewModel

val appModule = module {

    single { Cicerone.create() }
    single { get<Cicerone<Router>>().router }
    single { get<Cicerone<Router>>().getNavigatorHolder() }

    single<PicturesDataSource.Remote> { PicturesRemoteDataSource() }
    single<PicturesDataSource.Local> { PicturesLocalDataSource() }
    single<IPicturesRepository> { PicturesRepository(get(), get()) }

    viewModel { PicturesViewModel(get(), get()) }
    viewModel { PictureDetailViewModel(get(), get()) }
    viewModel { FavoritePicturesViewModel(get(), get()) }
}