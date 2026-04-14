package de.ka.crunchr.ui.game.subscreens

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import de.ka.crunchr.generated.Res
import de.ka.crunchr.generated.not_start_new
import de.ka.crunchr.generated.not_started_exit
import de.ka.crunchr.generated.not_started_settings
import de.ka.crunchr.generated.not_started_title
import de.ka.crunchr.ui.composables.AnimatedContainer
import de.ka.crunchr.ui.composables.DefaultMenuEntry
import de.ka.crunchr.ui.composables.Menu
import de.ka.crunchr.ui.composables.SpacerMenuEntry
import de.ka.crunchr.ui.game.GameInteractions
import de.ka.crunchr.ui.game.SettingsInteractions
import org.jetbrains.compose.resources.stringResource

@Composable
fun GameNotStartedScreen(
    isVisible: Boolean = false,
    gameInteractions: GameInteractions = GameInteractions(),
    settingsInteractions: SettingsInteractions = SettingsInteractions()
) {
    AnimatedContainer(isVisible = isVisible) {
        Menu(
            menuTitle = stringResource(Res.string.not_started_title), menuEntries = listOf(
                DefaultMenuEntry(
                    title = stringResource(Res.string.not_start_new),
                    action = settingsInteractions.onOpenLevelSelect,
                    color = MaterialTheme.colorScheme.onBackground
                ),
                SpacerMenuEntry,
                DefaultMenuEntry(
                    title = stringResource(Res.string.not_started_settings),
                    action = { settingsInteractions.onOpenSettings(true) }
                ),
                SpacerMenuEntry,
                DefaultMenuEntry(
                    title = stringResource(Res.string.not_started_exit),
                    action = gameInteractions.onExit
                )
            ),
            isVisible = isVisible
        )
    }
}
