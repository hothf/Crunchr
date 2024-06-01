package de.ka.crunchrgame

/**
 * The game stane contains all [CrunchrGame] relevant game variables.
 */
data class GameState(
     val gameOverTimeMs: Long = 30_000,
     val gameOverElapsedTimeMs: Long = 0,
     val currentCrunchElapsedTimeMs: Long = 0,
     val currentLevel: Level = Level(),
     val currentCrunch: Crunch? = null,
     val currentScore: Long = 0L,
     val currentSolvedCrunchCount: Int = 0,
     val status: GameStatus = GameStatus.NOT_STARTED,
)
