package com.coco0o0o.artcart.core.designsystem.component

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

internal object ArtCartFunkyTokens {
    val Pink = Color(0xFFFB4C8E)
    val DeepMaroon = Color(0xFF641A35)
    val Ink = Color(0xFF21121A)
    val Cream = Color(0xFFFFF7DF)
    val Lemon = Color(0xFFFFE66D)
    val Mint = Color(0xFF6EF3C5)
    val Lilac = Color(0xFFCDB4FF)
    val Sky = Color(0xFF8DDCFF)

    val HotBrush = Brush.linearGradient(
        colors = listOf(Pink, Lilac, Lemon)
    )

    val CoolBrush = Brush.linearGradient(
        colors = listOf(Mint, Sky, Lilac)
    )
}
