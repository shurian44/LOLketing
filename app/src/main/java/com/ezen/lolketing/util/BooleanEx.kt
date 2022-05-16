package com.ezen.lolketing.util

import java.text.SimpleDateFormat
import java.util.*

fun isCurrentDate(standardDate: String) : Boolean {
    val date = 1000 * 60 * 60 * 24

    val dateFormat = SimpleDateFormat("yyyyMMdd HH:mm", Locale.KOREA)
    val currentTime = System.currentTimeMillis()
    val gameStartTime = dateFormat.parse(standardDate)?.time ?: 0

    return (currentTime / date) != (gameStartTime / date)
}