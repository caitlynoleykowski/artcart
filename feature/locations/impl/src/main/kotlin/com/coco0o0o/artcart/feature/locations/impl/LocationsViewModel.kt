package com.coco0o0o.artcart.feature.locations.impl

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coco0o0o.artcart.core.data.repository.LocationsRepository
import com.coco0o0o.artcart.core.domain.GetFavoriteableLocationsUseCase
import com.coco0o0o.artcart.core.domain.RefreshLocationsUseCase
import com.coco0o0o.artcart.core.model.data.ArtLocation
import com.coco0o0o.artcart.feature.locations.api.navigation.LocationsNavKey
import com.google.android.gms.maps.model.LatLng
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
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

    val permissionGrantedState = MutableStateFlow<Boolean?>(null)

    val uiState: StateFlow<ArtNearbyUiState> =
        combine(
            permissionGrantedState,
            getFollowableArtLocationsUseCase()
        ) { permissionGranted, locations ->
            when (permissionGranted) {
                false -> ArtNearbyUiState.PermissionNotGranted
                true -> ArtNearbyUiState.ArtLocations(locations)
                null -> ArtNearbyUiState.Loading
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ArtNearbyUiState.Loading,
        )
    // TODO: Don't use the google maps model for LatLng here, make a new one
    val currentLocation = MutableStateFlow<LatLng?>(null)

    init {
        currentLocation.onEach {
            if (it != null) {
                refreshLocationsUseCase(lat = it.latitude, lng = it.longitude)
            }
        }.launchIn(viewModelScope)
    }

    fun updateCurrentLocation(lat: Double, lng: Double) {
        viewModelScope.launch {
            currentLocation.value = (LatLng(lat, lng))
        }
    }

    fun saveLocation(locationId: String, followed: Boolean) {
        viewModelScope.launch {
            locationsRepository.updateLocationIsFavorite(locationId, isFavorite = followed)
        }
    }

    fun onLocationPermissionResult(granted: Boolean) {
        permissionGrantedState.value = granted
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

    data object PermissionNotGranted : ArtNearbyUiState
    data object Empty : ArtNearbyUiState
}
