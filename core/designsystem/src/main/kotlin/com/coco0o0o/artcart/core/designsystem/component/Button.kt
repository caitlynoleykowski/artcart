package com.coco0o0o.artcart.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.coco0o0o.artcart.core.designsystem.theme.ArtCartTheme

@Composable
fun ArtCartButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 12.dp, bottomEnd = 28.dp, bottomStart = 12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = ArtCartFunkyTokens.Pink,
            contentColor = Color.White,
            disabledContainerColor = ArtCartFunkyTokens.Pink.copy(alpha = .34f),
            disabledContentColor = Color.White.copy(alpha = .72f),
        ),
        contentPadding = contentPadding,
        content = content,
    )
}

@Composable
fun ArtCartButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    ArtCartButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        contentPadding = if (leadingIcon != null) ButtonDefaults.ButtonWithIconContentPadding else ButtonDefaults.ContentPadding,
    ) { ArtCartButtonContent(text = text, leadingIcon = leadingIcon) }
}

@Composable
fun ArtCartOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = RoundedCornerShape(topStart = 10.dp, topEnd = 26.dp, bottomEnd = 10.dp, bottomStart = 26.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = ArtCartFunkyTokens.Cream,
            contentColor = ArtCartFunkyTokens.DeepMaroon,
            disabledContentColor = ArtCartFunkyTokens.DeepMaroon.copy(alpha = .38f),
        ),
        border = BorderStroke(
            width = ArtCartButtonDefaults.OutlinedButtonBorderWidth,
            color = if (enabled) ArtCartFunkyTokens.Pink else ArtCartFunkyTokens.Pink.copy(alpha = ArtCartButtonDefaults.DISABLED_OUTLINED_BUTTON_BORDER_ALPHA),
        ),
        contentPadding = contentPadding,
        content = content,
    )
}

@Composable
fun ArtCartOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    ArtCartOutlinedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        contentPadding = if (leadingIcon != null) ButtonDefaults.ButtonWithIconContentPadding else ButtonDefaults.ContentPadding,
    ) { ArtCartButtonContent(text = text, leadingIcon = leadingIcon) }
}

@Composable
fun ArtCartTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier.clip(RoundedCornerShape(18.dp)),
        enabled = enabled,
        colors = ButtonDefaults.textButtonColors(
            contentColor = ArtCartFunkyTokens.Pink,
            disabledContentColor = ArtCartFunkyTokens.Pink.copy(alpha = .38f),
        ),
        content = content,
    )
}

@Composable
fun ArtCartTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    ArtCartTextButton(onClick = onClick, modifier = modifier, enabled = enabled) {
        ArtCartButtonContent(text = text, leadingIcon = leadingIcon)
    }
}

@Composable
private fun ArtCartButtonContent(
    text: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    if (leadingIcon != null) {
        Box(
            Modifier
                .sizeIn(maxHeight = ButtonDefaults.IconSize)
                .clip(RoundedCornerShape(50))
                .background(Color.White.copy(alpha = .18f)),
        ) { leadingIcon() }
    }
    Box(
        Modifier.padding(start = if (leadingIcon != null) ButtonDefaults.IconSpacing else 0.dp),
    ) { text() }
}

@ThemePreviews
@Composable
fun ArtCartButtonPreview() {
    ArtCartTheme { ArtCartBackground(modifier = Modifier.size(150.dp, 50.dp)) { ArtCartButton(onClick = {}, text = { Text("Test button") }) } }
}

@ThemePreviews
@Composable
fun ArtCartOutlinedButtonPreview() {
    ArtCartTheme { ArtCartBackground(modifier = Modifier.size(150.dp, 50.dp)) { ArtCartOutlinedButton(onClick = {}, text = { Text("Test button") }) } }
}

@ThemePreviews
@Composable
fun ArtCartButtonLeadingIconPreview() {
    ArtCartTheme {
        ArtCartBackground(modifier = Modifier.size(150.dp, 50.dp)) {
            ArtCartButton(onClick = {}, text = { Text("Test button") }, leadingIcon = { Icon(imageVector = Icons.Rounded.Add, contentDescription = null) })
        }
    }
}

object ArtCartButtonDefaults {
    const val DISABLED_OUTLINED_BUTTON_BORDER_ALPHA = 0.12f
    val OutlinedButtonBorderWidth = 2.dp
}
