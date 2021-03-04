package ru.bimlibik.pictureswitcher.utils

import android.content.Context
import ru.bimlibik.pictureswitcher.PictureSwitcherApp

private const val PREF_NAME = "PictureSwitcherPrefs"
private const val FIRST_START = "first start"
private const val WIDTH = "width"
private const val WIDTH_DEFAULT = 1080

private val prefs by lazy {
    PictureSwitcherApp.get().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
}

fun isFirstStart(): Boolean = prefs.getBoolean(FIRST_START, true)

fun changeFirstStart(isFirst: Boolean = false) {
    prefs.edit().putBoolean(FIRST_START, isFirst).apply()
}

fun getFullSize(): Int = prefs.getInt(WIDTH, WIDTH_DEFAULT)

fun getHalfSize(): Int = prefs.getInt(WIDTH, WIDTH_DEFAULT) / 2

fun setSizeParams(width: Int) {
    prefs.edit().putInt(WIDTH, width).apply()
    changeFirstStart()
}