package ru.bimlibik.pictureswitcher.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.bimlibik.pictureswitcher.R
import ru.bimlibik.pictureswitcher.utils.isFirstStart
import ru.bimlibik.pictureswitcher.utils.setSizeParams

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        computeScreenSize()
    }

    private fun computeScreenSize() {
        if (isFirstStart()) {
            val displayMetrics = resources.displayMetrics
            val width = displayMetrics.widthPixels
            setSizeParams(width)
        }
    }
}