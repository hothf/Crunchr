package de.ka.crunchr.ui.game.subscreens

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import de.ka.crunchr.R
import de.ka.crunchr.ui.composables.AnimatedContainer
import de.ka.crunchr.ui.composables.DefaultMenuEntry
import de.ka.crunchr.ui.composables.Menu
import de.ka.crunchr.ui.composables.SpacerMenuEntry
import de.ka.crunchr.ui.game.GameInteractions
import de.ka.crunchr.ui.game.SettingsInteractions
import de.ka.crunchr.ui.theme.CrunchrTheme

@Composable
fun GameNotStartedScreen(
    isVisible: Boolean = false,
    gameInteractions: GameInteractions = GameInteractions(),
    settingsInteractions: SettingsInteractions = SettingsInteractions()
) {
    AnimatedContainer(isVisible = isVisible) {
        Menu(
            menuTitle = stringResource(id = R.string.not_started_title), menuEntries = listOf(
                DefaultMenuEntry(
                    title = stringResource(id = R.string.not_start_new),
                    action = settingsInteractions.onOpenLevelSelect,
                    color = MaterialTheme.colorScheme.onBackground,
                    iconResId = R.drawable.ic_play
                ),
                SpacerMenuEntry,
                DefaultMenuEntry(
                    title = stringResource(id = R.string.not_started_settings),
                    action = { settingsInteractions.onOpenSettings(true) },
                    iconResId = R.drawable.ic_settings
                ),
                SpacerMenuEntry,
                DefaultMenuEntry(
                    title = stringResource(id = R.string.not_started_exit),
                    action = gameInteractions.onExit,
                    color = MaterialTheme.colorScheme.onError,
                    iconResId = R.drawable.ic_exit
                )
            )
        )
    }
}

@Composable
@Preview
fun PreviewGameNotStartedScreen() {
    CrunchrTheme {
        GameNotStartedScreen(isVisible = true)
    }
}