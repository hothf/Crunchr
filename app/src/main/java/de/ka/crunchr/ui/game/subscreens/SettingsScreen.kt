package de.ka.crunchr.ui.game.subscreens

import androidx.compose.foundation.layout.fillMaxWidth
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
import de.ka.crunchr.ui.composables.SpacerMenuEntry
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
            menuTitle = stringResource(id = R.string.settings_title), menuEntries = listOf(
                SpacerMenuEntry,
                DefaultMenuEntry(
                    title = stringResource(id = R.string.settings_back),
                    action = { settingsInteractions.onOpenSettings(false) }
                )
            ),
            middleContent = {
                ActionCheckBox(
                    modifier = Modifier.fillMaxWidth(),
                    awaitOnTap = true,
                    checked = appSettings.isVibrationEnabled,
                    text = stringResource(id = R.string.settings_vibration),
                    onTap = {
                        settingsInteractions.onSettingsChanged(true, false)
                    })
                ActionCheckBox(
                    modifier = Modifier.fillMaxWidth(),
                    awaitOnTap = true,
                    checked = appSettings.isSoundEnabled,
                    text = stringResource(id = R.string.settings_sound),
                    onTap = {
                        settingsInteractions.onSettingsChanged(false, true)
                    })
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