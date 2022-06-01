package com.ezen.lolketing.view.main.ticket.detail

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ezen.lolketing.util.densityDp

class GridLayoutSpacing : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.setEmpty()

        val space = densityDp(view.context, 10)

        outRect.right = space / 2
        outRect.left = space / 2
        outRect.top = space / 2
        outRect.bottom = space / 2

    }

}