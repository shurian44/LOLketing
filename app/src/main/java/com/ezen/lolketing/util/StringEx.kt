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

fun getGrade(point: Long) = when(point){
    in 0..2999-> Grade.BRONZE.gradeCode
    in 3000..29999 -> Grade.SILVER.gradeCode
    in 30000..299999 -> Grade.GOLD.gradeCode
    else -> Grade.PLATINUM.gradeCode
}