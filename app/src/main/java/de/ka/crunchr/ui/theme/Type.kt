package de.ka.crunchr.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import de.ka.crunchr.R

val inconsolata = FontFamily(
    Font(R.font.inconsolata_light, FontWeight.Light),
    Font(R.font.inconsolata_regular, FontWeight.Normal),
    Font(R.font.inconsolata_medium, FontWeight.Medium),
    Font(R.font.inconsolata_bold, FontWeight.Bold)
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = inconsolata,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = inconsolata,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp
    ),
    titleSmall = TextStyle(
        fontFamily = inconsolata,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = inconsolata,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.sp
    ),
    titleLarge = TextStyle(
        fontFamily = inconsolata,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = inconsolata,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.5.sp
    )

)