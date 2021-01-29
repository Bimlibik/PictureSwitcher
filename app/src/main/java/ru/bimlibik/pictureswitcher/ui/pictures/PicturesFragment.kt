package ru.bimlibik.pictureswitcher.ui.pictures

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationView
import org.koin.android.viewmodel.ext.android.viewModel
import ru.bimlibik.pictureswitcher.R
import ru.bimlibik.pictureswitcher.databinding.FragmentPicturesBinding
import ru.bimlibik.pictureswitcher.utils.EventObserver

class PicturesFragment : Fragment() {

    private val viewModel: PicturesViewModel by viewModel()
    private lateinit var viewDataBinding: FragmentPicturesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = FragmentPicturesBinding.inflate(inflater, container, false)
            .apply { viewModel = this@PicturesFragment.viewModel }
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.lifecycleOwner = this

        setupAdapter()
        setupNavDrawerListener()
        setupNavigation()
        setupToolbar()
    }

    private fun setupNavigation() {
        viewModel.pictureDetailEvent.observe(viewLifecycleOwner, EventObserver {
            val action = PicturesFragmentDirections.actionPicturesFragmentToPictureDetailFragment(it)
            findNavController().navigate(action)
        })
    }

    private fun setupNavDrawerListener() {
        val navDrawer: NavigationView = requireActivity().findViewById(R.id.nav_drawer)
        val drawerLayout: DrawerLayout = requireActivity().findViewById(R.id.drawer_layout)

        navDrawer.setNavigationItemSelectedListener { item ->
            viewModel.searchPictures(item.itemId, item.title.toString())
            drawerLayout.closeDrawers()
            true
        }
    }

    private fun setupToolbar() {
        val drawerLayout: DrawerLayout = requireActivity().findViewById(R.id.drawer_layout)
        viewDataBinding.toolbar.setNavigationOnClickListener { drawerLayout.open() }
    }

    private fun setupAdapter() {
        viewDataBinding.recycler.adapter = PicturesAdapter(viewModel)
    }
}