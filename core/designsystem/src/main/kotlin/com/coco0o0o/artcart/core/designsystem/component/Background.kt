package com.coco0o0o.artcart.core.designsystem.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.coco0o0o.artcart.core.designsystem.theme.ArtCartTheme
import com.coco0o0o.artcart.core.designsystem.theme.GradientColors
import com.coco0o0o.artcart.core.designsystem.theme.LocalBackgroundTheme
import com.coco0o0o.artcart.core.designsystem.theme.LocalGradientColors
import kotlin.math.tan

@Composable
fun ArtCartBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val color = LocalBackgroundTheme.current.color
    val tonalElevation = LocalBackgroundTheme.current.tonalElevation
    Surface(
        color = if (color == Color.Unspecified) Color.Transparent else color,
        tonalElevation = if (tonalElevation == Dp.Unspecified) 0.dp else tonalElevation,
        modifier = modifier.fillMaxSize(),
    ) {
        CompositionLocalProvider(LocalAbsoluteTonalElevation provides 0.dp) { content() }
    }
}

@Composable
fun ArtCartGradientBackground(
    modifier: Modifier = Modifier,
    gradientColors: GradientColors = LocalGradientColors.current,
    content: @Composable () -> Unit,
) {
    val currentTopColor by rememberUpdatedState(gradientColors.top)
    val currentBottomColor by rememberUpdatedState(gradientColors.bottom)
    Surface(
        color = if (gradientColors.container == Color.Unspecified) {
            Color.Transparent
        } else {
            gradientColors.container
        },
        modifier = modifier.fillMaxSize(),
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .drawWithCache {
                    val offset = size.height * tan(
                        Math
                            .toRadians(11.06)
                            .toFloat(),
                    )

                    val start = Offset(size.width / 2 + offset / 2, 0f)
                    val end = Offset(size.width / 2 - offset / 2, size.height)
                    val topGradient = Brush.linearGradient(
                        0f to if (currentTopColor == Color.Unspecified) {
                            Color.Transparent
                        } else {
                            currentTopColor
                        },
                        0.724f to Color.Transparent,
                        start = start,
                        end = end,
                    )
                    val bottomGradient = Brush.linearGradient(
                        0.2552f to Color.Transparent,
                        1f to if (currentBottomColor == Color.Unspecified) {
                            Color.Transparent
                        } else {
                            currentBottomColor
                        },
                        start = start,
                        end = end,
                    )

                    onDrawBehind {
                        drawRect(topGradient)
                        drawRect(bottomGradient)
                    }
                },
        ) {
            content()
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light theme")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark theme")
annotation class ThemePreviews

@ThemePreviews
@Composable
fun BackgroundDefault() { ArtCartTheme(disableDynamicTheming = true) { ArtCartBackground(Modifier.size(100.dp), content = {}) } }

@ThemePreviews
@Composable
fun BackgroundDynamic() { ArtCartTheme(disableDynamicTheming = false) { ArtCartBackground(Modifier.size(100.dp), content = {}) } }

@ThemePreviews
@Composable
fun BackgroundAndroid() { ArtCartTheme(androidTheme = true) { ArtCartBackground(Modifier.size(100.dp), content = {}) } }

@ThemePreviews
@Composable
fun GradientBackgroundDefault() { ArtCartTheme(disableDynamicTheming = true) { ArtCartGradientBackground(Modifier.size(100.dp), content = {}) } }

@ThemePreviews
@Composable
fun GradientBackgroundDynamic() { ArtCartTheme(disableDynamicTheming = false) { ArtCartGradientBackground(Modifier.size(100.dp), content = {}) } }

@ThemePreviews
@Composable
fun GradientBackgroundAndroid() { ArtCartTheme(androidTheme = true) { ArtCartGradientBackground(Modifier.size(100.dp), content = {}) } }
