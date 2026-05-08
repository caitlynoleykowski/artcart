package com.coco0o0o.artcart.core.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.coco0o0o.artcart.core.designsystem.theme.ArtCartTheme
import androidx.compose.material3.HorizontalDivider

@Composable
fun ArtCartTab(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: @Composable () -> Unit,
) {
    Tab(
        selected = selected,
        onClick = onClick,
        modifier = modifier.clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
        enabled = enabled,
        selectedContentColor = ArtCartFunkyTokens.Pink,
        unselectedContentColor = ArtCartFunkyTokens.DeepMaroon.copy(alpha = .64f),
        text = {
            val style = MaterialTheme.typography.labelLarge.copy(textAlign = TextAlign.Center)
            ProvideTextStyle(value = style) {
                Box(modifier = Modifier.padding(top = ArtCartTabDefaults.TabTopPadding, bottom = 3.dp)) { text() }
            }
        },
    )
}

@Composable
fun ArtCartTabRow(
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    tabs: @Composable () -> Unit,
) {
    TabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = modifier,
        containerColor = ArtCartFunkyTokens.Cream,
        contentColor = ArtCartFunkyTokens.DeepMaroon,
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                height = 5.dp,
                color = ArtCartFunkyTokens.Pink,
            )
        },
        divider = {
            HorizontalDivider(
                thickness = 2.dp,
                color = ArtCartFunkyTokens.Lemon.copy(alpha = .7f),
            )
        },
        tabs = tabs,
    )
}

@ThemePreviews
@Composable
fun TabsPreview() {
    ArtCartTheme {
        val titles = listOf("Locations", "Saved")
        ArtCartTabRow(selectedTabIndex = 0) {
            titles.forEachIndexed { index, title ->
                ArtCartTab(
                    selected = index == 0,
                    onClick = { },
                    text = { Text(text = title) },
                )
            }
        }
    }
}

object ArtCartTabDefaults { val TabTopPadding = 7.dp }
