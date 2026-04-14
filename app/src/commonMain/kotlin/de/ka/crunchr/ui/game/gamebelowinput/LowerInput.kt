package de.ka.crunchr.ui.game.gamebelowinput

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import de.ka.crunchr.generated.Res
import de.ka.crunchr.generated.ic_done
import de.ka.crunchr.generated.ic_menu
import de.ka.crunchr.generated.ic_refresh
import de.ka.crunchr.generated.ic_skip
import de.ka.crunchr.ui.composables.ActionButton
import de.ka.crunchr.ui.composables.ButtonsTray
import de.ka.crunchr.ui.composables.utils.Corners
import de.ka.crunchr.ui.composables.utils.UiDefaults
import de.ka.crunchr.ui.composables.utils.innerShadow
import de.ka.crunchr.ui.game.GameInteractions

@Composable
fun LowerInput(
    modifier: Modifier = Modifier,
    gameInteractions: GameInteractions = GameInteractions(),
    buttonsEnabled: Boolean,
    isHorizontal: Boolean = false
) {
    if (isHorizontal) {
        LowerInputHorizontal(
            modifier = modifier,
            buttonsEnabled = buttonsEnabled,
            gameInteractions = gameInteractions
        )
    } else {
        LowerInputVertical(
            modifier = modifier,
            buttonsEnabled = buttonsEnabled,
            gameInteractions = gameInteractions
        )
    }
}

@Composable
private fun LowerInputVertical(
    modifier: Modifier,
    buttonsEnabled: Boolean = true,
    gameInteractions: GameInteractions
) {
    Column(
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.primary)
                .padding(top = UiDefaults.defaultPaddings, bottom = UiDefaults.defaultPaddings),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(UiDefaults.WIDTH_PERCENTAGE),
                horizontalArrangement = Arrangement.Center
            ) {
                ActionButton(
                    enabled = buttonsEnabled,
                    modifier = Modifier
                        .height(UiDefaults.buttonSize)
                        .padding(start = UiDefaults.smallPadding)
                        .weight(0.5f),
                    iconResId = Res.drawable.ic_refresh,
                    onTap = gameInteractions.clear,
                    tintColor = MaterialTheme.colorScheme.onPrimary,
                    awaitOnTap = true,
                    cornerRadius = UiDefaults.timerSmallSize
                )
                ActionButton(
                    enabled = buttonsEnabled,
                    modifier = Modifier
                        .height(UiDefaults.buttonSize)
                        .padding(start = UiDefaults.smallPadding, end = UiDefaults.smallPadding)
                        .weight(1f),
                    iconResId = Res.drawable.ic_done,
                    onTap = gameInteractions.onSolve,
                    foregroundColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.75f),
                    color = MaterialTheme.colorScheme.primary,
                    tintColor = MaterialTheme.colorScheme.onSecondary,
                    awaitOnTap = true,
                    cornerRadius = UiDefaults.timerSmallSize
                )
                ActionButton(
                    enabled = buttonsEnabled,
                    modifier = Modifier
                        .height(UiDefaults.buttonSize)
                        .padding(end = UiDefaults.smallPadding)
                        .weight(0.5f),
                    iconResId = Res.drawable.ic_skip,
                    onTap = {
                        gameInteractions.onResume(true)
                    },
                    tintColor = MaterialTheme.colorScheme.onError,
                    awaitOnTap = true,
                    cornerRadius = UiDefaults.timerSmallSize
                )
            }
        }
        ButtonsTray(
            buttonsEnabled = buttonsEnabled,
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(bottomStart = UiDefaults.defaultCorners, bottomEnd = UiDefaults.defaultCorners))
                .background(MaterialTheme.colorScheme.primary)
                .innerShadow(
                    blur = UiDefaults.blurRadius,
                    hideSides = false,
                    corners = Corners(bottomLeft = true, bottomRight = true),
                    cornersRadius = UiDefaults.defaultCorners)
                .padding(vertical = UiDefaults.bigPaddings),
            input = gameInteractions.input
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.primary)
                .padding(UiDefaults.defaultPaddings),
            contentAlignment = Alignment.Center
        ) {
            ActionButton(
                enabled = buttonsEnabled,
                color = MaterialTheme.colorScheme.primary,
                awaitOnTap = true,
                modifier = Modifier.size(UiDefaults.smallButtonSize),
                iconResId = Res.drawable.ic_menu,
                onTap = gameInteractions.onPause,
                cornerRadius = UiDefaults.timerSmallSize
            )
        }
    }
}

@Composable
private fun LowerInputHorizontal(
    modifier: Modifier,
    gameInteractions: GameInteractions,
    buttonsEnabled: Boolean,
) {
    Row(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .background(color = MaterialTheme.colorScheme.primary)
                .padding(start = UiDefaults.defaultPaddings, end = UiDefaults.defaultPaddings), contentAlignment = Alignment.Center
        ) {
            ActionButton(
                enabled = buttonsEnabled,
                color = MaterialTheme.colorScheme.primary,
                awaitOnTap = true,
                modifier = Modifier.size(UiDefaults.smallButtonSize),
                iconResId = Res.drawable.ic_menu,
                onTap = gameInteractions.onPause,
                cornerRadius = UiDefaults.timerSmallSize
            )
        }
        ButtonsTray(
            buttonsEnabled = buttonsEnabled,
            modifier = Modifier
                .weight(1f)
                .background(MaterialTheme.colorScheme.primary)
                .innerShadow(
                    blur = UiDefaults.blurRadius,
                    hideSides = false,
                    corners = Corners(bottomLeft = true, topLeft = true),
                    cornersRadius = UiDefaults.defaultCorners)
                .padding(vertical = UiDefaults.bigPaddings), input = gameInteractions.input
        )
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .background(color = MaterialTheme.colorScheme.primary)
                .padding(start = UiDefaults.defaultPaddings, end = UiDefaults.defaultPaddings),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxHeight(UiDefaults.WIDTH_PERCENTAGE),
                verticalArrangement = Arrangement.Center
            ) {
                ActionButton(
                    enabled = buttonsEnabled,
                    modifier = Modifier
                        .height(UiDefaults.buttonSize)
                        .padding(top = UiDefaults.smallPadding)
                        .weight(0.5f),
                    iconResId = Res.drawable.ic_refresh,
                    onTap = gameInteractions.clear,
                    tintColor = MaterialTheme.colorScheme.onPrimary,
                    awaitOnTap = true,
                    cornerRadius = UiDefaults.timerSmallSize
                )
                ActionButton(
                    enabled = buttonsEnabled,
                    modifier = Modifier
                        .height(UiDefaults.buttonSize)
                        .padding(top = UiDefaults.smallPadding, bottom = UiDefaults.smallPadding)
                        .weight(1f),
                    iconResId = Res.drawable.ic_done,
                    onTap = gameInteractions.onSolve,
                    foregroundColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.75f),
                    color = MaterialTheme.colorScheme.primary,
                    tintColor = MaterialTheme.colorScheme.onSecondary,
                    awaitOnTap = true,
                    cornerRadius = UiDefaults.timerSmallSize
                )
                ActionButton(
                    enabled = buttonsEnabled,
                    modifier = Modifier
                        .height(UiDefaults.buttonSize)
                        .padding(bottom = UiDefaults.smallPadding)
                        .weight(0.5f),
                    iconResId = Res.drawable.ic_skip,
                    onTap = {
                        gameInteractions.onResume(true)
                    },
                    tintColor = MaterialTheme.colorScheme.onError,
                    awaitOnTap = true,
                    cornerRadius = UiDefaults.timerSmallSize
                )
            }
        }
    }
}
