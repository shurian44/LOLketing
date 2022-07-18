package com.ezen.lolketing.view.dialog

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.ezen.lolketing.databinding.DialogSearchMenuBinding

class SearchMenuPopup(
    private val layoutInflater: LayoutInflater
) {

    private val binding : DialogSearchMenuBinding by lazy { DialogSearchMenuBinding.inflate(layoutInflater) }
    private lateinit var popupWindow: PopupWindow
    private var writerClickListener : (() -> Unit)? = null
    private var boardTitleClickListener : (() -> Unit)? = null

    fun createPopup() = with(binding) {
        popupWindow = PopupWindow(
            binding.root,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).also {
            it.isTouchable = true
            it.isFocusable = true
            it.isOutsideTouchable = true
            it.elevation = 20f
        }
        popup = this@SearchMenuPopup
    }

    fun setListener(
        writerClickListener : (() -> Unit)? = null,
        boardTitleClickListener : (() -> Unit)? = null,
    ) {
        this.writerClickListener = writerClickListener
        this.boardTitleClickListener = boardTitleClickListener
    }

    fun showPopup(view: View) {
        popupWindow.animationStyle = -1
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
    }

    fun onCancel(view: View) {
        popupWindow.dismiss()
    }

    fun onBoardTitleClick(view: View){
        writerClickListener?.invoke()
    }

    fun onWriterClick(view: View){
        boardTitleClickListener?.invoke()
    }

}