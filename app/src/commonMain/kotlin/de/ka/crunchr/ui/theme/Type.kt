package de.ka.crunchr.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import de.ka.crunchr.generated.Res
import de.ka.crunchr.generated.inconsolata_bold
import de.ka.crunchr.generated.inconsolata_light
import de.ka.crunchr.generated.inconsolata_medium
import de.ka.crunchr.generated.inconsolata_regular
import org.jetbrains.compose.resources.Font

@Composable
fun InconsolataFontFamily(): FontFamily = FontFamily(
    Font(Res.font.inconsolata_light, FontWeight.Light),
    Font(Res.font.inconsolata_regular, FontWeight.Normal),
    Font(Res.font.inconsolata_medium, FontWeight.Medium),
    Font(Res.font.inconsolata_bold, FontWeight.Bold)
)
