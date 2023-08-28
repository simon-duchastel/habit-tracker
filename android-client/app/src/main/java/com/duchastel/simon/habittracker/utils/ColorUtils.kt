package com.duchastel.simon.habittracker.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils

fun blendColors(firstColor: Color, secondColor: Color, percentage: Float): Color {
    check(percentage in 0.0..1.0) {
        "Percentage must be a value between 0 (0%) and 1.0 (100%) - got $percentage"
    }

    val firstArgb = firstColor.toArgb()
    val secondArgb = secondColor.toArgb()

    val resultArgb = ColorUtils.blendARGB(firstArgb, secondArgb, percentage)

    return Color(resultArgb)
}