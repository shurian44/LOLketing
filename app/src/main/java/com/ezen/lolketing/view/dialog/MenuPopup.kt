package com.ezen.lolketing.view.dialog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.ezen.lolketing.databinding.DialogMenuBinding

class MenuPopup(
    private val layoutInflater: LayoutInflater
) {

    private val binding : DialogMenuBinding by lazy { DialogMenuBinding.inflate(layoutInflater) }
    private lateinit var popupWindow: PopupWindow
    private var teamListener : (() -> Unit)? = null
    private var myBoardListener : (() -> Unit)? = null

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
        popup = this@MenuPopup
    }

    fun setListener(
        teamListener : (() -> Unit)? = null,
        myBoardListener : (() -> Unit)? = null,
    ) {
        this.teamListener = teamListener
        this.myBoardListener = myBoardListener
    }

    fun showPopup(view: View) {
        popupWindow.animationStyle = -1
        popupWindow.showAsDropDown(view)
    }

    fun onCancel(view: View) {
        popupWindow.dismiss()
    }

    fun onClub(view: View){
        teamListener?.invoke()
    }

    fun onMyBoard(view: View){
        myBoardListener?.invoke()
    }

}