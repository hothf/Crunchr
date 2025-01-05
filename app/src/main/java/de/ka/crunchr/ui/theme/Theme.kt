package de.ka.crunchr.ui.theme

import android.app.Activity
import android.content.res.Configuration
import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(

)

private val LightColorScheme = lightColorScheme(
    primary = light,
    secondary = accent,
    tertiary = glow,
    onBackground = solveColor,
    onTertiary = lightText,
    onError = clearColor,
    onPrimary = darkText,
    onSecondary = veryLightText,
    onSecondaryContainer = mediumText,
    onSurface = softColor,
    inversePrimary = darkText,
    outline = successGreen,
    error = errorRed,
    onTertiaryContainer = black,
    surfaceVariant = dark

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun CrunchrTheme(
    darkTheme: Boolean = false, //isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    val configuration = LocalConfiguration.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            val color =
                if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) colorScheme.onTertiaryContainer.toArgb() else colorScheme.primary.toArgb()
            window.navigationBarColor = color
            window.statusBarColor = color

            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = true
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}