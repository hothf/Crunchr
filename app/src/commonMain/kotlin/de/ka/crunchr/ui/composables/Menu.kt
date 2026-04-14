package de.ka.crunchr.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import de.ka.crunchr.ui.composables.utils.UiDefaults
import de.ka.crunchr.ui.composables.utils.innerShadow
import org.jetbrains.compose.resources.DrawableResource

@Composable
fun Menu(
    isVisible: Boolean,
    menuTitle: String,
    padTitle: Boolean = false,
    menuHint: @Composable ColumnScope.() -> Unit = {},
    middleContent: @Composable ColumnScope.() -> Int = { 0 },
    menuEntries: List<MenuEntry>
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null,
                onClick = {}),
        contentAlignment = Alignment.Center
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val isLandscape = maxWidth > maxHeight
            if (isLandscape) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    ButtonsMenuContent(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
                            .weight(UiDefaults.END_PERCENTAGE),
                        menuEntries = menuEntries,
                        middleContent = middleContent,
                        isHorizontal = true,
                        isVisible = isVisible
                    )
                    DisplayMenuContent(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(end = UiDefaults.bigPaddings)
                            .clip(RoundedCornerShape(UiDefaults.defaultCorners))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .innerShadow(
                                blur = UiDefaults.blurRadius,
                                hideSides = false,
                                color = MaterialTheme.colorScheme.tertiary,
                                cornersRadius = UiDefaults.defaultCorners
                            )
                            .weight(UiDefaults.START_PERCENTAGE),
                        menuTitle = menuTitle,
                        menuHint = menuHint,
                        hasEntries = padTitle
                    )
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Center
                ) {
                    DisplayMenuContent(
                        modifier = Modifier
                            .padding(top = UiDefaults.bigPaddings)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(UiDefaults.defaultCorners))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .innerShadow(
                                blur = UiDefaults.blurRadius,
                                hideSides = false,
                                color = MaterialTheme.colorScheme.tertiary,
                                cornersRadius = UiDefaults.defaultCorners
                            )
                            .weight(UiDefaults.TOP_PERCENTAGE),
                        menuTitle = menuTitle,
                        hasEntries = padTitle,
                        menuHint = menuHint
                    )
                    ButtonsMenuContent(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(UiDefaults.BOTTOM_PERCENTAGE)
                            .clip(RoundedCornerShape(UiDefaults.defaultCorners)),
                        menuEntries = menuEntries,
                        middleContent = middleContent,
                        isVisible = isVisible
                    )
                }
            }
        }

    }
}

@Composable
private fun DisplayMenuContent(
    modifier: Modifier,
    menuTitle: String,
    hasEntries: Boolean,
    menuHint: @Composable ColumnScope.() -> Unit = {},
) {
    Column(
        modifier = modifier.padding(UiDefaults.defaultPaddings),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(UiDefaults.bigPaddings),
            text = menuTitle,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSecondary
        )
        menuHint()
    }
}

@Composable
private fun ButtonsMenuContent(
    modifier: Modifier,
    middleContent: @Composable ColumnScope.() -> Int = { 0 },
    menuEntries: List<MenuEntry>,
    isHorizontal: Boolean = false,
    isVisible: Boolean
) {
    Column(
        modifier = modifier
            .padding(
                top = if (isHorizontal) 0.dp else UiDefaults.buttonSize + UiDefaults.defaultPaddings + UiDefaults.defaultPaddings,
                bottom = if (isHorizontal) 0.dp else UiDefaults.defaultPaddings + UiDefaults.smallButtonSize + UiDefaults.defaultPaddings,
                start = if (isHorizontal) UiDefaults.defaultPaddings + UiDefaults.smallButtonSize + UiDefaults.defaultPaddings else 0.dp,
                end = if (isHorizontal) UiDefaults.defaultPaddings + UiDefaults.smallButtonSize + UiDefaults.defaultPaddings else 0.dp,
            )
            .fillMaxWidth()
            .clip(RoundedCornerShape(UiDefaults.defaultCorners))
            .background(MaterialTheme.colorScheme.primary)
            .innerShadow(
                blur = UiDefaults.blurRadius,
                hideSides = false,
                cornersRadius = UiDefaults.defaultCorners
            )
            .verticalScroll(rememberScrollState())
            .padding(vertical = UiDefaults.bigPaddings),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(0.75f),
            verticalArrangement = Arrangement.spacedBy(UiDefaults.defaultPaddings)
        ) {
            val size = middleContent()
            menuEntries.forEachIndexed { index, action ->
                if (action is SpacerMenuEntry) {
                    Spacer(modifier = Modifier.size(UiDefaults.defaultSpacer))
                } else if (action is DefaultMenuEntry) {
                    SlidingInContent(index = size + index, isVisible = isVisible) {
                        ActionButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = UiDefaults.smallPadding)
                                .height(UiDefaults.buttonSize),
                            foregroundColor = if (action.color != null) MaterialTheme.colorScheme.secondary.copy(
                                alpha = 0.75f
                            ) else null,
                            tintColor = if (action.color != null) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.secondary,
                            awaitOnTap = true,
                            iconResId = action.iconResId,
                            onTap = action.action,
                            text = action.title,
                            cornerRadius = UiDefaults.smallIconSize
                        )
                    }
                }
            }
        }
    }
}

sealed interface MenuEntry

data class DefaultMenuEntry(
    val title: String = "",
    val color: Color? = null,
    val action: () -> Unit = {},
    val iconResId: DrawableResource? = null
) : MenuEntry

data object SpacerMenuEntry : MenuEntry


@Composable
fun MenuRows(rows: List<MenuRow>) {
    rows.forEach { (desc, text, special) ->
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier
                    .weight(0.3f)
                    .padding(end = UiDefaults.smallPadding),
                textAlign = TextAlign.End,
                text = text,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.bodySmall
            )
            if (special?.isNotBlank() == true) {
                Text(
                    modifier = Modifier.weight(0.35f),
                    text = special,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSecondary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Text(
                modifier = Modifier
                    .weight(0.35f)
                    .padding(start = UiDefaults.smallPadding),
                text = desc,
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.onSecondary,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

data class MenuRow(val value: String, val description: String, val isSpecial: String? = null)
