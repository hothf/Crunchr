<p align="center">
  <img src="screenshots/ic_launcher_round.png" width="120" alt="Crunchr icon"/>
</p>

<h1 align="center">Crunchr</h1>

<p align="center">
  A fast-paced math puzzle game — solve arithmetic problems before time runs out.
  <br/>
  Built with Kotlin Multiplatform and Compose Multiplatform.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-2.3.0-7F52FF?logo=kotlin" alt="Kotlin"/>
  <img src="https://img.shields.io/badge/Compose_Multiplatform-1.10.3-4285F4?logo=jetpackcompose" alt="Compose Multiplatform"/>
  <img src="https://img.shields.io/badge/Platforms-Android_%7C_Desktop-green" alt="Platforms"/>
</p>

---

## How It Works

The game presents a series of math problems called **crunches** — arithmetic expressions like `7 + 3`, `12 * -5`, or `9 / 4`. You type the answer and submit before the timer runs out.

- **Score points** for each correct answer. Faster answers earn more points.
- **Streaks** build up for consecutive correct answers, increasing your score multiplier.
- **Levels** increase as you score higher, introducing harder operations (subtraction, multiplication, division) and negative numbers.
- **Game over** when the global timer runs out. Correct answers add time; wrong answers don't.

## Building

```bash
# Android
./gradlew assembleDebug

# Desktop
./gradlew :app:run

# All tests
./gradlew test
```

**Requirements:** JDK 11+, Android SDK 36 (for Android builds).

## Project Structure

```
crunchr/
  app/                              # Compose Multiplatform application
    src/
      commonMain/kotlin/            # Shared UI, ViewModel, domain, theme
      commonMain/composeResources/  # Shared strings, drawables, fonts
      androidMain/kotlin/           # Android entry point + platform implementations
      desktopMain/kotlin/           # Desktop entry point
  crunchrgame/                      # KMP game engine library
    src/
      commonMain/kotlin/            # Game logic, rules, scoring, timers
      commonTest/kotlin/            # Tests (kotlin.test)
```

The **game engine** (`:crunchrgame`) is a pure Kotlin library with no UI dependencies. It communicates outward via callback lambdas, making it fully platform-agnostic.

The **app** (`:app`) contains all Compose UI, domain interfaces, and platform-specific implementations. Platform abstractions (back handling, sound, vibration, persistence) use `expect`/`actual` declarations or stub interfaces.

## Tech Stack

| Component | Technology |
|---|---|
| Language | Kotlin 2.3.0 (K2 compiler) |
| UI | Compose Multiplatform 1.10.3 |
| Architecture | Single-Activity, state-driven navigation, manual DI |
| Persistence | DataStore Preferences (Android) |
| Build | Gradle 8.14.4, AGP 8.13.2, Version Catalog |
| Targets | Android (minSdk 24), Desktop (JVM) |

## License

All rights reserved.
