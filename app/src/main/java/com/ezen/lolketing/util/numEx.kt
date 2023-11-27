package com.ezen.lolketing.util

import android.content.Context
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

fun getGrade(point: Long) = when(point){
    in 0..2999-> Grade.BRONZE.gradeCode
    in 3000..29999 -> Grade.SILVER.gradeCode
    in 30000..299999 -> Grade.GOLD.gradeCode
    else -> Grade.PLATINUM.gradeCode
}

fun getSimpleDateFormatMillSec(date: String, format : String = "yyyy.MM.dd HH:mm") : Long {
    val timeFormat = SimpleDateFormat(format, Locale.KOREA)
    return timeFormat.parse(date)?.time ?: 0
}

fun densityDp(context: Context, size: Int) : Int {
    val dm = context.resources.displayMetrics
    return (size * dm.density).roundToInt()
}