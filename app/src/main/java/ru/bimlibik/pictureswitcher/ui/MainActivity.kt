package ru.bimlibik.pictureswitcher.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import org.koin.android.ext.android.inject
import ru.bimlibik.pictureswitcher.R
import ru.bimlibik.pictureswitcher.Screens
import ru.bimlibik.pictureswitcher.utils.isFirstStart
import ru.bimlibik.pictureswitcher.utils.setSizeParams

class MainActivity : AppCompatActivity() {

    private val navigator = AppNavigator(this, R.id.fragment_container)
    private val navigatorHolder by inject<NavigatorHolder>()
    private val router by inject<Router>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        computeScreenSize()
        navigatorHolder.setNavigator(navigator)
        router.newRootScreen(Screens.picturesScreen())
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