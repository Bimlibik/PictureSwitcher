package ru.bimlibik.pictureswitcher.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import ru.bimlibik.pictureswitcher.R
import ru.bimlibik.pictureswitcher.databinding.ActivityMainBinding
import ru.bimlibik.pictureswitcher.utils.isFirstStart
import ru.bimlibik.pictureswitcher.utils.setSizeParams

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfig: AppBarConfiguration
    private lateinit var viewDataBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewDataBinding.root)

        setSupportActionBar(viewDataBinding.toolbarLayout.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val navController = findNavController(R.id.fragment_container)

        appBarConfig = AppBarConfiguration(setOf(R.id.pictures_fragment), viewDataBinding.drawerLayout)
        setupActionBarWithNavController(navController, appBarConfig)

        viewDataBinding.navDrawer.setupWithNavController(navController)
        computeScreenSize()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragment_container)
        return navController.navigateUp(appBarConfig) || super.onSupportNavigateUp()
    }

    private fun computeScreenSize() {
        if (isFirstStart()) {
            val displayMetrics = resources.displayMetrics
            val width = displayMetrics.widthPixels
            val dpr = displayMetrics.density.toInt()
            val sizeParams = "&w=$width&dpr=$dpr"
            setSizeParams(sizeParams)
        }
    }
}