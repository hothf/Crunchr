package de.ka.crunchr.ui.game.subscreens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import de.ka.crunchr.domain.Settings
import de.ka.crunchr.generated.Res
import de.ka.crunchr.generated.ic_back
import de.ka.crunchr.generated.settings_back
import de.ka.crunchr.generated.settings_sound
import de.ka.crunchr.generated.settings_title
import de.ka.crunchr.generated.settings_vibration
import de.ka.crunchr.ui.composables.ActionCheckBox
import de.ka.crunchr.ui.composables.AnimatedContainer
import de.ka.crunchr.ui.composables.DefaultMenuEntry
import de.ka.crunchr.ui.composables.Menu
import de.ka.crunchr.ui.composables.SlidingInContent
import de.ka.crunchr.ui.composables.SpacerMenuEntry
import de.ka.crunchr.ui.composables.utils.UiDefaults
import de.ka.crunchr.ui.game.SettingsInteractions
import org.jetbrains.compose.resources.stringResource

@Composable
fun SettingsScreen(
    isVisible: Boolean = false,
    appSettings: Settings,
    settingsInteractions: SettingsInteractions = SettingsInteractions()
) {
    AnimatedContainer(isVisible = isVisible) {
        Menu(
            isVisible = isVisible,
            menuTitle = stringResource(Res.string.settings_title), menuEntries = listOf(
                SpacerMenuEntry,
                DefaultMenuEntry(
                    title = stringResource(Res.string.settings_back),
                    action = { settingsInteractions.onOpenSettings(false) },
                    iconResId = Res.drawable.ic_back
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
                        text = stringResource(Res.string.settings_vibration),
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
                        text = stringResource(Res.string.settings_sound),
                        onTap = {
                            settingsInteractions.onSettingsChanged(false, true)
                        })
                }
                2
            }
        )
    }
}
