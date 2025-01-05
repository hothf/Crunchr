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
import de.ka.crunchr.ui.game.getLevelSymbols
import de.ka.crunchr.ui.theme.CrunchrTheme
import de.ka.crunchrgame.models.Level

@Composable
fun LevelSelectScreen(
    isVisible: Boolean = false,
    gameInteractions: GameInteractions = GameInteractions(),
) {
    AnimatedContainer(isVisible = isVisible) {
        Menu(
            isVisible = isVisible,
            menuTitle = stringResource(id = R.string.select_level_title),
            menuEntries = listOf(
                entryForLevel(level = Level(1), action = gameInteractions.onStart),
                entryForLevel(level = Level(10), action = gameInteractions.onStart),
                entryForLevel(level = Level(20), action = gameInteractions.onStart),
                SpacerMenuEntry,
                DefaultMenuEntry(
                    title = stringResource(id = R.string.settings_back),
                    action = gameInteractions.onBack,
                    iconResId = R.drawable.ic_back
                )
            )
        )
    }
}

@Composable
private fun entryForLevel(level: Level, action: (level: Level) -> Unit): DefaultMenuEntry {
    return DefaultMenuEntry(
        title = stringResource(id = R.string.select_level, getLevelSymbols(level = level)),
        action = { action(level) },
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
@Preview
fun PreviewLevelSelectScreen() {
    CrunchrTheme {
        LevelSelectScreen(isVisible = true)
    }
}