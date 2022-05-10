package com.ezen.lolketing.util

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("timestamp")
fun setTimestamp(textView: TextView, timestamp: Long) {
    textView.text = timestamp.timestampToString()
}

