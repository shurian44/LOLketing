package com.ezen.lolketing.util

import java.text.SimpleDateFormat
import java.util.*

fun isCurrentDate(standardDate: String, format: String = "yyyyMMdd HH:mm") : Boolean {
    val date = 1000 * 60 * 60 * 24

    val dateFormat = SimpleDateFormat(format, Locale.KOREA)
    val currentTime = System.currentTimeMillis()
    val gameStartTime = dateFormat.parse(standardDate)?.time ?: 0

    return (currentTime / date) != (gameStartTime / date)
}

fun isPassedTime(standardDate: String, format: String = "HH:mm") : Boolean {
    val date = 1000 * 60 * 60 * 24

    val dateFormat = SimpleDateFormat(format, Locale.KOREA)
    val currentTime = System.currentTimeMillis()
    val gameStartTime = dateFormat.parse(standardDate)?.time ?: 0

    return (currentTime % date) >= (gameStartTime % date)
}