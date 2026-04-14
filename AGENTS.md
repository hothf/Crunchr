# AGENTS.md

Crunchr is a math puzzle game for Android, written in Kotlin with Jetpack Compose. Two modules: `:app` (UI, domain, data) and `:crunchrgame` (platform-agnostic game engine library).

## Build & test commands

```bash
./gradlew assembleDebug                # build
./gradlew test                         # all unit tests (only verification step)
./gradlew :crunchrgame:allTests           # game engine tests on all KMP targets (meaningful coverage)
./gradlew :crunchrgame:testDebugUnitTest  # game engine tests only (Android target)
./gradlew :crunchrgame:desktopTest        # game engine tests only (Desktop/JVM target)
./gradlew :app:testDebugUnitTest          # app tests only (placeholder only)
./gradlew :crunchrgame:testDebugUnitTest --tests "de.ka.crunchrgame.RuleTest"  # single test class
```

No CI, no lint tools, no formatters, no pre-commit hooks are configured. `./gradlew test` is the only automated check.

## Module boundaries

- **`:crunchrgame`** (`de.ka.crunchrgame`) — Kotlin Multiplatform library (Android + Desktop/JVM targets). Pure game logic: coroutine-based timers, scoring, crunch generation. All sources in `src/commonMain/kotlin/`, tests in `src/commonTest/kotlin/` using `kotlin.test`. `Rules.kt` is an `internal object` — not accessible from `:app`.
- **`:app`** (`de.ka.crunchr`) — Compose Multiplatform application (Android + Desktop/JVM targets). Depends on `:crunchrgame`. Contains domain interfaces with `Stub*` implementations, `DataStore`-backed data layer, and Compose UI.
  - `src/commonMain/kotlin/` — Shared Compose UI, domain interfaces, ViewModel, theme. Uses CMP resources (`composeResources/`).
  - `src/androidMain/kotlin/` — Android entry point (`MainActivity`), platform implementations (`data/` — DataStore, MediaPlayer, Vibrator).
  - `src/desktopMain/kotlin/` — Desktop entry point (`Main.kt`).
- The game engine communicates outward via callback lambdas in `Listeners` and `Savers` data classes, not interfaces.

## Architecture — things you will get wrong without reading this

- **No DI framework.** Manual injection via `GameDependencies` data class (defined in `GameScreenDefaults.kt`), wired in `MainActivity.onCreate()`. Do not introduce Hilt/Dagger/Koin without being asked.
- **ViewModel instantiated directly** as `GameViewModel()` — not via `viewModels()` delegate or `ViewModelProvider`. Dependencies are set on `viewModel.dependencies` after construction.
- **State-driven navigation** via `GameScreenStatus` enum — no Navigation Component, no nav graph. Sub-screens render as overlays in a `Box`, toggled by `uiState.status.current`. Back navigation is manual via `BackHandler` + `consumeBack()`.
- **Every domain interface has a Stub** (`StubSound`, `StubVibrator`, `StubAppGameSaver`, etc.) used as defaults in `GameDependencies` and for Compose previews.
- **All persistence is DataStore Preferences** — no Room, no SQLite. Savers use lambda factories (`() -> AppGameSaver`) so DataStore instances are created lazily.
- **No custom Application class** — the default is used.
- **Single Activity** (`MainActivity`) with a splash screen via `androidx.core:core-splashscreen`.
- **Platform abstractions** use `expect`/`actual` (e.g., `PlatformBackHandler`). Orientation detection uses `BoxWithConstraints` (not `LocalConfiguration`).
- **Resources** use the CMP resource system (`composeResources/`). Access via generated `Res` object (`de.ka.crunchr.generated.Res`), not Android `R`.

## Key versions & constraints

- Kotlin `2.3.0`, Compose Multiplatform `1.10.3`, Compose compiler via `org.jetbrains.kotlin.plugin.compose` (no separate `kotlinCompilerExtensionVersion`)
- AGP `8.13.2`, Gradle `8.14.4`
- JVM target `11`, minSdk `24`, compileSdk/targetSdk `36`
- Version catalog: `gradle/libs.versions.toml`
- `kotlinOptions` is removed in Kotlin 2.3 — use top-level `kotlin { compilerOptions { } }` block instead.

## Conventions

- Kotlin DSL (`.kts`) for all Gradle files.
- All dependency versions managed via the version catalog — do not use hardcoded versions.
- Commit messages: imperative present tense, short summaries.
- No code generation (no kapt/ksp), no ProGuard minification enabled.
- `kotlin.code.style=official` (set in `gradle.properties`).
