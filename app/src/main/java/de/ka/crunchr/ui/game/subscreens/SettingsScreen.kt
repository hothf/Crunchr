package de.ka.crunchr.ui.game.subscreens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import de.ka.crunchr.R
import de.ka.crunchr.domain.Settings
import de.ka.crunchr.ui.composables.ActionCheckBox
import de.ka.crunchr.ui.composables.AnimatedContainer
import de.ka.crunchr.ui.composables.DefaultMenuEntry
import de.ka.crunchr.ui.composables.Menu
import de.ka.crunchr.ui.composables.SlidingInContent
import de.ka.crunchr.ui.composables.SpacerMenuEntry
import de.ka.crunchr.ui.composables.utils.UiDefaults
import de.ka.crunchr.ui.game.SettingsInteractions
import de.ka.crunchr.ui.theme.CrunchrTheme

@Composable
fun SettingsScreen(
    isVisible: Boolean = false,
    appSettings: Settings,
    settingsInteractions: SettingsInteractions = SettingsInteractions()
) {
    AnimatedContainer(isVisible = isVisible) {
        Menu(
            isVisible = isVisible,
            menuTitle = stringResource(id = R.string.settings_title), menuEntries = listOf(
                SpacerMenuEntry,
                DefaultMenuEntry(
                    title = stringResource(id = R.string.settings_back),
                    action = { settingsInteractions.onOpenSettings(false) },
                    iconResId = R.drawable.ic_back
                )
            ),
            middleContent = {
                SlidingInContent(
                    isVisible = isVisible,
                    index = 0
                ) {
                    ActionCheckBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(UiDefaults.smallPadding),
                        awaitOnTap = true,
                        checked = appSettings.isVibrationEnabled,
                        tintColor = MaterialTheme.colorScheme.onSecondary,
                        text = stringResource(id = R.string.settings_vibration),
                        onTap = {
                            settingsInteractions.onSettingsChanged(true, false)
                        })
                }
                SlidingInContent(
                    isVisible = isVisible,
                    index = 1
                ) {
                    ActionCheckBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(UiDefaults.smallPadding),
                        awaitOnTap = true,
                        checked = appSettings.isSoundEnabled,
                        tintColor = MaterialTheme.colorScheme.onSecondary,
                        text = stringResource(id = R.string.settings_sound),
                        onTap = {
                            settingsInteractions.onSettingsChanged(false, true)
                        })
                }
                2
            }
        )
    }
}

@Composable
@Preview
fun PreviewSettingsScreen() {
    CrunchrTheme {
        SettingsScreen(
            isVisible = true,
            appSettings = Settings()
        )
    }
}