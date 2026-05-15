package com.coco0o0o.artcart.feature.locations.impl

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.coco0o0o.artcart.core.designsystem.component.ArtCartBackground
import com.coco0o0o.artcart.core.designsystem.component.ArtCartLoadingWheel
import com.coco0o0o.artcart.core.designsystem.theme.ArtCartTheme
import com.coco0o0o.artcart.core.ui.DevicePreviews
import com.coco0o0o.artcart.feature.locations.api.R

@Composable
internal fun LocationsList(
    uiState: ArtNearbyUiState,
    onSaveLocationClick: (String, Boolean) -> Unit,
    onLocationClick: (String) -> Unit,
    modifier: Modifier = Modifier.Companion,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Companion.CenterHorizontally,
    ) {
        when (uiState) {
            ArtNearbyUiState.Loading ->
                Box(modifier.fillMaxSize()) {
                    ArtCartLoadingWheel(
                        modifier = modifier
                            .align(Alignment.Companion.Center),
                        contentDesc = stringResource(id = R.string.feature_locations_api_loading),
                    )
                }

            is ArtNearbyUiState.ArtLocations ->
                LocationsListContent(
                    locations = uiState.artLocations,
                    onLocationClick = onLocationClick,
                    onSaveLocationClick = onSaveLocationClick,
                    selectedLocationId = null, // TODO: uiState.selectedLocationId,
                )

            is ArtNearbyUiState.PermissionNotGranted -> {
                Box(modifier.fillMaxSize()) {
                    LocationsPermissionNotGranted(
                        modifier = modifier
                            .align(Alignment.Companion.Center),
                    )
                }
            }

            is ArtNearbyUiState.Empty -> {
                Box(modifier.fillMaxSize()) {
                    LocationsListEmpty(
                        modifier = modifier
                            .align(Alignment.Companion.Center),
                    )
                }
            }
        }
    }
}

@Composable
private fun LocationsListEmpty(modifier: Modifier = Modifier.Companion) {
    Text(
        modifier = modifier,
        text = stringResource(id = R.string.feature_locations_api_empty_header),
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun LocationsPermissionNotGranted(modifier: Modifier = Modifier.Companion) {
    Text(
        modifier = modifier,
        text = stringResource(id = R.string.feature_locations_api_location_permissions_not_granted),
    )
}

@DevicePreviews
@Composable
fun LocationsListLoading() {
    ArtCartTheme {
        ArtCartBackground {
            LocationsList(
                uiState = ArtNearbyUiState.Loading,
                onSaveLocationClick = { _, _ -> },
                onLocationClick = {},
            )
        }
    }
}

@DevicePreviews
@Composable
fun LocationsListEmpty() {
    ArtCartTheme {
        ArtCartBackground {
            LocationsList(
                uiState = ArtNearbyUiState.Empty,
                onSaveLocationClick = { _, _ -> },
                onLocationClick = {},
            )
        }
    }
}