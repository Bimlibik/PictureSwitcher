package ru.bimlibik.pictureswitcher.ui.favorite_pictures

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.android.viewmodel.ext.android.viewModel
import ru.bimlibik.pictureswitcher.databinding.FragmentFavoritePicturesBinding

class FavoritePicturesFragment : Fragment() {

    private val viewModel: FavoritePicturesViewModel by viewModel()
    private lateinit var viewDataBinding: FragmentFavoritePicturesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = FragmentFavoritePicturesBinding
            .inflate(inflater, container, false)
            .apply { viewModel = this@FavoritePicturesFragment.viewModel }
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.lifecycleOwner = this

        setupAdapter()
        setupToolbar()
    }

    private fun setupAdapter() {
        viewDataBinding.recycler.adapter = FavoritePicturesAdapter(viewModel)
    }

    private fun setupToolbar() {
        viewDataBinding.toolbar.setNavigationOnClickListener {
            viewModel.goBack()
        }
    }
}