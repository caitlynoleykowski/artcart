package com.coco0o0o.artcart.feature.locations.impl

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coco0o0o.artcart.core.data.repository.LocationsRepository
import com.coco0o0o.artcart.core.domain.GetFavoriteableLocationsUseCase
import com.coco0o0o.artcart.core.domain.RefreshLocationsUseCase
import com.coco0o0o.artcart.core.model.data.ArtLocation
import com.coco0o0o.artcart.feature.locations.api.navigation.LocationsNavKey
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = LocationsViewModel.Factory::class)
class LocationsViewModel @AssistedInject constructor(
    private val savedStateHandle: SavedStateHandle,
    getFollowableArtLocationsUseCase: GetFavoriteableLocationsUseCase,
    private val refreshLocationsUseCase: RefreshLocationsUseCase,
    private val locationsRepository: LocationsRepository,
    // TODO: see comment below
    @Assisted val key: LocationsNavKey,
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

    fun saveLocation(locationId: String, followed: Boolean) {
        viewModelScope.launch {
            locationsRepository.updateLocationIsFavorite(locationId, isFavorite = followed)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(key: LocationsNavKey): LocationsViewModel
    }
}

sealed interface ArtNearbyUiState {
    data object Loading : ArtNearbyUiState

    data class ArtLocations(
        val artLocations: List<ArtLocation>,
    ) : ArtNearbyUiState

    data object Empty : ArtNearbyUiState
}
