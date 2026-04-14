package de.ka.crunchr.ui.game.subscreens

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import de.ka.crunchr.generated.Res
import de.ka.crunchr.generated.ic_back
import de.ka.crunchr.generated.select_level
import de.ka.crunchr.generated.select_level_title
import de.ka.crunchr.generated.settings_back
import de.ka.crunchr.ui.composables.AnimatedContainer
import de.ka.crunchr.ui.composables.DefaultMenuEntry
import de.ka.crunchr.ui.composables.Menu
import de.ka.crunchr.ui.composables.SpacerMenuEntry
import de.ka.crunchr.ui.game.GameInteractions
import de.ka.crunchr.ui.game.getLevelSymbols
import de.ka.crunchrgame.models.Level
import org.jetbrains.compose.resources.stringResource

@Composable
fun LevelSelectScreen(
    isVisible: Boolean = false,
    gameInteractions: GameInteractions = GameInteractions(),
) {
    AnimatedContainer(isVisible = isVisible) {
        Menu(
            isVisible = isVisible,
            menuTitle = stringResource(Res.string.select_level_title),
            menuEntries = listOf(
                entryForLevel(level = Level(1), action = gameInteractions.onStart),
                entryForLevel(level = Level(10), action = gameInteractions.onStart),
                entryForLevel(level = Level(20), action = gameInteractions.onStart),
                SpacerMenuEntry,
                DefaultMenuEntry(
                    title = stringResource(Res.string.settings_back),
                    action = gameInteractions.onBack,
                    iconResId = Res.drawable.ic_back
                )
            )
        )
    }
}

@Composable
private fun entryForLevel(level: Level, action: (level: Level) -> Unit): DefaultMenuEntry {
    return DefaultMenuEntry(
        title = stringResource(Res.string.select_level, getLevelSymbols(level = level)),
        action = { action(level) },
        color = MaterialTheme.colorScheme.onBackground
    )
}
