package de.ka.crunchr.ui.composables

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.ka.crunchr.ui.composables.utils.UiDefaults
import de.ka.crunchr.ui.composables.utils.shadowTap

@Composable
fun ActionButton(
    modifier: Modifier = Modifier,
    text: String = "",
    awaitOnTap: Boolean = false,
    onTap: () -> Unit = {},
    color: Color = MaterialTheme.colorScheme.primary,
    foregroundColor: Color? = MaterialTheme.colorScheme.tertiary,
    tintColor: Color = MaterialTheme.colorScheme.inversePrimary,
    @DrawableRes iconResId: Int? = null
) {
    Box(
        modifier = modifier.shadowTap(
            onTap = onTap,
            awaitOnTap = awaitOnTap,
            backgroundColor = color,
            foregroundColor = foregroundColor,
            borderColor = MaterialTheme.colorScheme.onSurface
        ),
        contentAlignment = Alignment.Center
    ) {
        if (iconResId == null) {
            Text(
                modifier = Modifier
                    .padding(UiDefaults.defaultPaddings),
                text = text,
                style = MaterialTheme.typography.titleMedium,
                color = tintColor,
                textAlign = TextAlign.Center
            )
        } else {
            Row(modifier = Modifier.padding(UiDefaults.defaultPaddings)) {
                Icon(
                    modifier = Modifier,
                    imageVector = ImageVector.vectorResource(id = iconResId),
                    contentDescription = text,
                    tint = tintColor
                )
                if (text.isNotEmpty()) {
                    Text(
                        modifier = Modifier.padding(horizontal = UiDefaults.smallPadding),
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
                modifier = Modifier.height(100.dp),
                iconResId = androidx.core.R.drawable.ic_call_decline
            )
            ActionButton(
                modifier = Modifier.height(100.dp),
                text = "Hello",
                iconResId = androidx.core.R.drawable.ic_call_decline
            )
        }

    }
}