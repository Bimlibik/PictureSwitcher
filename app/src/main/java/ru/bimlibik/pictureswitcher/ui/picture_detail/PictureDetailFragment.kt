package ru.bimlibik.pictureswitcher.ui.picture_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import org.koin.android.viewmodel.ext.android.viewModel
import ru.bimlibik.pictureswitcher.databinding.FragmentPictureDetailBinding

class PictureDetailFragment : Fragment() {

    private val viewModel: PictureDetailViewModel by viewModel()
    private lateinit var viewDataBinding: FragmentPictureDetailBinding

    private val args: PictureDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentPictureDetailBinding
            .inflate(inflater, container, false)
            .apply { viewModel = this@PictureDetailFragment.viewModel }
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.lifecycleOwner = this
        viewModel.start(args.picture)
    }
}