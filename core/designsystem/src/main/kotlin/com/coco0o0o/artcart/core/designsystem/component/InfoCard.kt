package com.coco0o0o.artcart.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun InfoCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = ArtCartFunkyTokens.Pink.copy(alpha = .55f),
                shape = RoundedCornerShape(
                    topStart = 32.dp,
                    topEnd = 12.dp,
                    bottomEnd = 32.dp,
                    bottomStart = 12.dp,
                ),
            )
            .padding(20.dp),
        content = content,
    )
}