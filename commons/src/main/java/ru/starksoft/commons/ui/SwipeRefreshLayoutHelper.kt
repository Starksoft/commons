package ru.starksoft.commons.ui

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.starksoft.commons.R

object SwipeRefreshLayoutHelper {

    @JvmStatic
    fun init(swipeRefreshLayout: SwipeRefreshLayout, listener: () -> Unit) {
        with(swipeRefreshLayout) {
            setOnRefreshListener(listener)
            setColorSchemeResources(
                R.color.swipe_color_1,
                R.color.swipe_color_2,
                R.color.swipe_color_3,
                R.color.swipe_color_4
            )
        }
    }
}
