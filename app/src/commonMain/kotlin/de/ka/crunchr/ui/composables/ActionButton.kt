package de.ka.crunchr.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
fun ActionButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String = "",
    awaitOnTap: Boolean = false,
    onTap: () -> Unit = {},
    color: Color = MaterialTheme.colorScheme.primary,
    foregroundColor: Color? = null,
    tintColor: Color = MaterialTheme.colorScheme.inversePrimary,
    cornerRadius: Dp = 0.dp,
    iconResId: DrawableResource? = null
) {
    Box(
        modifier = modifier.shadowTap(
            enabled = enabled,
            onTap = onTap,
            awaitOnTap = awaitOnTap,
            backgroundColor = color,
            foregroundColor = foregroundColor,
            borderColor = MaterialTheme.colorScheme.onSurface,
            cornersRadius = cornerRadius,
            corners = Corners.All()
        ),
        contentAlignment = Alignment.Center
    ) {
        val alpha = if (!enabled) 0.4f else 1f
        if (iconResId == null) {
            Text(
                modifier = Modifier
                    .alpha(alpha)
                    .padding(UiDefaults.defaultPaddings),
                text = text,
                style = MaterialTheme.typography.titleMedium,
                color = tintColor,
                textAlign = TextAlign.Center
            )
        } else {
            Row(modifier = Modifier.padding(UiDefaults.defaultPaddings)) {
                Icon(
                    modifier = Modifier.alpha(alpha),
                    imageVector = vectorResource(iconResId),
                    contentDescription = text,
                    tint = tintColor
                )
                if (text.isNotEmpty()) {
                    Text(
                        modifier = Modifier
                            .alpha(alpha)
                            .padding(horizontal = UiDefaults.smallPadding),
                        text = text,
                        style = MaterialTheme.typography.titleMedium,
                        color = tintColor,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }

}
