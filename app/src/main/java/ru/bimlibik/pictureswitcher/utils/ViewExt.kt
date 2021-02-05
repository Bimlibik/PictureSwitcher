package ru.bimlibik.pictureswitcher.utils

import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import ru.bimlibik.pictureswitcher.R

fun View.showSnackbar(message: String) {
    Snackbar
        .make(this, message, Snackbar.LENGTH_LONG)
        .show()
}


fun View.setupSnackbar(
    lifecycleOwner: LifecycleOwner,
    snackbarEvent: LiveData<Event<Int>>
) {
    snackbarEvent.observe(lifecycleOwner, { event ->
        event.getContentIfNotHandled()?.let {
            showSnackbar(context.getString(it))
        }
    })
}

fun Fragment.setupRefreshLayout(swipe: SwipeRefreshLayout) {
    swipe.setColorSchemeColors(
        ContextCompat.getColor(requireActivity(), R.color.colorAccent),
        ContextCompat.getColor(requireActivity(), R.color.colorSecondaryAccent)
    )
}