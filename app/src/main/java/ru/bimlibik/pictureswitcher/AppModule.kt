package ru.bimlibik.pictureswitcher

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.bimlibik.pictureswitcher.data.IPicturesRepository
import ru.bimlibik.pictureswitcher.data.PicturesDataSource
import ru.bimlibik.pictureswitcher.data.PicturesRepository
import ru.bimlibik.pictureswitcher.data.remote.PicturesRemoteDataSource
import ru.bimlibik.pictureswitcher.ui.pictures.PicturesViewModel

val appModule = module {

    single<PicturesDataSource> { PicturesRemoteDataSource() }
    single<IPicturesRepository> { PicturesRepository(get()) }
    viewModel { PicturesViewModel(get()) }
}