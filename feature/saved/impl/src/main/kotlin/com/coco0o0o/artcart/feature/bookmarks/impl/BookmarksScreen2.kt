package com.coco0o0o.artcart.feature.saved.impl

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.coco0o0o.artcart.core.designsystem.component.ArtCartLoadingWheel

@Composable
fun SavedScreen2(
    onLocationClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SavedViewModel2 = hiltViewModel(),
) {
    val uiState by viewModel.savedUiState.collectAsStateWithLifecycle()

    SavedScreen2(
        uiState = uiState,
        removeFavorite = { id: String, isFavorite: Boolean ->
            viewModel.removeFromSavedResources(id) // TODO: should be able to undo?
        },
        onLocationClick = {
            onLocationClick(it)
        },
        modifier = modifier,
    )
}

@Composable
internal fun SavedScreen2(
    uiState: FavoritedArtLocationsUiState,
    removeFavorite: (String, Boolean) -> Unit,
    onLocationClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when (uiState) {
            FavoritedArtLocationsUiState.Loading ->
                ArtCartLoadingWheel(
                    contentDesc = stringResource(id = com.coco0o0o.artcart.feature.saved.api.R.string.feature_saved_api_loading),
                )

            is FavoritedArtLocationsUiState.ArtLocations ->
                SavedTabContent(
                    locations = uiState.artLocations,
                    onLocationClick = onLocationClick,
                    onFollowButtonClick = {
                        id: String, isFavorite: Boolean -> removeFavorite(id, isFavorite)
                    }
                )

            is FavoritedArtLocationsUiState.Empty -> LocationsEmptyScreen()
        }
    }
}

@Composable
private fun LocationsEmptyScreen() {
    Text(text = stringResource(id = com.coco0o0o.artcart.feature.saved.api.R.string.feature_saved_api_empty_error))
}