package ru.bimlibik.pictureswitcher.ui.picture_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.koin.android.viewmodel.ext.android.viewModel
import ru.bimlibik.pictureswitcher.R
import ru.bimlibik.pictureswitcher.databinding.FragmentPictureDetailBinding
import ru.bimlibik.pictureswitcher.utils.EventObserver
import ru.bimlibik.pictureswitcher.utils.setupSnackbar

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
        setupSnackbar()
        setupNavigation()
        setupToolbar()
    }

    private fun setupNavigation() {
        viewModel.authorProfileEvent.observe(viewLifecycleOwner, EventObserver {
            val action = PictureDetailFragmentDirections
                .actionPictureDetailFragmentToAuthorProfileFragment(
                    args.picture.getAuthorProfileLink(),
                    args.picture.author?.name
                )
            findNavController().navigate(action)
        })
    }

    private fun setupToolbar() {
        val toolbarTitle: TextView = requireActivity().findViewById(R.id.toolbar_title)
        toolbarTitle.text = null
    }

    private fun setupSnackbar() {
        view?.setupSnackbar(this, viewModel.snackbarText)
    }
}