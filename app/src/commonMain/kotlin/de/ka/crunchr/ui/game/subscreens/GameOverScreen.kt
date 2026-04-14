package de.ka.crunchr.ui.game.subscreens

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import de.ka.crunchr.domain.HighScore
import de.ka.crunchr.generated.Res
import de.ka.crunchr.generated.game_over_best
import de.ka.crunchr.generated.game_over_exit
import de.ka.crunchr.generated.game_over_start
import de.ka.crunchr.generated.game_over_title
import de.ka.crunchr.generated.highscore_average
import de.ka.crunchr.generated.highscore_level
import de.ka.crunchr.generated.highscore_score
import de.ka.crunchr.generated.highscore_solved
import de.ka.crunchr.generated.highscore_streak
import de.ka.crunchr.generated.highscore_time
import de.ka.crunchr.ui.composables.AnimatedContainer
import de.ka.crunchr.ui.composables.DefaultMenuEntry
import de.ka.crunchr.ui.composables.Menu
import de.ka.crunchr.ui.composables.MenuRow
import de.ka.crunchr.ui.composables.MenuRows
import de.ka.crunchr.ui.composables.SpacerMenuEntry
import de.ka.crunchr.ui.game.GameInteractions
import de.ka.crunchr.ui.game.getLevelSymbols
import de.ka.crunchr.ui.game.msToTime
import de.ka.crunchrgame.models.Score
import org.jetbrains.compose.resources.stringResource

@Composable
fun GameOverScreen(
    isVisible: Boolean = false,
    score: Score?,
    highScore: HighScore? = null,
    gameInteractions: GameInteractions = GameInteractions()
) {
    AnimatedContainer(isVisible = isVisible) {
        Menu(
            menuTitle = stringResource(Res.string.game_over_title), menuEntries = listOf(
                DefaultMenuEntry(
                    title = stringResource(Res.string.game_over_start),
                    action = gameInteractions.onQuit,
                    color = MaterialTheme.colorScheme.onBackground
                ),
                SpacerMenuEntry,
                DefaultMenuEntry(
                    title = stringResource(Res.string.game_over_exit),
                    action = gameInteractions.onExit
                )
            ),
            isVisible = isVisible,
            padTitle = true,
            menuHint = {
                if (score != null) {
                    val best = stringResource(Res.string.game_over_best)
                    MenuRows(
                        rows = listOf(
                            MenuRow(
                                description = stringResource(Res.string.highscore_score),
                                value = score.score.toString(),
                                isSpecial = if (highScore?.score != null) best else null
                            ),
                            MenuRow(
                                description = stringResource(Res.string.highscore_level),
                                value = getLevelSymbols(level = score.level),
                                isSpecial = if (highScore?.level != null) best else null
                            ),
                            MenuRow(
                                description = stringResource(Res.string.highscore_solved),
                                value = "${score.crunchCount}/${score.overallCrunchCount}",
                                isSpecial = if (highScore?.solveCount != null) best else null
                            ),
                            MenuRow(
                                description = stringResource(Res.string.highscore_time),
                                value = score.elapsedTimeMs.msToTime(),
                                isSpecial = if (highScore?.time != null) best else null
                            ),
                            MenuRow(
                                description = stringResource(Res.string.highscore_average),
                                value = score.getAverageSolvingTimeMs().msToTime(),
                                isSpecial = if (highScore?.averageTimeMs != null) best else null
                            ),
                            MenuRow(
                                description = stringResource(Res.string.highscore_streak),
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
