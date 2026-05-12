package com.coco0o0o.artcart.core.designsystem.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.coco0o0o.artcart.core.designsystem.theme.ArtCartTheme

@Composable
fun ArtCartIconToggleButton(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: @Composable () -> Unit,
    checkedIcon: @Composable () -> Unit = icon,
) {
    IconToggleButton(
        checked = checked,
        onCheckedChange = onCheckedChange,
//        modifier = modifier.border(
//            BorderStroke(1.5.dp, if (checked) ArtCartFunkyTokens.Lemon else ArtCartFunkyTokens.Pink.copy(alpha = .55f)),
//            RoundedCornerShape(topStart = 18.dp, topEnd = 8.dp, bottomEnd = 18.dp, bottomStart = 8.dp),
//        ),
        enabled = enabled,
        colors = IconButtonDefaults.iconToggleButtonColors(
            contentColor = ArtCartFunkyTokens.Pink,
            checkedContainerColor = ArtCartFunkyTokens.Pink,
            checkedContentColor = Color.White,
            disabledContainerColor = if (checked) ArtCartFunkyTokens.Pink.copy(alpha = ArtCartIconButtonDefaults.DISABLED_ICON_BUTTON_CONTAINER_ALPHA) else Color.Transparent,
        ),
    ) { if (checked) checkedIcon() else icon() }
}

@Composable
fun ArtCartIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector,
) {
    IconButton(
        onClick = onClick,
        enabled = enabled,
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = Color.White,
            containerColor = ArtCartFunkyTokens.Pink,
            disabledContainerColor = ArtCartFunkyTokens.Pink.copy(alpha = ArtCartIconButtonDefaults.DISABLED_ICON_BUTTON_CONTAINER_ALPHA),
        ),
        modifier = modifier
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Back",
        )
    }
}

@ThemePreviews
@Composable
fun IconToggleButtonPreview() {
    ArtCartTheme { ArtCartIconToggleButton(checked = true, onCheckedChange = { }, icon = { Icon(imageVector = Icons.Rounded.Check, contentDescription = null) }, checkedIcon = { Icon(imageVector = Icons.Rounded.Check, contentDescription = null) }) }
}

@ThemePreviews
@Composable
fun IconToggleButtonPreviewUnchecked() {
    ArtCartTheme { ArtCartIconToggleButton(checked = false, onCheckedChange = { }, icon = { Icon(imageVector = Icons.Rounded.Add, contentDescription = null) }, checkedIcon = { Icon(imageVector = Icons.Rounded.Add, contentDescription = null) }) }
}

@ThemePreviews
@Composable
fun IconButtonPreview() {
    ArtCartTheme { ArtCartIconButton(onClick = { }, icon = Icons.Rounded.Check) }
}

object ArtCartIconButtonDefaults {
    const val DISABLED_ICON_BUTTON_CONTAINER_ALPHA = 0.12f
}
