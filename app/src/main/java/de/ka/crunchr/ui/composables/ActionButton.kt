package de.ka.crunchr.ui.composables

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.ka.crunchr.R
import de.ka.crunchr.ui.composables.utils.Corners
import de.ka.crunchr.ui.composables.utils.UiDefaults
import de.ka.crunchr.ui.composables.utils.shadowTap

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
    @DrawableRes iconResId: Int? = null
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
                    imageVector = ImageVector.vectorResource(id = iconResId),
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

@Preview
@Composable
fun PreviewActionButton() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ActionButton(
                modifier = Modifier.height(100.dp), text = "SOLVE"
            )
            ActionButton(
                modifier = Modifier.height(100.dp), text = "SOLVE",
                foregroundColor = MaterialTheme.colorScheme.onTertiary
            )
            ActionButton(
                cornerRadius = 8.dp,
                modifier = Modifier.height(100.dp),
                iconResId = R.drawable.ic_done
            )
            ActionButton(
                modifier = Modifier.height(100.dp),
                text = "Hello",
                iconResId = R.drawable.ic_done
            )
            ActionButton(
                enabled = false,
                modifier = Modifier.height(100.dp),
                text = "Hello",
                iconResId = R.drawable.ic_done
            )
        }

    }
}