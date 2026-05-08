package com.coco0o0o.artcart.core.designsystem.component

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.coco0o0o.artcart.core.designsystem.theme.ArtCartTheme
import kotlinx.coroutines.launch

@Composable
fun ArtCartLoadingWheel(
    contentDesc: String,
    modifier: Modifier = Modifier,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "wheel transition")
    val startValue = if (LocalInspectionMode.current) 0F else 1F
    val floatAnimValues = (0 until NUM_OF_LINES).map { remember { Animatable(startValue) } }
    LaunchedEffect(floatAnimValues) {
        (0 until NUM_OF_LINES).map { index ->
            launch {
                floatAnimValues[index].animateTo(
                    targetValue = 0F,
                    animationSpec = tween(durationMillis = 120, easing = FastOutSlowInEasing, delayMillis = 35 * index),
                )
            }
        }
    }

    val rotationAnim by infiniteTransition.animateFloat(
        initialValue = 0F,
        targetValue = 360F,
        animationSpec = infiniteRepeatable(animation = tween(durationMillis = ROTATION_TIME, easing = LinearEasing)),
        label = "wheel rotation animation",
    )

    val colors = listOf(ArtCartFunkyTokens.Pink, ArtCartFunkyTokens.Lemon, ArtCartFunkyTokens.Mint, ArtCartFunkyTokens.Lilac)
    val colorAnimValues = (0 until NUM_OF_LINES).map { index ->
        infiniteTransition.animateColor(
            initialValue = colors[index % colors.size],
            targetValue = colors[(index + 1) % colors.size],
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = ROTATION_TIME / 2
                    colors[(index + 2) % colors.size] at ROTATION_TIME / NUM_OF_LINES / 2 using LinearEasing
                    colors[index % colors.size] at ROTATION_TIME / NUM_OF_LINES using LinearEasing
                },
                repeatMode = RepeatMode.Restart,
                initialStartOffset = StartOffset(ROTATION_TIME / NUM_OF_LINES / 2 * index),
            ),
            label = "wheel color animation",
        )
    }

    Canvas(
        modifier = modifier
            .size(52.dp)
            .padding(7.dp)
            .graphicsLayer { rotationZ = rotationAnim }
            .semantics { contentDescription = contentDesc }
            .testTag("loadingWheel"),
    ) {
        repeat(NUM_OF_LINES) { index ->
            rotate(degrees = index * 30f) {
                drawLine(
                    color = colorAnimValues[index].value,
                    alpha = if (floatAnimValues[index].value < 1f) 1f else 0f,
                    strokeWidth = if (index % 3 == 0) 7F else 4.5F,
                    cap = StrokeCap.Round,
                    start = Offset(size.width / 2, size.height / 5),
                    end = Offset(size.width / 2, floatAnimValues[index].value * size.height / 3),
                )
            }
        }
    }
}

@Composable
fun ArtCartOverlayLoadingWheel(
    contentDesc: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 12.dp, bottomEnd = 28.dp, bottomStart = 12.dp),
        shadowElevation = 10.dp,
        color = ArtCartFunkyTokens.Cream.copy(alpha = 0.92f),
        modifier = modifier.size(64.dp),
    ) { ArtCartLoadingWheel(contentDesc = contentDesc) }
}

@ThemePreviews
@Composable
fun ArtCartLoadingWheelPreview() { ArtCartTheme { Surface { ArtCartLoadingWheel(contentDesc = "LoadingWheel") } } }

@ThemePreviews
@Composable
fun ArtCartOverlayLoadingWheelPreview() { ArtCartTheme { Surface { ArtCartOverlayLoadingWheel(contentDesc = "LoadingWheel") } } }

private const val ROTATION_TIME = 9000
private const val NUM_OF_LINES = 12
