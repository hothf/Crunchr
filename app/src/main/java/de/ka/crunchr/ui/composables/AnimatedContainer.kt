package de.ka.crunchr.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun AnimatedContainer(isVisible: Boolean, content: @Composable () -> Unit = {}) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box {
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