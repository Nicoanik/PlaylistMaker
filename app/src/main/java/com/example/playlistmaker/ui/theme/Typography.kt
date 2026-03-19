package com.example.playlistmaker.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = CustomTypography(
    ysRegular11 = TextStyle(
        fontFamily = YsDisplayFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp
    ),
    ysRegular16 = TextStyle(
        fontFamily = YsDisplayFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    ysMedium14 = TextStyle(
        fontFamily = YsDisplayFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    ),
    ysMedium19 = TextStyle(
        fontFamily = YsDisplayFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 19.sp
    ),
    ysMedium22 = TextStyle(
        fontFamily = YsDisplayFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp
    )
)

data class CustomTypography(
    val ysRegular11: TextStyle,
    val ysRegular16: TextStyle,
    val ysMedium14: TextStyle,
    val ysMedium19: TextStyle,
    val ysMedium22: TextStyle,
)