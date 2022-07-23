package com.ezen.lolketing.util

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.ezen.lolketing.R
import com.ezen.lolketing.model.BoardItem
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

@BindingAdapter(value = ["boardTitle", "boardCategory"])
fun setBoardTitle(textView: TextView, boardTitle: String, boardCategory: String) {
    val codeName = findCodeName(boardCategory)
    val str = textView.context.getString(R.string.board_detail_title, codeName, boardTitle)
    textView.text = str.htmlFormat()
}

@BindingAdapter("boardWriter")
fun setBoardWriter(textView: TextView, boardItem: BoardItem.BoardListItem) {
    val writer = "${boardItem.nickname} ${boardItem.timestamp.timestampToString("yyyy.MM.dd")} 조회 ${boardItem.views}"
    textView.text = writer
}

@BindingAdapter("codeName")
fun setCodeName(textView: TextView, code: String) {
    textView.text = findCodeName(code)
}

@BindingAdapter("urlImage")
fun setImage(imageView: ImageView, url: String?) {
    if (url == null) return
    setGlide(imageView, url)
}