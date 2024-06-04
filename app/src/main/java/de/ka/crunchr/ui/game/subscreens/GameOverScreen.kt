package de.ka.crunchr.ui.game.subscreens

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import de.ka.crunchr.R
import de.ka.crunchr.domain.HighScore
import de.ka.crunchr.ui.composables.AnimatedContainer
import de.ka.crunchr.ui.composables.DefaultMenuEntry
import de.ka.crunchr.ui.composables.Menu
import de.ka.crunchr.ui.composables.MenuRow
import de.ka.crunchr.ui.composables.MenuRows
import de.ka.crunchr.ui.composables.SpacerMenuEntry
import de.ka.crunchr.ui.game.GameInteractions
import de.ka.crunchr.ui.game.getLevelSymbols
import de.ka.crunchr.ui.game.msToTime
import de.ka.crunchr.ui.theme.CrunchrTheme
import de.ka.crunchrgame.models.Score

@Composable
fun GameOverScreen(
    isVisible: Boolean = false,
    score: Score?,
    highScore: HighScore? = null,
    gameInteractions: GameInteractions = GameInteractions()
) {
    AnimatedContainer(isVisible = isVisible) {
        Menu(
            menuTitle = stringResource(id = R.string.game_over_title), menuEntries = listOf(
                DefaultMenuEntry(
                    title = stringResource(id = R.string.game_over_start),
                    action = gameInteractions.onQuit,
                ),
                SpacerMenuEntry,
                DefaultMenuEntry(
                    title = stringResource(id = R.string.game_over_exit),
                    action = gameInteractions.onExit,
                    color = MaterialTheme.colorScheme.onError
                )
            ),
            menuHint = {
                if (score != null) {
                    val best = stringResource(id = R.string.game_over_best)
                    MenuRows(
                        rows = listOf(
                            MenuRow(
                                description = stringResource(id = R.string.highscore_score),
                                value = score.score.toString(),
                                isSpecial = if (highScore?.score != null) best else null
                            ),
                            MenuRow(
                                description = stringResource(id = R.string.highscore_level),
                                value = getLevelSymbols(level = score.level),
                                isSpecial = if (highScore?.level != null) best else null
                            ),
                            MenuRow(
                                description = stringResource(id = R.string.highscore_solved),
                                value = "${score.crunchCount}/${score.overallCrunchCount}",
                                isSpecial = if (highScore?.solveCount != null) best else null
                            ),
                            MenuRow(
                                description = stringResource(id = R.string.highscore_time),
                                value = score.elapsedTimeMs.msToTime(),
                                isSpecial = if (highScore?.time != null) best else null
                            ),
                            MenuRow(
                                description = stringResource(id = R.string.highscore_average),
                                value = score.getAverageSolvingTimeMs().msToTime(),
                                isSpecial = if (highScore?.averageTimeMs != null) best else null
                            ),
                            MenuRow(
                                description = stringResource(id = R.string.highscore_streak),
                                value = score.bestStreakCount.toString(),
                                isSpecial = if (highScore?.streakCount != null) best else null
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
fun PreviewGameOverScreen() {
    CrunchrTheme {
        GameOverScreen(isVisible = true, score = Score.preview())
    }
}