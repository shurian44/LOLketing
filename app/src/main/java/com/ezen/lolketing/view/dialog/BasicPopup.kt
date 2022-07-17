package com.ezen.lolketing.view.dialog

import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.PopupWindow
import androidx.annotation.MenuRes

object BoardPopup {

    fun createPopup(
        view : View,
        @MenuRes menuRes: Int,
        listener : (Int) -> Unit
    ) {
        val popup = PopupMenu(view.context, view)
        popup.menuInflater.inflate(menuRes, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            listener(item.itemId)
            true
        }

        popup.show()
    }
}