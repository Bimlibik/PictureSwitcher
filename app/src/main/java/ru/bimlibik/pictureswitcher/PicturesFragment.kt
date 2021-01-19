package ru.bimlibik.pictureswitcher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.bimlibik.pictureswitcher.databinding.FragmentPicturesBinding

class PicturesFragment : Fragment() {

    private val viewModel: PicturesViewModel = PicturesViewModel()
    private lateinit var viewDataBinding: FragmentPicturesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentPicturesBinding.inflate(inflater, container, false)
            .apply { viewModel = this@PicturesFragment.viewModel }
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.lifecycleOwner = this

        setupAdapter()
    }

    private fun setupAdapter() {
        viewDataBinding.recycler.adapter = PicturesAdapter(viewModel)
    }
}