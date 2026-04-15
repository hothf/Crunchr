package de.ka.crunchr

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import de.ka.crunchr.data.DesktopStringResolver
import de.ka.crunchr.ui.game.GameDependencies
import de.ka.crunchr.ui.game.GameScreen
import de.ka.crunchr.ui.game.GameViewModel
import de.ka.crunchr.ui.theme.CrunchrTheme

fun main() = application {
    val viewModel = GameViewModel()
    viewModel.dependencies = GameDependencies(
        stringResolver = DesktopStringResolver()
    )
    viewModel.loadLastGameAndSettings()

    Window(
        onCloseRequest = ::exitApplication,
        title = "Crunchr"
    ) {
        CrunchrTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.primary
            ) {
                GameScreen(viewModel = viewModel) {
                    exitApplication()
                }
            }
        }
    }
}
