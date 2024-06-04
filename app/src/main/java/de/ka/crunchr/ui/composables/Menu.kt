package de.ka.crunchr.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.tooling.preview.Preview
import de.ka.crunchr.ui.composables.utils.UiDefaults
import de.ka.crunchr.ui.composables.utils.innerShadow

@Composable
fun Menu(
    menuTitle: String,
    menuHint: @Composable ColumnScope.() -> Unit = {},
    middleContent: @Composable ColumnScope.() -> Unit = {},
    menuEntries: List<MenuEntry>
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary)
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null,
                onClick = {}),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(UiDefaults.sideMargin)
                .fillMaxWidth(0.85f)
                .clip(RoundedCornerShape(UiDefaults.defaultCorners))
                .background(MaterialTheme.colorScheme.primary)
                .innerShadow(blur = UiDefaults.blurRadius, hideSides = false)
                .padding(UiDefaults.bigPaddings),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(UiDefaults.bigPaddings)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(UiDefaults.defaultCorners))
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(UiDefaults.bigPaddings),
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(UiDefaults.defaultPaddings),
                    text = menuTitle,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSecondary
                )
                menuHint()
            }
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(UiDefaults.defaultPaddings)
            ) {
                middleContent()
                menuEntries.forEach { action ->
                    if (action is SpacerMenuEntry) {
                        Spacer(modifier = Modifier.size(UiDefaults.defaultSpacer))
                    } else if (action is DefaultMenuEntry) {
                        ActionButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(UiDefaults.buttonSize),
                            tintColor = action.color ?: MaterialTheme.colorScheme.inversePrimary,
                            awaitOnTap = true,
                            onTap = action.action,
                            text = action.title
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
    val action: () -> Unit = {}
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
                modifier = Modifier.weight(0.3f),
                text = text,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.bodySmall
            )
            if (special?.isNotBlank() == true) {
                Text(
                    modifier = Modifier.weight(0.35f),
                    text = special,
                    textAlign = TextAlign.Start,
                    color = MaterialTheme.colorScheme.onSecondary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Text(
                modifier = Modifier.weight(0.35f),
                text = desc,
                textAlign = TextAlign.End,
                color = MaterialTheme.colorScheme.onSecondary,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

data class MenuRow(val value: String, val description: String, val isSpecial: String? = null)

@Composable
@Preview
fun PreviewMenu() {
    MaterialTheme {
        Menu(
            menuTitle = "Game Over",
            menuHint = {
                MenuRows(
                    rows = listOf(
                        MenuRow("Hello", "1234"),
                        MenuRow("Hello", "1234"),
                        MenuRow("Hello", "1234", "OKay!"),
                        MenuRow("Hello", "1234"),
                        MenuRow("Hello", "1234", "Personal Best!"),
                    )
                )
            },
            menuEntries = listOf(DefaultMenuEntry("Resume", Color.Blue), DefaultMenuEntry("Exit"))
        )
    }
}