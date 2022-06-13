package com.ezen.lolketing.util

import android.widget.TextView
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter
import com.ezen.lolketing.model.Board
import com.ezen.lolketing.view.custom.CustomSeatCheckBox

@BindingAdapter("timestamp")
fun setTimestamp(textView: TextView, timestamp: Long) {
    textView.text = timestamp.timestampToString()
}

@BindingAdapter(value = ["seatEnable2", "seatChecked2"])
fun setSeatCheckBoxStatus(checkBox : CustomSeatCheckBox, isEnabled : Boolean = true, isChecked: Boolean = false) {
    checkBox.isEnabled = isEnabled
    checkBox.isChecked = isChecked
}

@BindingAdapter("html")
fun setHtml(textView: TextView, str: String) {
    textView.text = str.htmlFormat()
}

@BindingAdapter("boardWriter")
fun setBoardWriter(textView: TextView, boardItem: Board.BoardItem.BoardListItem) {
    val writer = "${boardItem.nickname} ${boardItem.timestamp?.timestampToString("yyyy.MM.dd")} 조회 ${boardItem.views}"
    textView.text = writer
}