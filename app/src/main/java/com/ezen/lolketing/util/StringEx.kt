package com.ezen.lolketing.util

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

fun Long.timestampToString(format: String = "yyyy-MM-dd HH:mm") : String {
    val timeFormat = SimpleDateFormat(format, Locale.KOREA)
    return timeFormat.format(this)
}

fun Long.priceFormat() : String =
    DecimalFormat("###,###,###,###").format(this)

fun Int.priceFormat() : String =
    this.toLong().priceFormat()
