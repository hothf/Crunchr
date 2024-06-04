package de.ka.crunchr.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.ka.crunchr.ui.composables.utils.UiDefaults
import de.ka.crunchr.ui.composables.utils.innerShadow

@Composable
fun ButtonsTray(modifier: Modifier = Modifier, input: (String) -> Unit = {}) {
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
            ActionButton(
                modifier = midModifier,
                text = "1",
                onTap = { input("1") },
            )
            ActionButton(
                modifier = midModifier,
                text = "2",
                onTap = { input("2") }
            )
            ActionButton(
                modifier = midModifier,
                text = "3",
                onTap = { input("3") }
            )
        }
        Row(modifier = rowModifier) {
            ActionButton(
                modifier = midModifier,
                text = "4",
                onTap = { input("4") }
            )
            ActionButton(
                modifier = midModifier,
                text = "5",
                onTap = { input("5") }
            )
            ActionButton(
                modifier = midModifier,
                text = "6",
                onTap = { input("6") }
            )

        }
        Row(modifier = rowModifier) {
            ActionButton(
                modifier = midModifier,
                text = "7",
                onTap = { input("7") }
            )
            ActionButton(
                modifier = midModifier,
                text = "8",
                onTap = { input("8") }
            )
            ActionButton(
                modifier = midModifier,
                text = "9",
                onTap = { input("9") }
            )
        }
        Row(modifier = rowModifier) {
            ActionButton(
                modifier = midModifier,
                text = "0",
                onTap = { input("0") }
            )
            ActionButton(
                modifier = midModifier,
                text = ".",
                onTap = { input(".") }
            )
            ActionButton(
                modifier = midModifier,
                text = "-",
                onTap = { input("-") }
            )
        }
    }


}

@Preview(heightDp = 800)
@Composable
fun PreviewButtonsTray() {
    MaterialTheme {
        ButtonsTray()
    }
}
