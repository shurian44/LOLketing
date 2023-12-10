package com.ezen.lolketing.util

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.ezen.lolketing.R
import com.ezen.lolketing.model.BoardItem

@BindingAdapter("timestamp")
fun setTimestamp(textView: TextView, timestamp: Long) {
    textView.text = timestamp.timestampToString()
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
    if (url.isNullOrEmpty()) return
    setGlide(imageView, url)
}

@BindingAdapter("backgroundColorRes")
fun setBackgroundColor(view: View, @ColorRes color: Int) {
    view.setBackgroundColor(ContextCompat.getColor(view.context, color))
}

@BindingAdapter("imageRes")
fun imageLoad(imageView: ImageView, @DrawableRes resId: Int) {
    imageView.setImageResource(resId)
}