package com.coco0o0o.artcart.feature.locationdetails.impl

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.coco0o0o.artcart.core.designsystem.component.DynamicAsyncImage
import com.coco0o0o.artcart.core.designsystem.component.ArtCartFilterChip
import com.coco0o0o.artcart.core.designsystem.component.ArtCartLoadingWheel
import com.coco0o0o.artcart.core.designsystem.theme.ArtCartTheme
import com.coco0o0o.artcart.core.model.data.ArtLocation
import com.coco0o0o.artcart.core.ui.R as UiR
import com.coco0o0o.artcart.feature.locationdetails.api.R as LocationDetailsR

@Composable
fun LocationScreen(
    showBackButton: Boolean,
    onBackClick: () -> Unit,
    onLocationClick: (String) -> Unit,
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
    onLocationClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state = rememberLazyListState()
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
                    ArtCartLoadingWheel(
                        modifier = modifier,
                        contentDesc = stringResource(id = LocationDetailsR.string.feature_locationdetails_api_loading),
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
                        onLocationClick = onLocationClick,
                    )
                }

                LocationUiState.Error -> TODO()
            }
            item {
                Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
            }
        }
    }
}

private fun LazyListScope.locationBody(
    name: String,
    description: String,
    imageUrl: String,
    onLocationClick: (String) -> Unit,
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
    ArtCartTheme {
        LazyColumn {
            locationBody(
                name = "Jetpack Compose",
                description = "Lorem ipsum maximum",
                imageUrl = "",
                onLocationClick = {},
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
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = stringResource(
                        id = UiR.string.core_ui_back,
                    ),
                )
            }
        } else {
            // Keeps the ArtCartFilterChip aligned to the end of the Row.
            Spacer(modifier = Modifier.width(1.dp))
        }
        val selected = uiState.isFavorite
        ArtCartFilterChip(
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
