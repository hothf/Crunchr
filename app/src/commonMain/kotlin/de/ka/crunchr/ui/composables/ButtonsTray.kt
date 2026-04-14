package de.ka.crunchr.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import de.ka.crunchr.ui.composables.utils.UiDefaults

@Composable
fun ButtonsTray(
    modifier: Modifier = Modifier,
    buttonsEnabled: Boolean = true,
    input: (String) -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val rowModifier = Modifier
            .fillMaxWidth(0.75f)
            .weight(1f)

        val midModifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .weight(1f)
            .padding(UiDefaults.smallPadding)
        Row(
            modifier = rowModifier,
            horizontalArrangement = Arrangement.Center,
        ) {
            SlideInButton(
                modifier = midModifier,
                isVisible = buttonsEnabled,
                row = 0,
                text = "1",
                onTap = { input("1") }
            )
            SlideInButton(
                modifier = midModifier,
                isVisible = buttonsEnabled,
                row = 0,
                text = "2",
                onTap = { input("2") }
            )
            SlideInButton(
                modifier = midModifier,
                isVisible = buttonsEnabled,
                row = 0,
                text = "3",
                onTap = { input("3") }
            )
        }
        Row(modifier = rowModifier) {
            SlideInButton(
                modifier = midModifier,
                isVisible = buttonsEnabled,
                row = 1,
                text = "4",
                onTap = { input("4") }
            )
            SlideInButton(
                modifier = midModifier,
                isVisible = buttonsEnabled,
                row = 1,
                text = "5",
                onTap = { input("5") }
            )
            SlideInButton(
                modifier = midModifier,
                isVisible = buttonsEnabled,
                row = 1,
                text = "6",
                onTap = { input("6") }
            )
        }
        Row(modifier = rowModifier) {
            SlideInButton(
                modifier = midModifier,
                isVisible = buttonsEnabled,
                row = 2,
                text = "7",
                onTap = { input("7") }
            )
            SlideInButton(
                modifier = midModifier,
                isVisible = buttonsEnabled,
                row = 2,
                text = "8",
                onTap = { input("8") }
            )
            SlideInButton(
                modifier = midModifier,
                isVisible = buttonsEnabled,
                row = 2,
                text = "9",
                onTap = { input("9") }
            )
        }
        Row(modifier = rowModifier) {
            SlideInButton(
                modifier = midModifier,
                isVisible = buttonsEnabled,
                row = 3,
                text = "0",
                onTap = { input("0") }
            )
            SlideInButton(
                modifier = midModifier,
                isVisible = buttonsEnabled,
                row = 3,
                text = ".",
                onTap = { input(".") }
            )
            SlideInButton(
                modifier = midModifier,
                isVisible = buttonsEnabled,
                row = 3,
                text = "-",
                onTap = { input("-") }
            )
        }
    }


}

@Composable
private fun SlideInButton(
    modifier: Modifier,
    row: Int,
    text: String,
    isVisible: Boolean,
    onTap: () -> Unit
) {
    SlidingInContent(modifier = modifier, isVisible = isVisible, index = row) {
        ActionButton(
            enabled = isVisible,
            modifier = modifier,
            text = text,
            onTap = onTap,
            cornerRadius = UiDefaults.smallIconSize,
        )
    }
}
