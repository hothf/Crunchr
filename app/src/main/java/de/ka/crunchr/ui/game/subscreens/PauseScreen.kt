package de.ka.crunchr.ui.game.subscreens

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import de.ka.crunchr.R
import de.ka.crunchr.ui.composables.AnimatedContainer
import de.ka.crunchr.ui.composables.DefaultMenuEntry
import de.ka.crunchr.ui.composables.Menu
import de.ka.crunchr.ui.composables.MenuRow
import de.ka.crunchr.ui.composables.MenuRows
import de.ka.crunchr.ui.composables.SpacerMenuEntry
import de.ka.crunchr.ui.game.GameInteractions
import de.ka.crunchr.ui.game.SettingsInteractions
import de.ka.crunchr.ui.game.getLevelSymbols
import de.ka.crunchr.ui.game.msToTime
import de.ka.crunchr.ui.theme.CrunchrTheme
import de.ka.crunchrgame.models.Score

@Composable
fun PauseScreen(
    isVisible: Boolean = false,
    score: Score?,
    gameInteractions: GameInteractions = GameInteractions(),
    settingsInteractions: SettingsInteractions = SettingsInteractions()
) {
    AnimatedContainer(isVisible = isVisible) {
        Menu(
            menuTitle = stringResource(id = R.string.paused_title), menuEntries = listOf(
                DefaultMenuEntry(
                    title = stringResource(id = R.string.paused_resume),
                    action = gameInteractions.onResume,
                    color = MaterialTheme.colorScheme.onBackground,
                    iconResId = R.drawable.ic_play
                ),
                SpacerMenuEntry,
                DefaultMenuEntry(
                    title = stringResource(id = R.string.paused_settings),
                    action = { settingsInteractions.onOpenSettings(true) },
                    iconResId = R.drawable.ic_settings
                ),
                SpacerMenuEntry,
                DefaultMenuEntry(
                    title = stringResource(id = R.string.paused_restart),
                    action = { gameInteractions.onStart(null) }
                ),
                DefaultMenuEntry(
                    title = stringResource(id = R.string.paused_forfeit),
                    action = gameInteractions.onForfeit
                ),
                SpacerMenuEntry,
                DefaultMenuEntry(
                    title = stringResource(id = R.string.paused_exit),
                    action = gameInteractions.onExit,
                    color = MaterialTheme.colorScheme.onError,
                    iconResId = R.drawable.ic_exit
                )
            ),
            menuHint = {
                if (score != null) {
                    MenuRows(
                        rows = listOf(
                            MenuRow(
                                description = stringResource(id = R.string.highscore_score),
                                value = score.score.toString()
                            ),
                            MenuRow(
                                description = stringResource(id = R.string.highscore_level),
                                value = getLevelSymbols(score.level)
                            )
                        )
                    )
                }
            }
        )
    }
}

@Preview
@Composable
private fun PreviewPauseScreen() {
    CrunchrTheme {
        PauseScreen(isVisible = true, score = Score.preview())
    }
}