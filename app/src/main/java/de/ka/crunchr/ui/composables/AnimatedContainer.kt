package de.ka.crunchr.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.ka.crunchr.ui.composables.utils.UiDefaults

@Composable
fun AnimatedContainer(isVisible: Boolean, content: @Composable () -> Unit = {}) {
    AnimatedVisibility(
        visible = isVisible,
        enter = expandIn(
            expandFrom = Alignment.Center
        ),
        exit = shrinkOut(
            shrinkTowards = Alignment.Center
        )
    ) {
        Box(modifier = Modifier.clip(RoundedCornerShape(12.dp))) {
            content()
        }

    }
}

@Composable
@Preview
fun PreviewAnimatedContainer() {
    MaterialTheme {
        AnimatedContainer(isVisible = true)
    }
}