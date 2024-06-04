package de.ka.crunchr.ui.game.subscreens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import de.ka.crunchr.ui.theme.CrunchrTheme

@Composable
fun LoadingScreen(isVisible: Boolean = true) {
    AnimatedVisibility(visible = isVisible, exit = fadeOut()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.secondary),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.onSecondary)
        }
    }
}

@Preview
@Composable
fun PreviewLoadingScreen() {
    CrunchrTheme {
        LoadingScreen()
    }
}