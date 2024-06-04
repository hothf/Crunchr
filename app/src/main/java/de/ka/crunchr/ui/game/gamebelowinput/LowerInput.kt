package de.ka.crunchr.ui.game.gamebelowinput

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import de.ka.crunchr.R
import de.ka.crunchr.ui.composables.ActionButton
import de.ka.crunchr.ui.composables.ButtonsTray
import de.ka.crunchr.ui.composables.utils.UiDefaults
import de.ka.crunchr.ui.composables.utils.innerShadow
import de.ka.crunchr.ui.game.GameInteractions
import de.ka.crunchr.ui.theme.CrunchrTheme

@Composable
fun LowerInput(
    modifier: Modifier = Modifier,
    gameInteractions: GameInteractions = GameInteractions(),
    isHorizontal: Boolean = false
) {
    if (isHorizontal) {
        LowerInputHorizontal(modifier = modifier, gameInteractions = gameInteractions)
    } else {
        LowerInputVertical(modifier = modifier, gameInteractions = gameInteractions)
    }
}

@Composable
private fun LowerInputVertical(
    modifier: Modifier,
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
                    modifier = Modifier
                        .height(UiDefaults.buttonSize)
                        .padding(start = UiDefaults.smallPadding)
                        .weight(0.5f),
                    iconResId = R.drawable.ic_refresh,
                    onTap = gameInteractions.clear,
                    tintColor = MaterialTheme.colorScheme.onPrimary,
                    awaitOnTap = true
                )
                ActionButton(
                    modifier = Modifier
                        .height(UiDefaults.buttonSize)
                        .padding(start = UiDefaults.smallPadding, end = UiDefaults.smallPadding)
                        .weight(1f),
                    iconResId = R.drawable.ic_done,
                    onTap = gameInteractions.onSolve,
                    color = MaterialTheme.colorScheme.onBackground,
                    tintColor = MaterialTheme.colorScheme.primary,
                    awaitOnTap = true
                )
                ActionButton(
                    modifier = Modifier
                        .height(UiDefaults.buttonSize)
                        .padding(end = UiDefaults.smallPadding)
                        .weight(0.5f),
                    iconResId = R.drawable.ic_skip,
                    onTap = gameInteractions.onResume,
                    tintColor = MaterialTheme.colorScheme.error,
                    awaitOnTap = true
                )
            }
        }
        ButtonsTray(
            modifier = Modifier
                .weight(1f)
                .background(MaterialTheme.colorScheme.primary)
                .innerShadow(blur = UiDefaults.blurRadius)
                .padding(vertical = UiDefaults.bigPaddings), input = gameInteractions.input
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.primary)
                .padding(UiDefaults.defaultPaddings), contentAlignment = Alignment.Center
        ) {
            ActionButton(
                color = MaterialTheme.colorScheme.primary,
                awaitOnTap = true,
                modifier = Modifier.size(UiDefaults.smallButtonSize),
                iconResId = R.drawable.ic_menu,
                onTap = gameInteractions.onPause
            )
        }
    }
}

@Composable
private fun LowerInputHorizontal(
    modifier: Modifier,
    gameInteractions: GameInteractions
) {
    Row(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .background(color = MaterialTheme.colorScheme.primary)
                .padding(UiDefaults.defaultPaddings), contentAlignment = Alignment.Center
        ) {
            ActionButton(
                color = MaterialTheme.colorScheme.primary,
                awaitOnTap = true,
                modifier = Modifier.size(UiDefaults.smallButtonSize),
                iconResId = R.drawable.ic_menu,
                onTap = gameInteractions.onPause
            )
        }




        ButtonsTray(
            modifier = Modifier
                .weight(1f)
                .background(MaterialTheme.colorScheme.primary)
                .innerShadow(blur = UiDefaults.blurRadius, hideSides = false)
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
                    modifier = Modifier
                        .height(UiDefaults.buttonSize)
                        .padding(top = UiDefaults.smallPadding)
                        .weight(0.5f),
                    iconResId = R.drawable.ic_refresh,
                    onTap = gameInteractions.clear,
                    tintColor = MaterialTheme.colorScheme.inversePrimary,
                    awaitOnTap = true
                )
                ActionButton(
                    modifier = Modifier
                        .height(UiDefaults.buttonSize)
                        .padding(vertical = UiDefaults.smallPadding)
                        .weight(1f),
                    iconResId = R.drawable.ic_done,
                    onTap = gameInteractions.onSolve,
                    color = MaterialTheme.colorScheme.onBackground,
                    tintColor = MaterialTheme.colorScheme.primary,
                    awaitOnTap = true
                )
                ActionButton(
                    modifier = Modifier
                        .height(UiDefaults.buttonSize)
                        .padding(top = UiDefaults.smallPadding)
                        .weight(0.5f),
                    iconResId = R.drawable.ic_skip,
                    onTap = gameInteractions.onResume,
                    tintColor = MaterialTheme.colorScheme.onError,
                    awaitOnTap = true
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewLowerInput() {
    CrunchrTheme {
        Column {
            LowerInput()
        }
    }
}

@Preview(widthDp = 800, heightDp = 450)
@Composable
fun PreviewLowerInputHorizontal() {
    CrunchrTheme {
        Column {
            LowerInput(isHorizontal = true)
        }
    }
}