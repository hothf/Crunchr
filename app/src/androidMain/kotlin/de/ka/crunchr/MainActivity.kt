package de.ka.crunchr

import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewModelScope
import de.ka.crunchr.data.AppGameSaverImpl
import de.ka.crunchr.data.AppSettingsSaverImpl
import de.ka.crunchr.data.HighScoreSaverImpl
import de.ka.crunchr.data.SoundImpl
import de.ka.crunchr.data.StringResolverImpl
import de.ka.crunchr.data.VibratorImpl
import de.ka.crunchr.ui.game.GameDependencies
import de.ka.crunchr.ui.game.GameScreen
import de.ka.crunchr.ui.game.GameViewModel
import de.ka.crunchr.ui.theme.CrunchrTheme
import kotlinx.coroutines.job
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = GameViewModel()
        viewModel.dependencies = GameDependencies(
            gameSaver = { AppGameSaverImpl(this) },
            settingsSaver = { AppSettingsSaverImpl(this) },
            highScoreSaver = { HighScoreSaverImpl(this) },
            vibrator = VibratorImpl(this),
            sound = SoundImpl(this),
            stringResolver = StringResolverImpl(this)
        )

        handleSplashScreen(viewModel)

        setContent {
            CrunchrTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.primary
                ) {
                    GameScreen(viewModel = viewModel) {
                        finish()
                    }
                }
            }
        }
    }

    private fun handleSplashScreen(viewModel: GameViewModel) {
        val screen = this@MainActivity.installSplashScreen()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            var isLoading = false
            screen.setKeepOnScreenCondition {
                if (isLoading) {
                    return@setKeepOnScreenCondition true
                }
                if (viewModel.state.value.status.current != GameViewModel.GameScreenStatus.NOT_LOADED) {
                    return@setKeepOnScreenCondition false
                }
                isLoading = true

                viewModel.loadLastGameAndSettings()

                viewModel.viewModelScope.launch {
                    viewModel.state.collect {
                        if (it.status.current != GameViewModel.GameScreenStatus.NOT_LOADED) {
                            this.coroutineContext.job.cancel()
                            isLoading = false
                            return@collect
                        }
                    }
                }
                true
            }
        } else {
            viewModel.loadLastGameAndSettings()
        }
    }

}
