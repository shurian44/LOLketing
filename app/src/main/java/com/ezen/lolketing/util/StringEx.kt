package com.ezen.lolketing.util

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

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

fun checkPasswordFormat(password: String): Boolean {
    val pattern = "\\w+"
    return Pattern.matches(pattern, password) || password.length !in 6..20
}

fun getToday(format: String = "yyyy.MM.dd") = System.currentTimeMillis().timestampToString(format)

fun formatDate(year: Int, month: Int, day: Int): String {
    val calendar = Calendar.getInstance()
    calendar.set(year, month, day)

    val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
    return dateFormat.format(calendar.time)
}

fun getPreviousDay(currentDate: String): String {
    val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
    val date = dateFormat.parse(currentDate) ?: return currentDate
    val calendar = Calendar.getInstance()
    calendar.time = date
    calendar.add(Calendar.DAY_OF_MONTH, -1)

    return dateFormat.format(calendar.time)
}

fun getNextDay(currentDate: String): String {
    val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
    val date = dateFormat.parse(currentDate) ?: return currentDate
    val calendar = Calendar.getInstance()
    calendar.time = date
    calendar.add(Calendar.DAY_OF_MONTH, 1)

    return dateFormat.format(calendar.time)
}