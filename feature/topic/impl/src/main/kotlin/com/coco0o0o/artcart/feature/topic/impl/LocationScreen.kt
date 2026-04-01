/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.coco0o0o.artcart.feature.topic.impl

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.coco0o0o.artcart.core.designsystem.component.DynamicAsyncImage
import com.coco0o0o.artcart.core.designsystem.component.NiaFilterChip
import com.coco0o0o.artcart.core.designsystem.component.NiaLoadingWheel
import com.coco0o0o.artcart.core.designsystem.icon.NiaIcons
import com.coco0o0o.artcart.core.designsystem.theme.NiaTheme
import com.coco0o0o.artcart.core.model.data.ArtLocation
import com.coco0o0o.artcart.core.ui.TrackScrollJank
import com.coco0o0o.artcart.core.ui.R as UiR
import com.coco0o0o.artcart.feature.topic.api.R as TopicR

@Composable
fun LocationScreen(
    showBackButton: Boolean,
    onBackClick: () -> Unit,
    onTopicClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LocationViewModel = hiltViewModel(),
) {
    val locationUiState: LocationUiState by viewModel.locationUiState.collectAsStateWithLifecycle()

//    LocationScreen(
//        locationUiState = locationUiState,
//        modifier = modifier.testTag("location:${viewModel.locationId}"),
//        showBackButton = showBackButton,
//        onBackClick = onBackClick,
//        onFollowClick = viewModel::followLocationToggle,
//        onTopicClick = onTopicClick,
//    )
    TestScreen()
}

@Composable
internal fun TestScreen(
    list: List<String> = listOf("test", "testasf", "teoaifsd", "0832rhds"),
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        items(list.size) {
            Text(text = list[it])
        }
        item {
            Button({}) {
                Text(text = "Click here")
            }
            TextField(
                value = "Default",
                onValueChange = {},
                label = { Text("Label") },
            )
        }
    }
}

@VisibleForTesting
@Composable
internal fun LocationScreen(
    locationUiState: LocationUiState,
    showBackButton: Boolean,
    onBackClick: () -> Unit,
    onFollowClick: (Boolean) -> Unit,
    onTopicClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state = rememberLazyListState()
    TrackScrollJank(scrollableState = state, stateName = "location:screen")
    Box(
        modifier = modifier,
    ) {
        LazyColumn(
            state = state,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
            }
            when (locationUiState) {
                LocationUiState.Loading -> item {
                    NiaLoadingWheel(
                        modifier = modifier,
                        contentDesc = stringResource(id = TopicR.string.feature_topic_api_loading),
                    )
                }

                is LocationUiState.Success -> {
                    item {
                        LocationToolbar(
                            showBackButton = showBackButton,
                            onBackClick = onBackClick,
                            onFollowClick = onFollowClick,
                            uiState = locationUiState.location,
                        )
                    }
                    locationBody(
                        name = locationUiState.location.name,
                        description = locationUiState.location.address,
                        imageUrl = locationUiState.location.photoUrl,
                        onTopicClick = onTopicClick,
                    )
                }

                LocationUiState.Error -> TODO()
            }
            item {
                Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
            }
        }
        /* TODO: Reviews
        val itemsAvailable = topicItemsSize(topicUiState, newsUiState)
        val scrollbarState = state.scrollbarState(
            itemsAvailable = itemsAvailable,
        )
        state.DraggableScrollbar(
            modifier = Modifier
                .fillMaxHeight()
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(horizontal = 2.dp)
                .align(Alignment.CenterEnd),
            state = scrollbarState,
            orientation = Orientation.Vertical,
            onThumbMoved = state.rememberDraggableScroller(
                itemsAvailable = itemsAvailable,
            ),
        ) */
    }
}

//private fun topicItemsSize(
//    topicUiState: TopicUiState,
//    newsUiState: NewsUiState,
//) = when (topicUiState) {
//    TopicUiState.Error -> 0 // Nothing
//    TopicUiState.Loading -> 1 // Loading bar
//    is TopicUiState.Success -> when (newsUiState) {
//        NewsUiState.Error -> 0 // Nothing
//        NewsUiState.Loading -> 1 // Loading bar
//        is NewsUiState.Success -> 2 + newsUiState.news.size // Toolbar, header
//    }
//}

private fun LazyListScope.locationBody(
    name: String,
    description: String,
    imageUrl: String,
    onTopicClick: (String) -> Unit,
) {
    // TODO: Show icon if available
    item {
        LocationHeader(name, description, imageUrl)
    }
}

@Composable
private fun LocationHeader(name: String, description: String, imageUrl: String) {
    Column(
        modifier = Modifier.padding(horizontal = 24.dp),
    ) {
        DynamicAsyncImage(
            imageUrl = imageUrl,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(132.dp)
                .padding(bottom = 12.dp),
        )
        Text(name, style = MaterialTheme.typography.displayMedium)
        if (description.isNotEmpty()) {
            Text(
                description,
                modifier = Modifier.padding(top = 24.dp),
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Preview
@Composable
private fun LocationBodyPreview() {
    NiaTheme {
        LazyColumn {
            locationBody(
                name = "Jetpack Compose",
                description = "Lorem ipsum maximum",
                imageUrl = "",
                onTopicClick = {},
            )
        }
    }
}

@Composable
private fun LocationToolbar(
    uiState: ArtLocation,
    modifier: Modifier = Modifier,
    showBackButton: Boolean = true,
    onBackClick: () -> Unit = {},
    onFollowClick: (Boolean) -> Unit = {},
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp),
    ) {
        if (showBackButton) {
            IconButton(onClick = { onBackClick() }) {
                Icon(
                    imageVector = NiaIcons.ArrowBack,
                    contentDescription = stringResource(
                        id = UiR.string.core_ui_back,
                    ),
                )
            }
        } else {
            // Keeps the NiaFilterChip aligned to the end of the Row.
            Spacer(modifier = Modifier.width(1.dp))
        }
        val selected = uiState.isFavorite
        NiaFilterChip(
            selected = selected,
            onSelectedChange = onFollowClick,
            modifier = Modifier.padding(end = 24.dp),
        ) {
            if (selected) {
                Text("FOLLOWING")
            } else {
                Text("NOT FOLLOWING")
            }
        }
    }
}
