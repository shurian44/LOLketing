package com.ezen.lolketing.util

import java.text.SimpleDateFormat
import java.util.*

fun isCurrentDate(standardDate: String, format: String = "yyyyMMdd HH:mm") : Boolean {
    val date = 1000 * 60 * 60 * 24

    val dateFormat = SimpleDateFormat(format, Locale.KOREA)
    val currentTime = Calendar.getInstance().also { removeTime(it) }
    val gameStartTime = Calendar.getInstance().also {
        it.timeInMillis = dateFormat.parse(standardDate)?.time ?: 0
        removeTime(it)
    }

    return (currentTime.timeInMillis / date) - (gameStartTime.timeInMillis / date) == 0L
}

private fun removeTime(calendar: Calendar) =
    calendar.also {
        it.set(Calendar.HOUR_OF_DAY, 0)
        it.set(Calendar.MINUTE, 0)
        it.set(Calendar.SECOND, 0)
        it.set(Calendar.MILLISECOND, 0)
    }

fun isPassedTime(standardDate: String, format: String = "HH:mm") : Boolean {
    val date = 1000 * 60 * 60 * 24

    val dateFormat = SimpleDateFormat(format, Locale.KOREA)
    val currentTime = System.currentTimeMillis()
    val gameStartTime = dateFormat.parse(standardDate)?.time ?: 0

    return (currentTime % date) >= (gameStartTime % date)
}

fun isPassedDate(standardDate: String, passed: Int, format: String = "yyyyMMdd HH:mm") : Boolean {
    val date = 1000 * 60 * 60 * 24

    val dateFormat = SimpleDateFormat(format, Locale.KOREA)
    val currentTime = Calendar.getInstance().also { removeTime(it) }
    val gameStartTime = Calendar.getInstance().also {
        it.timeInMillis = dateFormat.parse(standardDate)?.time ?: 0
        removeTime(it)
    }

    return (currentTime.timeInMillis / date) - (gameStartTime.timeInMillis / date) < passed * -1
}