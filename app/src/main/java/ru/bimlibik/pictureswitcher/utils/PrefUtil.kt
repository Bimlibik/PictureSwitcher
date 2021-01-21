package ru.bimlibik.pictureswitcher.utils

import android.content.Context
import ru.bimlibik.pictureswitcher.PictureSwitcherApp

private const val PREF_NAME = "PictureSwitcherPrefs"
private const val FIRST_START = "first start"
private const val SIZE_PARAMS = "size params"
private const val SIZE_PARAMS_DEFAULT = "&w=1500&dpr=2"

private val prefs by lazy {
    PictureSwitcherApp.get().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
}

fun isFirstStart(): Boolean = prefs.getBoolean(FIRST_START, true)

fun changeFirstStart(isFirst: Boolean = false) {
    prefs.edit().putBoolean(FIRST_START, isFirst).apply()
}

fun getSizeParams(): String = prefs.getString(SIZE_PARAMS, SIZE_PARAMS_DEFAULT)!!

fun setSizeParams(params: String) {
    prefs.edit().putString(SIZE_PARAMS, params).apply()
    changeFirstStart()
}