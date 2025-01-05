package de.ka.crunchr.ui.composables.utils

import android.graphics.BlurMaskFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

fun Modifier.shadowTap(
    enabled: Boolean = true,
    onTap: () -> Unit,
    awaitOnTap: Boolean = false,
    backgroundColor: Color,
    borderColor: Color = Color.Black.copy(alpha = 0.75f),
    cornersRadius: Dp = 0.dp,
    corners: Corners = Corners(),
    foregroundColor: Color? = null
) = composed {
    val shape = RoundedCornerShape(
        topStart = if (corners.topLeft) cornersRadius else 0.dp,
        topEnd = if (corners.topRight) cornersRadius else 0.dp,
        bottomStart = if (corners.bottomLeft) cornersRadius else 0.dp,
        bottomEnd = if (corners.bottomRight) cornersRadius else 0.dp,)
    val isTapped: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }
    LaunchedEffect(enabled) {
        isTapped.value = false
    }
    if (!enabled) {
        Modifier.alpha(0.4f)
    } else {
        Modifier.pointerInput(key1 = Unit) {
            detectTapGestures(
                onPress = {
                    isTapped.value = true
                    if (!awaitOnTap) {
                        onTap()
                    }
                    tryAwaitRelease()
                    delay(50)
                    isTapped.value = false
                },
                onTap = {
                    if (awaitOnTap) {
                        onTap()
                    }
                }
            )
        }
    } then border(
        BorderStroke(0.5.dp, color = borderColor),
       shape
    ) then clip(
        shape
    ) then background(
        backgroundColor
    ) then background(
        foregroundColor ?: backgroundColor
    ) then background(
        if (enabled) Color.Transparent else Color.Black.copy(alpha = 0.15f)
    ) then shadowing(
        isTapped,
        corners,
        cornersRadius
    ) then scaleOnPressed(isTapped)
}

fun Modifier.shadowing(
    isTapped: MutableState<Boolean>,
    corners: Corners,
    cornersRadius: Dp
) = composed {
    val scale by animateFloatAsState(
        if (isTapped.value) {
            1f
        } else {
            0f
        }, label = ""
    )
    this
        .innerShadow(
            color = MaterialTheme.colorScheme.secondary.copy(alpha = scale),
            cornersRadius = cornersRadius,
            corners = corners,
            blur = 4.dp,
            offsetX = (-2).dp,
            offsetY = 4.dp,
            hideSides = false
        )
        .innerShadow(
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = scale),
            cornersRadius = cornersRadius,
            corners = corners,
            blur = 4.dp,
            offsetX = 2.dp,
            offsetY = 2.dp,
            hideSides = false
        )
}

fun Modifier.tap(onTap: () -> Unit) =
    composed {
        val isTapped: MutableState<Boolean> = remember {
            mutableStateOf(false)
        }
        Modifier.pointerInput(key1 = Unit) {
            detectTapGestures(
                onPress = {
                    isTapped.value = true
                    onTap()
                    tryAwaitRelease()
                    delay(50)
                    isTapped.value = false
                }
            )
        } then scaleOnPressed(isTapped)
    }

fun Modifier.scaleOnPressed(
    isTapped: MutableState<Boolean>,
) = composed {
    val scale by animateFloatAsState(
        if (isTapped.value) {
            0.92f
        } else {
            1f
        }, label = ""
    )
    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
}

fun Modifier.innerShadow(
    color: Color = Color.Black.copy(alpha = 0.75f),
    cornersRadius: Dp = 0.dp,
    corners: Corners = Corners(),
    spread: Dp = 0.dp,
    blur: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp,
    hideSides: Boolean = true,
) = drawWithContent {

    drawContent()

    val rect = Rect(Offset.Zero, size)
    val paint = Paint()

    val topLeftRadius = if (corners.topLeft)
        CornerRadius(cornersRadius.toPx()) else CornerRadius.Zero
    val topRightRadius = if (corners.topRight)
        CornerRadius(cornersRadius.toPx()) else CornerRadius.Zero
    val bottomLeftRadius = if (corners.bottomLeft)
        CornerRadius(cornersRadius.toPx()) else CornerRadius.Zero
    val bottomRightRadius = if (corners.bottomRight)
        CornerRadius(cornersRadius.toPx()) else CornerRadius.Zero

    drawIntoCanvas {
        paint.color = color
        paint.isAntiAlias = true
        it.saveLayer(rect, paint)

        val rounded = Outline.Rounded(
            RoundRect(
                left = rect.left,
                top = rect.top,
                right = rect.right,
                bottom = rect.bottom,
                topLeftCornerRadius = topLeftRadius,
                topRightCornerRadius = topRightRadius,
                bottomLeftCornerRadius = bottomLeftRadius,
                bottomRightCornerRadius = bottomRightRadius,
            )
        )
        it.drawOutline(rounded, paint)

        val frameworkPaint = paint.asFrameworkPaint()
        frameworkPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        if (blur.toPx() > 0) {
            frameworkPaint.maskFilter = BlurMaskFilter(blur.toPx(), BlurMaskFilter.Blur.NORMAL)
        }
        val left = if (offsetX > 0.dp) {
            rect.left + offsetX.toPx()
        } else {
            rect.left * 2
        }
        val top = if (offsetY > 0.dp) {
            rect.top + offsetY.toPx()
        } else {
            rect.top
        }
        val right = if (offsetX < 0.dp) {
            rect.right + offsetX.toPx()
        } else {
            rect.right
        }
        val bottom = if (offsetY < 0.dp) {
            rect.bottom + offsetY.toPx()
        } else {
            rect.bottom
        }
        paint.color = Color.Black

        val innerRounded = Outline.Rounded(
            RoundRect(
                left = if (hideSides) -100f else left + spread.toPx() / 2,
                top = top + spread.toPx() / 2,
                right = if (hideSides) right + 100 else right - spread.toPx() / 2,
                bottom = bottom - spread.toPx() / 2,
                topLeftCornerRadius = topLeftRadius,
                topRightCornerRadius = topRightRadius,
                bottomLeftCornerRadius = bottomLeftRadius,
                bottomRightCornerRadius = bottomRightRadius,
            )
        )
        it.drawOutline(innerRounded, paint)

        frameworkPaint.xfermode = null
        frameworkPaint.maskFilter = null
    }
}

open class Corners(
    val topLeft: Boolean = false,
    val topRight: Boolean = false,
    val bottomLeft: Boolean = false,
    val bottomRight: Boolean = false
) {
     class All: Corners(true, true, true, true)
}