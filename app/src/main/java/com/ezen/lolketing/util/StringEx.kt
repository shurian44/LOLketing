package com.ezen.lolketing.util

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

fun Long.timestampToString(format: String = "yyyy-MM-dd HH:mm") : String {
    val timeFormat = SimpleDateFormat(format, Locale.KOREA)
    return timeFormat.format(this)
}

fun Long.priceFormat() : String =
    DecimalFormat("###,###").format(this).plus("원")

fun Int.priceFormat() : String =
    this.toLong().priceFormat()

fun String.removePriceFormat(): Long = try {
    this.replace(",", "").replace("원", "").toLong()
} catch (e: Exception) {
    0L
}

// 쿠폰 유효기간 계산 : 60일
fun getCouponValidityPeriod() : String {
    val lateDay60 = System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 60L)
    return lateDay60.timestampToString()
}