package com.ezen.lolketing.util

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.ezen.lolketing.R

enum class Grade(val gradeCode: String, val gradeName: String, @DrawableRes val image: Int) {
    BRONZE("USER001", "브론즈", R.drawable.icon_bronze),
    SILVER("USER002", "실버", R.drawable.icon_silver),
    GOLD("USER003", "골드", R.drawable.icon_gold),
    PLATINUM("USER004", "플래티넘", R.drawable.icon_platinum),
    MASTER("USER005", "마스터", R.drawable.icon_master)
}

fun setGradeImageView(imageView: ImageView, gradeCode: String) {
    val res = Grade.values().find { it.gradeCode == gradeCode }?.image ?: Grade.BRONZE.image
    imageView.setImageResource(res)
}

fun getGradeName(gradeCode: String) =
    Grade.values().find { it.gradeCode == gradeCode }?.gradeName ?: Grade.BRONZE.gradeName

fun getGradeImageRes(gradeName: String) =
    Grade.values().find { it.gradeName == gradeName }?.image ?: Grade.BRONZE.image