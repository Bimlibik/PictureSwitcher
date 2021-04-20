package ru.bimlibik.pictureswitcher

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.bimlibik.pictureswitcher.data.IPicturesRepository
import ru.bimlibik.pictureswitcher.data.PicturesDataSource
import ru.bimlibik.pictureswitcher.data.PicturesRepository
import ru.bimlibik.pictureswitcher.data.local.PicturesLocalDataSource
import ru.bimlibik.pictureswitcher.data.remote.ApiClient
import ru.bimlibik.pictureswitcher.data.remote.PicturesRemoteDataSource
import ru.bimlibik.pictureswitcher.ui.favorite_pictures.FavoritePicturesViewModel
import ru.bimlibik.pictureswitcher.ui.picture_detail.PictureDetailViewModel
import ru.bimlibik.pictureswitcher.ui.pictures.PicturesViewModel

val appModule = module {

    single { ApiClient.client }
    single<PicturesDataSource.Remote> { PicturesRemoteDataSource(get()) }
    single<PicturesDataSource.Local> { PicturesLocalDataSource() }
    single<IPicturesRepository> { PicturesRepository(get(), get()) }
    viewModel { PicturesViewModel(get()) }
    viewModel { PictureDetailViewModel(get()) }
    viewModel { FavoritePicturesViewModel(get()) }
}