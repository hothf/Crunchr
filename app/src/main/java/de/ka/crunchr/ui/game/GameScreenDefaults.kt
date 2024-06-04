package de.ka.crunchr.ui.game

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import de.ka.crunchr.R
import de.ka.crunchr.domain.AppGameSaver
import de.ka.crunchr.domain.AppSettingsSaver
import de.ka.crunchr.domain.HighScoreSaver
import de.ka.crunchr.domain.Sound
import de.ka.crunchr.domain.StringResolver
import de.ka.crunchr.domain.StubAppGameSaver
import de.ka.crunchr.domain.StubAppSettingsSaver
import de.ka.crunchr.domain.StubHighScoreSaver
import de.ka.crunchr.domain.StubSound
import de.ka.crunchr.domain.StubStringResolver
import de.ka.crunchr.domain.StubVibrator
import de.ka.crunchr.domain.Vibrator
import de.ka.crunchr.ui.composables.ColorUpdateHostState
import de.ka.crunchr.ui.composables.ExpectedUpdateHostState
import de.ka.crunchr.ui.composables.SolvingResult
import de.ka.crunchr.ui.composables.SolvingResultUpdateHostState
import de.ka.crunchr.ui.composables.TimerHostState
import de.ka.crunchr.ui.composables.utils.ScoreDisplayUtils.getDisplayableFloat
import de.ka.crunchrgame.models.crunch.Result
import de.ka.crunchrgame.models.Level

data class GameInteractions(
    val onStart: (level: Level?) -> Unit = {},
    val onResume: () -> Unit = {},
    val onPause: () -> Unit = {},
    val onForfeit: () -> Unit = {},
    val onSolve: () -> Unit = {},
    val input: (String) -> Unit = {},
    val onQuit: () -> Unit = {},
    val onExit: () -> Unit = {},
    val clear: () -> Unit = {},
    val onBack: () -> Unit = {},
)

data class SettingsInteractions(
    val onOpenLevelSelect: () -> Unit = {},
    val onOpenSettings: (Boolean) -> Unit = {},
    val onSettingsChanged: (vibration: Boolean, sound: Boolean) -> Unit = { _, _ -> }
)

data class GameDependencies(
    val gameSaver: () -> AppGameSaver = { StubAppGameSaver() },
    val settingsSaver: () -> AppSettingsSaver = { StubAppSettingsSaver() },
    val highScoreSaver: () -> HighScoreSaver = { StubHighScoreSaver() },
    val vibrator: Vibrator = StubVibrator(),
    val sound: Sound = StubSound(),
    val stringResolver: StringResolver = StubStringResolver()
)

data class GameHostStates(
    val gameTimerHostState: TimerHostState = TimerHostState(),
    val crunchTimerHostState: TimerHostState = TimerHostState(),
    val scoreUpdateHostState: SolvingResultUpdateHostState = SolvingResultUpdateHostState(),
    val expectedUpdateHostState: ExpectedUpdateHostState = ExpectedUpdateHostState(),
    val colorUpdateHostState: ColorUpdateHostState = ColorUpdateHostState()
)

@Composable
fun defaultGameHostStates(): GameHostStates {
    return GameHostStates(
        gameTimerHostState = remember { TimerHostState() },
        crunchTimerHostState = remember { TimerHostState() },
        scoreUpdateHostState = remember { SolvingResultUpdateHostState() },
        expectedUpdateHostState = remember { ExpectedUpdateHostState() },
        colorUpdateHostState = remember { ColorUpdateHostState() }
    )
}

fun Result.toSolvingResult(stringResolver: StringResolver): SolvingResult {
    val streak = if (this.streakCount > 0) stringResolver.string(
        R.string.performance_streak,
        this.streakCount + 1
    ) else ""
    val multiplierDisplay =
        if (this.multiplier > 1f) stringResolver.string(
            R.string.performance_multiplier,
            this.multiplier.getDisplayableFloat() ?: ""
        ) else ""
    return SolvingResult(
        successful = successful,
        performance = if (streakCount > 0) streak else multiplierDisplay,
        score = stringResolver.string(
            if (successful) R.string.performance_points_pos else R.string.performance_points_neg,
            this.finalPoints
        ),
        expected = this.expected,
        was = this.actual,
        multiplier = multiplierDisplay
    )
}

/**
 * Formats the given time in milliseconds to a readable time string.
 */
@Composable
fun Long.msToTime(): String {
    val seconds = this / 1000
    if (seconds < 60) return stringResource(id = R.string.time_seconds, "$seconds")
    val modSeconds = seconds % 60
    val minutes = seconds / 60
    if (minutes < 60) return stringResource(id = R.string.time_minutes, "$minutes", "$modSeconds")
    val modMinutes = minutes % 60
    val hours = minutes / 60
    if (hours < 24) return stringResource(
        R.string.time_hours,
        "$hours",
        "$modMinutes",
        "$modSeconds"
    )
    val modHours = hours % 24
    val days = hours / 24
    return stringResource(
        id = R.string.time_days,
        "$days",
        "$modHours",
        "$modMinutes",
        "$modSeconds"
    )
}

@Composable
fun getLevelSymbols(level: Level): String {
    return stringResource(
        id = R.string.highscore_level_val,
        level.symbols.joinToString("") {
            it.stringRepresentation
        },
        "${level.value}"
    )
}