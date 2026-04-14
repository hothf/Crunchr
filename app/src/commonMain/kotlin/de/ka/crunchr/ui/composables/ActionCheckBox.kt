package de.ka.crunchr.ui.composables

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.ka.crunchr.ui.composables.utils.Corners
import de.ka.crunchr.ui.composables.utils.UiDefaults
import de.ka.crunchr.ui.composables.utils.shadowTap
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun ActionCheckBox(
    modifier: Modifier = Modifier,
    text: String = "",
    awaitOnTap: Boolean = false,
    checked: Boolean = false,
    onTap: (Boolean) -> Unit = {},
    color: Color = MaterialTheme.colorScheme.primary,
    foregroundColor: Color? = MaterialTheme.colorScheme.secondary.copy(alpha = 0.75f),
    tintColor: Color = MaterialTheme.colorScheme.inversePrimary,
    cornerRadius: Dp = UiDefaults.smallIconSize,
    iconResId: DrawableResource = UiDefaults.defaultCheckImageRes
) {
    Row(
        modifier = modifier.shadowTap(
            cornersRadius = cornerRadius,
            onTap = { onTap(!checked) },
            awaitOnTap = awaitOnTap,
            backgroundColor = color,
            foregroundColor = foregroundColor,
            corners = Corners.All(),
            borderColor = MaterialTheme.colorScheme.onSurface
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        var scale by remember { mutableFloatStateOf(if (checked) 1.0f else 0.0f) }
        val scaleAnim by animateFloatAsState(
            targetValue = scale,
            animationSpec = tween(
                durationMillis = UiDefaults.FAST_MS,
                easing = LinearOutSlowInEasing
            ), label = "scale"
        )
        LaunchedEffect(checked) {
            scale = if (checked) 1.0f else 0.0f
        }
        Icon(
            modifier = Modifier
                .padding(UiDefaults.defaultPaddings)
                .size(UiDefaults.defaultCheckBoxSize)
                .border(
                    BorderStroke(0.5.dp, color = tintColor),
                    RoundedCornerShape(8.dp)
                )
                .scale(scaleAnim)
                .padding(UiDefaults.defaultPaddings),
            imageVector = vectorResource(iconResId),
            contentDescription = text,
            tint = tintColor
        )
        Text(
            modifier = Modifier.padding(UiDefaults.defaultPaddings),
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = tintColor,
            textAlign = TextAlign.Start
        )
    }

}
