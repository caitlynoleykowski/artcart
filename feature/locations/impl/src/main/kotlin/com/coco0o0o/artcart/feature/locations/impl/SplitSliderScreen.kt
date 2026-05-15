package com.coco0o0o.artcart.feature.locations.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip

@Composable
fun SplitSliderScreen(
    topContent: @Composable () -> Unit,
    bottomContent: @Composable () -> Unit,
) {
    var split by remember { mutableFloatStateOf(0.45f) }

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val density = LocalDensity.current
        val totalHeightPx = with(density) { maxHeight.toPx() }

        Column(modifier = Modifier.fillMaxSize()) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(split),
                contentAlignment = Alignment.Center,
            ) {
                topContent()
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .pointerInput(Unit) {
                        detectVerticalDragGestures { _, dragAmount ->

                            val currentPx = split * totalHeightPx
                            val newPx = currentPx + dragAmount

                            split = (newPx / totalHeightPx)
                                .coerceIn(0.2f, 0.8f)
                        }
                    },
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .width(64.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color.LightGray)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f - split),
                contentAlignment = Alignment.Center,
            ) {
                bottomContent()
            }
        }
    }
}