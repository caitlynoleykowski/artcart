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

package com.coco0o0o.artcart.feature.interests.impl

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coco0o0o.artcart.core.data.repository.LocationsRepository
import com.coco0o0o.artcart.core.domain.GetFollowableLocationsUseCase
import com.coco0o0o.artcart.core.domain.RefreshLocationsUseCase
import com.coco0o0o.artcart.core.model.data.ArtLocation
import com.coco0o0o.artcart.feature.interests.api.navigation.InterestsNavKey
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = InterestsViewModel.Factory::class)
class InterestsViewModel @AssistedInject constructor(
    private val savedStateHandle: SavedStateHandle,
    getFollowableArtLocationsUseCase: GetFollowableLocationsUseCase,
    private val refreshLocationsUseCase: RefreshLocationsUseCase,
    private val locationsRepository: LocationsRepository,
    // TODO: see comment below
    @Assisted val key: InterestsNavKey,
) : ViewModel() {

    val uiState: StateFlow<ArtNearbyUiState> =
        getFollowableArtLocationsUseCase().map {
            ArtNearbyUiState.ArtLocations(it)
        }
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ArtNearbyUiState.Loading,
    )

    private val currentLocation = MutableSharedFlow<Pair<Double, Double>>()

    init {
        currentLocation.onEach {
            refreshLocationsUseCase(lat = it.first, lng = it.second)
        }.launchIn(viewModelScope)
    }

    fun updateCurrentLocation(lat: Double, lng: Double) {
        viewModelScope.launch {
            currentLocation.emit(Pair(lat, lng))
        }
    }

    fun followTopic(followedTopicId: String, followed: Boolean) {
        viewModelScope.launch {
            locationsRepository.updateLocationIsFavorite(followedTopicId, isFavorite = followed)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(key: InterestsNavKey): InterestsViewModel
    }
}

sealed interface ArtNearbyUiState {
    data object Loading : ArtNearbyUiState

    data class ArtLocations(
        val artLocations: List<ArtLocation>,
    ) : ArtNearbyUiState

    data object Empty : ArtNearbyUiState
}
