package com.ezen.lolketing.view.dialog

import android.graphics.Typeface
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout.LayoutParams
import android.widget.PopupWindow
import android.widget.TextView
import com.ezen.lolketing.R
import com.ezen.lolketing.databinding.PopupBasicMenuBinding
import com.ezen.lolketing.util.dpToPx

class BasicPopup(
    private val view: View,
    private val layoutInflater: LayoutInflater,
    private val list: List<PopupItem>
) {
    private val binding: PopupBasicMenuBinding by lazy {
        PopupBasicMenuBinding.inflate(
            layoutInflater
        )
    }
    private val popupWindow: PopupWindow = PopupWindow(
        binding.root,
        LayoutParams.WRAP_CONTENT,
        LayoutParams.WRAP_CONTENT
    ).also {
        it.isTouchable = true
        it.isFocusable = true
        it.isOutsideTouchable = true
        it.elevation = 20f
    }

    fun showPopup() {
        binding.popup = this
        list.forEachIndexed { index, item ->
            binding.linearLayout.addView(
                createPopupMenu(item, index % 2 == 0)
            )
        }
        popupWindow.animationStyle = -1
        popupWindow.showAsDropDown(view)
    }

    private fun createPopupMenu(
        item: PopupItem,
        isOdd: Boolean
    ) = TextView(view.context).also {
        it.text = item.text
        it.textSize = 16f
        it.setTextColor(it.context.getColor(R.color.white))
        it.setTypeface(null, Typeface.BOLD)
        it.setBackgroundColor(
            it.context.getColor(
                if (isOdd) R.color.light_black else R.color.black
            )
        )
        it.gravity = Gravity.CENTER
        it.layoutParams =
            LayoutParams(it.context.dpToPx(150), LayoutParams.WRAP_CONTENT)
        val paddingValue = it.context.dpToPx(8)
        it.setPadding(0, paddingValue, 0, paddingValue)
        it.setOnClickListener {
            item.onClick()
            dismiss()
        }
    }

    fun dismiss() {
        popupWindow.dismiss()
    }
}

data class PopupItem(
    val text: String,
    val onClick: () -> Unit
)