package de.ka.crunchr.ui.composables.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

@Composable
fun OnLifeCycle(
    lifeCycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onPauseEvent: () -> Unit = {},
    onDestroyEvent: () -> Unit = {}
) {
    DisposableEffect(lifeCycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                onDestroyEvent()
            }
            else if (event == Lifecycle.Event.ON_PAUSE) {
                onPauseEvent()
            }
        }
        lifeCycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifeCycleOwner.lifecycle.removeObserver(observer)
        }
    }
}