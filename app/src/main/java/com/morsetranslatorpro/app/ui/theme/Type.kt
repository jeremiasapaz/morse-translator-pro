package com.morsetranslatorpro.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

val AppTypography = Typography()

/** A scaled-up typography set used when the accessibility "large text" toggle is on. */
val LargeTypography = Typography(
    bodyLarge = TextStyle(fontSize = 20.sp, lineHeight = 28.sp),
    bodyMedium = TextStyle(fontSize = 18.sp, lineHeight = 26.sp),
    titleLarge = TextStyle(fontSize = 26.sp, lineHeight = 34.sp),
    titleMedium = TextStyle(fontSize = 22.sp, lineHeight = 30.sp),
    headlineSmall = TextStyle(fontSize = 28.sp, lineHeight = 36.sp),
    labelLarge = TextStyle(fontSize = 18.sp, lineHeight = 24.sp)
)
