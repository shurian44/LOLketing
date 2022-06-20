package com.ezen.lolketing.view.dialog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.core.view.isVisible
import com.ezen.lolketing.databinding.DialogBoardMenuBinding

class BoardMenuPopup(
    private val layoutInflater: LayoutInflater
) {

    private val binding : DialogBoardMenuBinding by lazy { DialogBoardMenuBinding.inflate(layoutInflater) }
    private lateinit var popupWindow: PopupWindow

    private var modifyListener : (() -> Unit)? = null
    private var deleteListener : (() -> Unit)? = null
    private var commentListener : (() -> Unit)? = null
    private var reportListener : (() -> Unit)? = null

    fun createPopup(
        state: String,
    ) = with(binding) {
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
        popup = this@BoardMenuPopup

        when(state) {
            MY_BOARD -> {
                txtComment.isVisible = false
                txtReport.isVisible = false
            }
            ANOTHER_PERSON_BOARD -> {
                txtModify.isVisible = false
                txtDelete.isVisible = false
            }
            MY_COMMENT -> {
                txtModify.isVisible = false
                txtComment.isVisible = false
                txtReport.isVisible = false
            }
            ANOTHER_PERSON_COMMENT -> {
                txtModify.isVisible = false
                txtDelete.isVisible = false
                txtComment.isVisible = false
            }
        }
    }

    fun setListener(
        modifyListener : (() -> Unit)? = null,
        deleteListener : (() -> Unit)? = null,
        commentListener : (() -> Unit)? = null,
        reportListener : (() -> Unit)? = null
    ) {
        this.modifyListener = modifyListener
        this.deleteListener = deleteListener
        this.commentListener = commentListener
        this.reportListener = reportListener
    }

    fun showPopup(view: View) {
        popupWindow.animationStyle = -1
        popupWindow.showAsDropDown(view)
    }

    fun onCancel(view: View) {
        popupWindow.dismiss()
    }

    fun onModify(view: View) {
        modifyListener?.invoke()
        popupWindow.dismiss()
    }

    fun onDelete(view: View) {
        deleteListener?.invoke()
        popupWindow.dismiss()
    }

    fun onComment(view: View) {
        commentListener?.invoke()
        popupWindow.dismiss()
    }

    fun onReport(view: View){
        reportListener?.invoke()
        popupWindow.dismiss()
    }

    companion object {
        const val MY_BOARD = "my_board"
        const val ANOTHER_PERSON_BOARD = "another_person_board"
        const val MY_COMMENT = "my_comment"
        const val ANOTHER_PERSON_COMMENT = "another_person_comment"
    }

}