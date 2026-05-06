/*
 * Copyright 2022 The Android Open Source Project
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

package com.coco0o0o.artcart.feature.saved.impl

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coco0o0o.artcart.core.data.repository.LocationsRepository
import com.coco0o0o.artcart.core.model.data.ArtLocation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedViewModel2 @Inject constructor(
    private val locationsRepository: LocationsRepository,
) : ViewModel() {

    var shouldDisplayUndoBookmark by mutableStateOf(false)
    private var lastRemovedBookmarkId: String? = null

    val savedUiState: StateFlow<FavoritedArtLocationsUiState> =
        locationsRepository.getFavoriteLocations()
            .map<List<ArtLocation>, FavoritedArtLocationsUiState>(FavoritedArtLocationsUiState::ArtLocations)
            .onStart { emit(FavoritedArtLocationsUiState.Loading) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = FavoritedArtLocationsUiState.Loading,
            )

    fun removeFromSavedResources(newsResourceId: String) {
        viewModelScope.launch {
            shouldDisplayUndoBookmark = true
            lastRemovedBookmarkId = newsResourceId
            locationsRepository.updateLocationIsFavorite(newsResourceId, isFavorite = false)
        }
    }

    fun undoBookmarkRemoval() {
        viewModelScope.launch {
            lastRemovedBookmarkId?.let {
                locationsRepository.updateLocationIsFavorite(it, isFavorite = true)
            }
        }
        clearUndoState()
    }

    fun clearUndoState() {
        shouldDisplayUndoBookmark = false
        lastRemovedBookmarkId = null
    }
}

sealed interface FavoritedArtLocationsUiState {
    data object Loading : FavoritedArtLocationsUiState

    data class ArtLocations(
        val artLocations: List<ArtLocation>,
    ) : FavoritedArtLocationsUiState

    data object Empty : FavoritedArtLocationsUiState
}