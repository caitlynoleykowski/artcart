@file:OptIn(ExperimentalMaterial3Api::class)
package com.coco0o0o.artcart.core.designsystem.component

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.coco0o0o.artcart.core.designsystem.theme.ArtCartTheme

@Composable
fun ArtCartTopAppBar(
    @StringRes titleRes: Int,
    navigationIcon: ImageVector,
    navigationIconContentDescription: String,
    modifier: Modifier = Modifier,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(
        containerColor = ArtCartFunkyTokens.Cream,
        titleContentColor = ArtCartFunkyTokens.DeepMaroon,
        navigationIconContentColor = ArtCartFunkyTokens.Pink,
        actionIconContentColor = ArtCartFunkyTokens.Pink,
    ),
    onNavigationClick: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = { Text(text = stringResource(id = titleRes), fontWeight = FontWeight.Black) },
        navigationIcon = {
//            IconButton(onClick = onNavigationClick) {
//                Icon(imageVector = navigationIcon, contentDescription = navigationIconContentDescription, tint = ArtCartFunkyTokens.Pink)
//            }
        },
        actions = {},
        colors = colors,
        modifier = modifier.testTag("ArtCartTopAppBar"),
    )
}

@Preview("Top App Bar")
@Composable
private fun ArtCartTopAppBarPreview() {
    ArtCartTheme {
        ArtCartTopAppBar(
            titleRes = android.R.string.untitled,
            navigationIcon = Icons.Rounded.Search,
            navigationIconContentDescription = "Navigation icon",
        )
    }
}
