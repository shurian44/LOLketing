package com.ezen.lolketing.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
fun Long.timestampToString(format: String = "yyyy-MM-dd HH:mm") : String {
    val timeFormat = SimpleDateFormat(format)
    return timeFormat.format(this)
}