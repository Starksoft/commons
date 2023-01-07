package ru.starksoft.commons.ui

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import ru.starksoft.commons.BuildConfig
import kotlin.math.abs

object RecyclerViewHelper {

    private const val THRESHOLD = 5

    @JvmStatic
    fun init(
        recyclerView: RecyclerView,
        adapter: RecyclerView.Adapter<*>,
        hideKeyboard: Boolean = true,
        disableChangeAnimation: Boolean = true,
        addDecoration: Boolean = false
    ) {

        if (BuildConfig.DEBUG && recyclerView.layoutManager == null) {
            throw IllegalStateException("No layout manager attached!")
        }

        recyclerView.adapter = adapter

        //		if (addDecoration && adapter is BaseAdapter) {
        //			recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, adapter))
        //		}

        if (disableChangeAnimation) {
            (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }

        if (hideKeyboard) {
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    try {
                        if (abs(dy) > THRESHOLD) {
                            ViewUtils.hideSoftKeyboard(recyclerView)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })
        }
    }
}
