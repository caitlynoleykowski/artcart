package com.coco0o0o.artcart.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.coco0o0o.artcart.core.designsystem.theme.ArtCartTheme

@Composable
fun ArtCartFilterChip(
    selected: Boolean,
    onSelectedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: @Composable () -> Unit,
) {
    FilterChip(
        selected = selected,
        onClick = { onSelectedChange(!selected) },
        label = { ProvideTextStyle(value = MaterialTheme.typography.labelSmall) { label() } },
        modifier = modifier,
        enabled = enabled,
        leadingIcon = if (selected) {{ Icon(imageVector = Icons.Rounded.Check, contentDescription = null) }} else null,
        shape = RoundedCornerShape(topStart = 18.dp, topEnd = 6.dp, bottomEnd = 18.dp, bottomStart = 6.dp),
        border = FilterChipDefaults.filterChipBorder(
            enabled = enabled,
            selected = selected,
            borderColor = ArtCartFunkyTokens.DeepMaroon,
            selectedBorderColor = ArtCartFunkyTokens.Pink,
            disabledBorderColor = ArtCartFunkyTokens.DeepMaroon.copy(alpha = ArtCartChipDefaults.DISABLED_CHIP_CONTENT_ALPHA),
            disabledSelectedBorderColor = ArtCartFunkyTokens.Pink.copy(alpha = ArtCartChipDefaults.DISABLED_CHIP_CONTENT_ALPHA),
            selectedBorderWidth = ArtCartChipDefaults.ChipBorderWidth,
        ),
        colors = FilterChipDefaults.filterChipColors(
            containerColor = Color.Transparent,
            labelColor = ArtCartFunkyTokens.DeepMaroon,
            iconColor = ArtCartFunkyTokens.DeepMaroon,
            disabledContainerColor = if (selected) ArtCartFunkyTokens.Pink.copy(alpha = ArtCartChipDefaults.DISABLED_CHIP_CONTAINER_ALPHA) else Color.Transparent,
            disabledLabelColor = ArtCartFunkyTokens.DeepMaroon.copy(alpha = ArtCartChipDefaults.DISABLED_CHIP_CONTENT_ALPHA),
            disabledLeadingIconColor = ArtCartFunkyTokens.DeepMaroon.copy(alpha = ArtCartChipDefaults.DISABLED_CHIP_CONTENT_ALPHA),
            selectedContainerColor = ArtCartFunkyTokens.Lemon,
            selectedLabelColor = ArtCartFunkyTokens.Ink,
            selectedLeadingIconColor = ArtCartFunkyTokens.Pink,
        ),
    )
}

@ThemePreviews
@Composable
fun ChipPreview() {
    ArtCartTheme { ArtCartBackground(modifier = Modifier.size(80.dp, 20.dp)) { ArtCartFilterChip(selected = true, onSelectedChange = {}) { Text("Chip") } } }
}

object ArtCartChipDefaults {
    const val DISABLED_CHIP_CONTAINER_ALPHA = 0.12f
    const val DISABLED_CHIP_CONTENT_ALPHA = 0.38f
    val ChipBorderWidth = 2.dp
}
