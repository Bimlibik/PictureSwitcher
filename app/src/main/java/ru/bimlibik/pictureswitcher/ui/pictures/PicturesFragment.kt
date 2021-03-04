package ru.bimlibik.pictureswitcher.ui.pictures

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationView
import org.koin.android.viewmodel.ext.android.viewModel
import ru.bimlibik.pictureswitcher.R
import ru.bimlibik.pictureswitcher.databinding.FragmentPicturesBinding
import ru.bimlibik.pictureswitcher.utils.EventObserver
import ru.bimlibik.pictureswitcher.utils.hideKeyboard
import ru.bimlibik.pictureswitcher.utils.setupRefreshLayout

class PicturesFragment : Fragment() {

    private val viewModel: PicturesViewModel by viewModel()
    private lateinit var viewDataBinding: FragmentPicturesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = FragmentPicturesBinding.inflate(inflater, container, false)
            .apply {
                viewModel = this@PicturesFragment.viewModel
                root.hideKeyboard()
            }
        setHasOptionsMenu(true)
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.lifecycleOwner = this
        viewModel.start()

        setupAdapter()
        setupNavDrawerListener()
        setupNavigation()
        setupToolbarNavigation()
        setupToolbarMenu()
        setupRefreshLayout(viewDataBinding.swipe)
    }

    private fun setupNavigation() {
        viewModel.pictureDetailEvent.observe(viewLifecycleOwner, EventObserver {
            val action = PicturesFragmentDirections.actionPicturesToPictureDetail(it)
            findNavController().navigate(action)
        })
    }

    private fun setupNavDrawerListener() {
        val navDrawer: NavigationView = requireActivity().findViewById(R.id.nav_drawer)
        val drawerLayout: DrawerLayout = requireActivity().findViewById(R.id.drawer_layout)

        navDrawer.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_nav_home -> viewModel.searchPictures()
                R.id.menu_nav_favorite -> navigateToFavorites()
                else -> viewModel.searchPictures(newCategory = item.title.toString())
            }
            drawerLayout.closeDrawers()
            true
        }
    }

    private fun setupToolbarNavigation() {
        val drawerLayout: DrawerLayout = requireActivity().findViewById(R.id.drawer_layout)
        viewDataBinding.toolbar.setNavigationOnClickListener { drawerLayout.open() }
    }

    private fun setupToolbarMenu() {
        viewDataBinding.toolbar.inflateMenu(R.menu.pictures_menu)
        val searchItem = viewDataBinding.toolbar.menu.findItem(R.id.menu_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.searchPictures(newQuery = it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                viewModel.searchPictures()
                return true
            }
        })
    }

    private fun setupAdapter() {
        viewDataBinding.recycler.adapter = PicturesAdapter(viewModel)
    }

    private fun navigateToFavorites() {
        val action = PicturesFragmentDirections.actionPicturesToFavoritePictures()
        findNavController().navigate(action)
    }
}