package com.ezen.lolketing.view.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    labelLarge = TextStyle(
        color = White,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    ),
    labelMedium = TextStyle(
        color = White,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold
    ),
    labelSmall = TextStyle(
        color = White,
        fontSize = 16.sp
    ),
    displaySmall = TextStyle(
        color = White,
        fontSize = 12.sp
    ),
    titleMedium = TextStyle(
        color = SubColor,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold
    ),
    titleLarge = TextStyle(
        color = SubColor,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    )
)