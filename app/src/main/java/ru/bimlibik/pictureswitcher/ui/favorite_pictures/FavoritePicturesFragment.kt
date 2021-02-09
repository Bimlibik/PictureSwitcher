package ru.bimlibik.pictureswitcher.ui.favorite_pictures

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import org.koin.android.viewmodel.ext.android.viewModel
import ru.bimlibik.pictureswitcher.databinding.FragmentFavoritePicturesBinding
import ru.bimlibik.pictureswitcher.utils.EventObserver

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

        setupNavigation()
        setupAdapter()
        setupToolbar()
    }

    private fun setupNavigation() {
        viewModel.pictureDetailEvent.observe(viewLifecycleOwner, EventObserver { picture ->
            val action = FavoritePicturesFragmentDirections
                .actionFavoritePicturesToPictureDetail(picture)
            findNavController().navigate(action)
        })
    }

    private fun setupAdapter() {
        viewDataBinding.recycler.adapter = FavoritePicturesAdapter(viewModel)
    }

    private fun setupToolbar() {
        viewDataBinding.toolbar.setNavigationOnClickListener { view ->
            view.findNavController().navigateUp()
        }
    }
}