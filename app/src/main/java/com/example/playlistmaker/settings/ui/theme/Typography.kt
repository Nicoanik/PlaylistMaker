package com.example.playlistmaker.settings.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = CustomTypography(
    ysMedium22 = TextStyle(
        fontFamily = YsDisplayFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp
    ),
    ysRegular16 = TextStyle(
        fontFamily = YsDisplayFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
)

data class CustomTypography(
    val ysMedium22: TextStyle,
    val ysRegular16: TextStyle
)